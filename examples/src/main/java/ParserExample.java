import dev.schlaubi.forp.core.StackTraceParser;
import dev.schlaubi.forp.core.StackTraceParserUtils;
import java.io.IOException;
import java.nio.file.Path;

public class ParserExample {

  public static void main(String[] args) throws IOException {
    fromString();
    fromInputStream();
    fromPath();
  }

  private static void fromString() {
    var input = """
        java.lang.NullPointerException: Cannot invoke "java.lang.String.toString()" because "x" is null
          at Test.main(Test.java:5)
               """;

    var stackTrace = StackTraceParser.parse(input);
    System.out.println(stackTrace);
  }

  private static void fromInputStream() throws IOException {
    var input = ClassLoader.getSystemResourceAsStream("trace_2.txt");

    assert input != null;
    var parse = StackTraceParserUtils.parse(input);
    System.out.println(parse);
  }

  private static void fromPath() throws IOException {
    var input = Path.of("examples/trace_3.txt");

    var parse = StackTraceParserUtils.parse(input);
    System.out.println(parse);
  }
}
