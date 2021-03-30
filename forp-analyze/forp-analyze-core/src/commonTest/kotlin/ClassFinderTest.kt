import kotlin.js.JsName
import kotlin.test.Test

class ClassFinderTest {

    @JsName("testSimpleClasses")
    @Test
    fun `test simple classes`() = findClasses(test1) {
        forEach {
            it.name shouldBe "Clu"
            it.potentiallyIncomplete shouldBe true
        }
    }

    @JsName("testOneComplexClass")
    @Test
    fun `test one complex class`() = findClasses(test2) {
        size shouldBe 1

        val element = first()
        element {
            name shouldBe "TestModule"
            potentiallyIncomplete shouldBe true
        }
    }

    @JsName("testOneCompleteClass")
    @Test
    fun `test one complete class`() = findClasses(test3) {
        size shouldBe 1

        val element = first()
        println(element)
        element {
            name shouldBe "Test"
            potentiallyIncomplete shouldBe false
        }
    }
}
