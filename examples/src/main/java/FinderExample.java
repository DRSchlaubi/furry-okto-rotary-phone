import dev.schlaubi.forp.find.StackTraceFinder;
import dev.schlaubi.forp.find.StackTraceFinderUtils;
import java.io.IOException;
import java.nio.file.Path;

public class FinderExample {

  public static void main(String[] args) throws IOException {
    fromString();
    fromInputStream();
    fromPath();
  }

  private static void fromString() {
    var input = """
        java.lang.NullPointerException: Cannot invoke "java.lang.String.toString()" because "x" is null
          at ClassFinderTest.main(ClassFinderTest.java:5)
               """;

    var stackTrace = StackTraceFinder.findStackTraces(input);
    System.out.println(stackTrace);
  }

  private static void fromInputStream() throws IOException {
    var input = ClassLoader.getSystemResourceAsStream("trace_2.txt");

    assert input != null;
    var parse = StackTraceFinderUtils.findStackTraces(input);
    System.out.println(parse);
  }

  private static void fromPath() throws IOException {
    var input = Path.of("examples/trace_3.txt");

    var parse = StackTraceFinderUtils.findStackTraces(input);
    System.out.println(parse);
  }
}
