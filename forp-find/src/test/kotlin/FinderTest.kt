import dev.schlaubi.forp.core.stacktrace.RootStackTrace
import kotlin.test.Test

class FinderTest {

    @Test
    fun `find one simple stack trace`() = checkFileForInputs("test_trace_1.txt") {
        size shouldBe 1

        val stackTrace = first()
        stackTrace {
            exception {
                packagePath shouldBe "java.lang"
                className shouldBe "NullPointerException"
            }

            message shouldBe null
        }
    }

    @Test
    fun `find two simple stack traces`() = checkFileForInputs("test_trace_2.txt") {
        size shouldBe 2

        fun RootStackTrace.check() {
            exception {
                packagePath shouldBe "java.lang"
                className shouldBe "NullPointerException"
            }

            message shouldBe null
        }

        forEach { it.check() }
    }

    @Test
    fun `find three simple stack traces`() = checkFileForInputs("test_trace_3.txt") {
        size shouldBe 3

        fun RootStackTrace.check() {
            exception {
                packagePath shouldBe "java.lang"
                className shouldBe "NullPointerException"
            }

            message shouldBe null
        }

        forEach { it.check() }
    }

    @Test
    fun `find one complex stack trace`() = checkFileForInputs("test_trace_4.txt") {
        size shouldBe 1

        val stackTrace = first()
        stackTrace {
            exception {
                packagePath shouldBe "org.bukkit.plugin"
                className shouldBe "InvalidPluginException"
            }

            message shouldBe "java.lang.UnsupportedClassVersionError: de/Konsti_x08/citie/main/Main has been compiled by a more recent version of the Java Runtime (class file version 59.0), this version of the Java Runtime only recognizes class file versions up to 52.0"
            children.size shouldBe 1

            val causedBy = children.first()

            causedBy {
                exception {
                    packagePath shouldBe "java.lang"
                    className shouldBe "UnsupportedClassVersionError"
                }

                message shouldBe "de/Konsti_x08/citie/main/Main has been compiled by a more recent version of the Java Runtime (class file version 59.0), this version of the Java Runtime only recognizes class file versions up to 52.0"
                children.size shouldBe 1
            }
        }
    }

    @Test
    fun `find three complex stack trace`() = checkFileForInputs("test_trace_5.txt") {
        size shouldBe 3

        fun RootStackTrace.check() {
            exception {
                packagePath shouldBe "org.bukkit.plugin"
                className shouldBe "InvalidPluginException"
            }

            message shouldBe "java.lang.UnsupportedClassVersionError: de/Konsti_x08/citie/main/Main has been compiled by a more recent version of the Java Runtime (class file version 59.0), this version of the Java Runtime only recognizes class file versions up to 52.0"
            children.size shouldBe 1

            val causedBy = children.first()

            causedBy {
                exception {
                    packagePath shouldBe "java.lang"
                    className shouldBe "UnsupportedClassVersionError"
                }

                message shouldBe "de/Konsti_x08/citie/main/Main has been compiled by a more recent version of the Java Runtime (class file version 59.0), this version of the Java Runtime only recognizes class file versions up to 52.0"
                children.size shouldBe 1
            }
        }

        forEach { it.check() }
    }
}