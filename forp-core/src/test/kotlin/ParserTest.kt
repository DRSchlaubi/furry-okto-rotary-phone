import dev.schlaubi.forp.core.stacktrace.DefaultQualifiedMethod
import dev.schlaubi.forp.core.stacktrace.DefaultStackTraceElement
import dev.schlaubi.forp.core.stacktrace.EllipsisStackTraceElement
import kotlin.test.Test

class ParserTest {

    @Test
    fun `test complex exception`() = checkFile("test_trace_1.txt") {
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

            val last = elements.last() as EllipsisStackTraceElement
            last {
                skipped shouldBe 7
            }
        }
    }

    @Test
    fun `test simple exception`() = checkFile("test_trace_2.txt") {
        exception {
            packagePath shouldBe "java.lang"
            className shouldBe "NullPointerException"
        }

        message shouldBe """Cannot invoke "java.lang.String.toString()" because "x" is null"""

        val firstElement = elements.first()
        firstElement {
            require(this is DefaultStackTraceElement)
            method {
                clazz {
                    className shouldBe "Test"
                }

                require(this is DefaultQualifiedMethod)
                methodName shouldBe "main"
            }

            source {
                require(this is DefaultStackTraceElement.FileSource)
                fileName shouldBe "Test.java"
                lineNumber shouldBe 5
            }
        }
    }

    @Test
    fun `test exception with cause`() = checkFile("test_trace_3.txt") {
        exception {
            packagePath shouldBe "dev.schlaubi"
            className shouldBe "DiedException"
        }

        message shouldBe """An Exception was thrown"""

        val firstElement = elements.first()
        firstElement {
            require(this is DefaultStackTraceElement)
            method {
                clazz {
                    className shouldBe "Test"
                }

                require(this is DefaultQualifiedMethod)
                methodName shouldBe "main"
            }

            source {
                require(this is DefaultStackTraceElement.FileSource)
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
                require(this is DefaultStackTraceElement)
                method {
                    clazz {
                        className shouldBe "Test"
                    }

                    require(this is DefaultQualifiedMethod)
                    methodName shouldBe "main"
                }

                source {
                    require(this is DefaultStackTraceElement.FileSource)
                    fileName shouldBe "Test.java"
                    lineNumber shouldBe 5
                }
            }
        }
    }
}
