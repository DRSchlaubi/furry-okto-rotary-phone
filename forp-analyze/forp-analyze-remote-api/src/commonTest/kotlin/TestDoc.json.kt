//language=JSON
val javadocText: String = """
      {
        "name": "string",
        "object": {
          "link": "https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/String.html",
          "type": "CLASS",
          "package": "java.lang",
          "name": "String",
          "description": "The \u003ccode\u003eString\u003c/code\u003e class represents character strings. All string literals in Java programs, such as \u003ccode\u003e\"abc\"\u003c/code\u003e, are implemented as instances of this class. \n\u003cp\u003e Strings are constant; their values cannot be changed after they are created. String buffers support mutable strings. Because String objects are immutable they can be shared. For example: \u003c/p\u003e\n\u003cblockquote\u003e\n \u003cpre\u003e     String str \u003d \"abc\";\n \u003c/pre\u003e\n\u003c/blockquote\u003e\n\u003cp\u003e is equivalent to: \u003c/p\u003e\n\u003cblockquote\u003e\n \u003cpre\u003e     char data[] \u003d {\u0027a\u0027, \u0027b\u0027, \u0027c\u0027};\n     String str \u003d new String(data);\n \u003c/pre\u003e\n\u003c/blockquote\u003e\n\u003cp\u003e Here are some more examples of how strings can be used: \u003c/p\u003e\n\u003cblockquote\u003e\n \u003cpre\u003e     System.out.println(\"abc\");\n     String cde \u003d \"cde\";\n     System.out.println(\"abc\" + cde);\n     String c \u003d \"abc\".substring(2,3);\n     String d \u003d cde.substring(1, 2);\n \u003c/pre\u003e\n\u003c/blockquote\u003e \n\u003cp\u003e The class \u003ccode\u003eString\u003c/code\u003e includes methods for examining individual characters of the sequence, for comparing strings, for searching strings, for extracting substrings, and for creating a copy of a string with all characters translated to uppercase or to lowercase. Case mapping is based on the Unicode Standard version specified by the \u003ca href\u003d\"Character.html\" title\u003d\"class in java.lang\"\u003e\u003ccode\u003eCharacter\u003c/code\u003e\u003c/a\u003e class. \u003c/p\u003e\n\u003cp\u003e The Java language provides special support for the string concatenation operator (\u0026nbsp;+\u0026nbsp;), and for conversion of other objects to strings. For additional information on string concatenation and conversion, see \u003ci\u003eThe Java™ Language Specification\u003c/i\u003e. \u003c/p\u003e\n\u003cp\u003e Unless otherwise noted, passing a \u003ccode\u003enull\u003c/code\u003e argument to a constructor or method in this class will cause a \u003ca href\u003d\"NullPointerException.html\" title\u003d\"class in java.lang\"\u003e\u003ccode\u003eNullPointerException\u003c/code\u003e\u003c/a\u003e to be thrown. \u003c/p\u003e\n\u003cp\u003eA \u003ccode\u003eString\u003c/code\u003e represents a string in the UTF-16 format in which \u003cem\u003esupplementary characters\u003c/em\u003e are represented by \u003cem\u003esurrogate pairs\u003c/em\u003e (see the section \u003ca href\u003d\"Character.html#unicode\"\u003eUnicode Character Representations\u003c/a\u003e in the \u003ccode\u003eCharacter\u003c/code\u003e class for more information). Index values refer to \u003ccode\u003echar\u003c/code\u003e code units, so a supplementary character uses two positions in a \u003ccode\u003eString\u003c/code\u003e. \u003c/p\u003e\n\u003cp\u003eThe \u003ccode\u003eString\u003c/code\u003e class provides methods for dealing with Unicode code points (i.e., characters), in addition to those for dealing with Unicode code units (i.e., \u003ccode\u003echar\u003c/code\u003e values). \u003c/p\u003e\n\u003cp\u003eUnless otherwise noted, methods for comparing Strings do not take locale into account. The \u003ca href\u003d\"../text/Collator.html\" title\u003d\"class in java.text\"\u003e\u003ccode\u003eCollator\u003c/code\u003e\u003c/a\u003e class provides methods for finer-grain, locale-sensitive String comparison.\u003c/p\u003e",
          "stripped_description": "The String class represents character strings. All string literals in Java programs, such as \"abc\", are implemented as instances of this class. Strings are constant; their values cannot be changed after they are created. String buffers support mutable strings. Because String objects are immutable they can be shared. For example:      String str \u003d \"abc\";\n is equivalent to:      char data[] \u003d {\u0027a\u0027, \u0027b\u0027, \u0027c\u0027};\n     String str \u003d new String(data);\n Here are some more examples of how strings can be used:      System.out.println(\"abc\");\n     String cde \u003d \"cde\";\n     System.out.println(\"abc\" + cde);\n     String c \u003d \"abc\".substring(2,3);\n     String d \u003d cde.substring(1, 2);\n The class String includes methods for examining individual characters of the sequence, for comparing strings, for searching strings, for extracting substrings, and for creating a copy of a string with all characters translated to uppercase or to lowercase. Case mapping is based on the Unicode Standard version specified by the Character class. The Java language provides special support for the string concatenation operator ( + ), and for conversion of other objects to strings. For additional information on string concatenation and conversion, see The Java™ Language Specification. Unless otherwise noted, passing a null argument to a constructor or method in this class will cause a NullPointerException to be thrown. A String represents a string in the UTF-16 format in which supplementary characters are represented by surrogate pairs (see the section Unicode Character Representations in the Character class for more information). Index values refer to char code units, so a supplementary character uses two positions in a String. The String class provides methods for dealing with Unicode code points (i.e., characters), in addition to those for dealing with Unicode code units (i.e., char values). Unless otherwise noted, methods for comparing Strings do not take locale into account. The Collator class provides methods for finer-grain, locale-sensitive String comparison.",
          "annotations": [],
          "deprecated": false,
          "deprecation_message": "",
          "modifiers": [
            "public",
            "final"
          ],
          "metadata": {
            "extensions": [
              "java.lang.Object"
            ],
            "implementations": [
              "java.lang.CharSequence",
              "java.lang.Comparable\u003cString\u003e",
              "java.io.Serializable"
            ],
            "all_implementations": [
              "java.lang.CharSequence",
              "java.lang.Comparable",
              "java.lang.String",
              "java.io.Serializable"
            ],
            "super_interfaces": [],
            "sub_interfaces": [],
            "sub_classes": [],
            "implementing_classes": [],
            "methods": [
              "java.lang.String#stripTrailing",
              "java.lang.String#codePointBefore",
              "java.lang.String#toString",
              "java.lang.String#equalsIgnoreCase",
              "java.lang.String#codePointCount",
              "java.lang.String#stripLeading",
              "java.lang.String#toCharArray",
              "java.lang.String#String",
              "java.lang.String#intern",
              "java.lang.String#lastIndexOf",
              "java.lang.String#equals",
              "java.lang.String#chars",
              "java.lang.String#lines",
              "java.lang.String#startsWith",
              "java.lang.String#copyValueOf",
              "java.lang.String#codePointAt",
              "java.lang.String#replaceAll",
              "java.lang.String#matches",
              "java.lang.String#length",
              "java.lang.String#isBlank",
              "java.lang.String#contains",
              "java.lang.String#toUpperCase",
              "java.lang.String#subSequence",
              "java.lang.String#compareTo",
              "java.lang.String#codePoints",
              "java.lang.String#regionMatches",
              "java.lang.String#join",
              "java.lang.String#substring",
              "java.lang.String#endsWith",
              "java.lang.String#charAt",
              "java.lang.String#split",
              "java.lang.String#strip",
              "java.lang.String#indexOf",
              "java.lang.String#repeat",
              "java.lang.String#contentEquals",
              "java.lang.String#replaceFirst",
              "java.lang.String#trim",
              "java.lang.String#getChars",
              "java.lang.String#toLowerCase",
              "java.lang.String#valueOf",
              "java.lang.String#compareToIgnoreCase",
              "java.lang.String#hashCode",
              "java.lang.String#isEmpty",
              "java.lang.String#replace",
              "java.lang.String#concat",
              "java.lang.String#offsetByCodePoints",
              "java.lang.String#format",
              "java.lang.String#getBytes"
            ],
            "fields": [
              "java.lang.String%CASE_INSENSITIVE_ORDER"
            ]
          }
        }
      }
""".trimIndent()