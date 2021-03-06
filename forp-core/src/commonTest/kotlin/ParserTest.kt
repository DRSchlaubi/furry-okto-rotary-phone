import dev.schlaubi.forp.core.stacktrace.ParsedDefaultQualifiedMethod
import dev.schlaubi.forp.core.stacktrace.ParsedDefaultStackTraceElement
import dev.schlaubi.forp.core.stacktrace.ParsedEllipsisStackTraceElement
import kotlin.js.JsName
import kotlin.test.Test

class ParserTest {

    @Test
    @JsName("testComplexException")
    fun `test complex exception`() = checkFile(trace1) {
        exception {
            packagePath shouldBe "org.bukkit.plugin"
            className shouldBe "InvalidPluginException"
        }

        message shouldBe """Cannot find main class `de.near.trollplugin.Troll'"""

        val caused = children.first()
        caused {
            exception {
                packagePath shouldBe "java.lang"
                className shouldBe "ClassNotFoundException"
            }

            message shouldBe """de.near.trollplugin.Troll"""

            val last = elements.last() as ParsedEllipsisStackTraceElement
            last {
                skipped shouldBe 7
            }
        }
    }

    @Test
    @JsName("testSimpleException")
    fun `test simple exception`() = checkFile(trace2) {
        exception {
            packagePath shouldBe "java.lang"
            className shouldBe "NullPointerException"
        }

        message shouldBe """Cannot invoke "java.lang.String.toString()" because "x" is null"""

        val firstElement = elements.first()
        firstElement {
            require(this is ParsedDefaultStackTraceElement)
            method {
                clazz {
                    className shouldBe "Test"
                }

                require(this is ParsedDefaultQualifiedMethod)
                methodName shouldBe "main"
            }

            source {
                require(this is ParsedDefaultStackTraceElement.ParsedFileSource)
                fileName shouldBe "Test.java"
                lineNumber shouldBe 5
            }
        }
    }

    @Test
    @JsName("testExceptionWithCause")
    fun `test exception with cause`() = checkFile(trace3) {
        exception {
            packagePath shouldBe "dev.schlaubi"
            className shouldBe "DiedException"
        }

        message shouldBe """An Exception was thrown"""

        val firstElement = elements.first()
        firstElement {
            require(this is ParsedDefaultStackTraceElement)
            method {
                clazz {
                    className shouldBe "Test"
                }

                require(this is ParsedDefaultQualifiedMethod)
                methodName shouldBe "main"
            }

            source {
                require(this is ParsedDefaultStackTraceElement.ParsedFileSource)
                fileName shouldBe "Test.java"
                lineNumber shouldBe 5
            }
        }

        val cause = children.first()
        cause {
            exception {
                packagePath shouldBe "java.lang"
                className shouldBe "NullPointerException"
            }

            message shouldBe """Cannot invoke "java.lang.String.toString()" because "x" is null"""

            val firstChildElement = elements.first()
            firstChildElement {
                require(this is ParsedDefaultStackTraceElement)
                method {
                    clazz {
                        className shouldBe "Test"
                    }

                    require(this is ParsedDefaultQualifiedMethod)
                    methodName shouldBe "main"
                }

                source {
                    require(this is ParsedDefaultStackTraceElement.ParsedFileSource)
                    fileName shouldBe "Test.java"
                    lineNumber shouldBe 5
                }
            }
        }
    }
}
