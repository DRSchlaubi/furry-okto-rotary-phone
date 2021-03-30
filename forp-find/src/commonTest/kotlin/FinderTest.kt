import dev.schlaubi.forp.core.stacktrace.ParsedRootStackTrace
import kotlin.js.JsName
import kotlin.test.Test

class  FinderTest {

    @Test
    @JsName("findOneSimpleStackTrace")
    fun `find one simple stack trace`() = checkFileForInputs(trace1) {
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
    @JsName("findTwoSimpleStackTraces")
    fun `find two simple stack traces`() = checkFileForInputs(trace2) {
        size shouldBe 2

        fun ParsedRootStackTrace.check() {
            exception {
                packagePath shouldBe "java.lang"
                className shouldBe "NullPointerException"
            }

            message shouldBe null
        }

        forEach { it.check() }
    }

    @Test
    @JsName("findThreeSimpleStackTraces")
    fun `find three simple stack traces`() = checkFileForInputs(trace3) {
        size shouldBe 3

        fun ParsedRootStackTrace.check() {
            exception {
                packagePath shouldBe "java.lang"
                className shouldBe "NullPointerException"
            }

            message shouldBe null
        }

        forEach { it.check() }
    }

    @Test
    @JsName("findOneComplexStackTrace")
    fun `find one complex stack trace`() = checkFileForInputs(trace4) {
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
    @JsName("findThreeComplexStackTrace")
    fun `find three complex stack trace`() = checkFileForInputs(trace5) {
        size shouldBe 3

        fun ParsedRootStackTrace.check() {
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
