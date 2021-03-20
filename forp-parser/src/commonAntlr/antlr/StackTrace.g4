grammar StackTrace;

// Root rule parsing the exception message
// the stack
// and  possible caused exceptions
stackTrace: messageLine stackTraceLine+ causedByLine*;

// A stack trace element line
// Can either be ellipsis: '... {int} more' (see ellipsisLine)
// Or an actual stack trace line (see atLine)
stackTraceLine: (atLine | ellipsisLine);

// Normal stack trace element:
// AT: at
// qualifiedMethod: org.bukkit.plugin.java.PluginClassLoader.findClass
// methodFileDefinition: PluginClassLoader.java:101
// methodFileSource: ~[spigot1.8.jar:git-Spigot-c3c767f-33d5de3]
atLine: AT qualifiedMethod methodFileDefinition methodFileSource?;

// See atLine
methodFileSource: TILDE? OPENING_BRACKETS jarFile COLON string CLOSING_BRACKETS;

// See atLine
methodFileDefinition: OPENING_PARENTHESES classFile CLOSING_PARENTHESES;

whiteSpacedString: (string | WS);

lineNumber: Number;

causedByLine: CAUSED_BY stackTrace;

ellipsisLine: ELLIPSIS Number MORE_;

messageLine: (qualifiedClass message?);

qualifiedClass: packagePath? className innerClassName*;

innerClassName: ('$' className);

packagePath: (identifier DOT)+;

classFile: (sourceFile | NATIVE_METHOD | UNKNOWN_SOURCE);

jarFile: (QUESTION_MARK | string DOT TheWordJar);

qualifiedMethod: qualifiedClass DOT (methodName | constructorDef | lambda)?;

lambda: LAMBDA DOLLAR Number;

methodName: identifier;

className: JavaWord;

// Workaround so ANTLR doesn't try to recognize file endings where there are no file endings
identifier: (JavaCharacter | JavaWord | TheWordJava | TheWordJar | Number)+;

message: COLON whiteSpacedString?;

sourceFile: sourceFileName COLON lineNumber;

sourceFileName: identifier DOT sourceFileEnding;

sourceFileEnding: (TheWordJava | TheWordKt | TheWordGroovy);

// This was called constructor initially but this would clash with a JS declaration
constructorDef: INIT;
string: (JavaWord | JavaCharacter | Number | DOT | Symbol | HYPHEN | TheWordJar | Qoute | sourceFileEnding | OPENING_PARENTHESES | CLOSING_PARENTHESES | OPENING_BRACKETS | TILDE | QUESTION_MARK | COLON | MORE_ | ',' | '/')+;

Number: Digit +;

JavaCharacter: (CapitalLetter | NonCapitalLetter | Symbol | Digit);

DOT: '.';

AT: 'at';

CAUSED_BY: 'Caused by:';

MORE_: 'more';

ELLIPSIS: '...';

COLON: ':';

OPENING_PARENTHESES: '(';

CLOSING_PARENTHESES: ')';

OPENING_BRACKETS: '[';

CLOSING_BRACKETS: ']';

NATIVE_METHOD: 'Native Method';

Qoute: (SINGLE_QOUTE | DOUBLE_QOUTE | BACKTICK);

SINGLE_QOUTE: '\'';
DOUBLE_QOUTE: '"';
BACKTICK: '`';

UNKNOWN_SOURCE: 'Unknown Source';

INIT: '<init>';

NonCapitalLetter: 'a' .. 'z';

CapitalLetter: 'A' .. 'Z';

Symbol: '_';

HYPHEN: '-';

Digit: '0'..'9';

TILDE: '~';
DOLLAR: '$';
QUESTION_MARK: '?';

LAMBDA: 'lambda';

WS: (' ' | '\r' | '\t' | '\u000C' | '\n' | 'こ') -> skip;


TheWordJava: 'java';
TheWordKt: 'kt';
TheWordGroovy: 'groovy';

TheWordJar: 'jar';

JavaWord: (JavaCharacter)+;
