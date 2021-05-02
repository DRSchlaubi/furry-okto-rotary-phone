package dev.schlaubi.forp.fetch.processor

import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.Image
import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.protobuf.ByteString
import dev.schlaubi.forp.fetch.input.FileInput
import dev.schlaubi.forp.fetch.input.Input
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * Exception thrown in case an error occurs whilst reading an image.
 *
 * @property error the [com.google.rpc.Status] we got indicating the error
 */
public class ImageReadingError(message: String, public val error: com.google.rpc.Status) :
    RuntimeException(message + error)

/**
 * Implementation of [InputProcessor] which uses an [ImageAnnotatorClient] to convert the image
 * into a string of text.
 *
 * @param client an [ImageAnnotatorClient] used to read the image text
 *
 * @throws ImageReadingError when vision errors
 */
@Suppress("BlockingMethodInNonBlockingContext")
public class ImageFileProcessor(private val client: ImageAnnotatorClient) :
    InputProcessor<FileInput, ByteReadChannel> {
    override fun supports(input: Input): Boolean =
        (input as? FileInput)?.fileType == FileInput.FileType.IMAGE

    override fun processInput(input: FileInput): ByteReadChannel = input.input

    override suspend fun fetchInput(data: ByteReadChannel): List<String> {
        val input = data.toInputStream()
        val imageBytes = withContext(Dispatchers.IO) {
            ByteString.readFrom(input)
        }

        val image = Image.newBuilder().setContent(imageBytes).build()
        val textDetection = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build()
        val request = AnnotateImageRequest.newBuilder()
            .addFeatures(textDetection)
            .setImage(image)
            .build()

        val response = try {
            client.batchAnnotateImages(listOf(request)).responsesList.first()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }

        if (response.hasError()) {
            throw ImageReadingError("Could not read image", response.error)
        }

        return listOf(response.textAnnotationsList.first().description)
    }
}
