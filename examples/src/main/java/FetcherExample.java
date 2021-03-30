import com.google.cloud.vision.v1.ImageAnnotatorClient;
import dev.schlaubi.forp.core.stacktrace.ParsedRootStackTrace;
import dev.schlaubi.forp.fetch.JavaStackTraceFetcher;
import dev.schlaubi.forp.fetch.StackTraceFetcher;
import dev.schlaubi.forp.fetch.input.FileInput.FileType;
import dev.schlaubi.forp.fetch.input.Input;
import dev.schlaubi.forp.fetch.input.Inputs;
import dev.schlaubi.forp.fetch.processor.BinaryFileProcessor;
import dev.schlaubi.forp.fetch.processor.HastebinProcessor;
import dev.schlaubi.forp.fetch.processor.ImageFileProcessor;
import dev.schlaubi.forp.fetch.processor.PlainStringProcessor;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class FetcherExample {

  public static void main(String[] args) throws IOException {
    var googleClient = ImageAnnotatorClient.create();
    var fetcher = StackTraceFetcher.newBuilder()
        .addProcessor(new PlainStringProcessor())
        .addProcessor(new BinaryFileProcessor())
        .addProcessor(new ImageFileProcessor(googleClient))
        .addHttpFetcher(client -> new HastebinProcessor(client, "haste.schlaubi.me"))
        .build();

    fromImage(fetcher);
    fromString(fetcher);
    fromStream(fetcher);
    fromPath(fetcher);
    fromHastebin(fetcher);

    googleClient.close();
    fetcher.close();
  }

  private static void fromImage(@NotNull JavaStackTraceFetcher fetcher) {
    var image = Path.of("examples/exception.png");

    var stackTraces = fetcher.fetch(Inputs.fromImagePath(image));
    System.out.println(stackTraces);
  }

  private static void fromHastebin(@NotNull JavaStackTraceFetcher fetcher) {
    var hastebin = "https://haste.schlaubi.me/ecamubahom.avrasm";

    var stackTraces = fetcher.fetch(Input.fromString(hastebin));
    System.out.println(stackTraces);
  }

  private static void fromString(@NotNull JavaStackTraceFetcher fetcher) {
    var input = """
        java.lang.NullPointerException: Cannot invoke "java.lang.String.toString()" because "x" is null
          at ClassFinderTest.main(ClassFinderTest.java:5)
               """;

    var stackTraces = fetcher.fetch(Input.fromPlainString(input));

    System.out.println(stackTraces);
  }

  private static void fromStream(@NotNull JavaStackTraceFetcher fetcher) {
    var input = ClassLoader.getSystemResourceAsStream("trace_2.txt");

    assert input != null;
    List<ParsedRootStackTrace> stackTraces = fetcher
        .fetch(Inputs.fromInputStream(input, FileType.PLAIN_TEXT));
    System.out.println(stackTraces);
  }

  private static void fromPath(@NotNull JavaStackTraceFetcher fetcher) {
    var input = Path.of("examples/trace_3.txt");

    var stackTraces = fetcher
        .fetch(Inputs.fromPath(input, FileType.PLAIN_TEXT));
    System.out.println(stackTraces);
  }
}
