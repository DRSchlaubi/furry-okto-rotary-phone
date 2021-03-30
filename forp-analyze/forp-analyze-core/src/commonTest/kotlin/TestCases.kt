val test1 = """
            package x.c.z;


            public class Clu {
            	// Text in class1
            }

            package x.c.z;


            public class Clu {
            	// Text in class2
            }
        """.trimIndent()

val test2 = """
    Moin ich arbeite zur Zeit mit Google Guice. Kennt jemand evtl. eine möglichkeit wie ich bestimmte daten erst beim erstellen der Instanz übergeben kann? 

Sprich ich habe so ein Module: 
package a.b.c;

public class TestModule extends AbstractModule {

    private String test;

    public TestModule(String test) {
        this.test = test;
    }

    @Override
    protected void configure() {
        bind(String.class).toInstance(test);
    }
}
 möchte aber das der String test erste beim erstellen der Instanz einer Klasse übergeben wird, sodass nicht einen Haufen von injectoren erstellen muss. Villeicht kennt ja jemand ne möglichkeit dazu. (Pls ping bei antwort)
""".trimIndent()

val test3 = """
package a.b.c;

import java.util.List;

public class Test {

    public void stringThing() {
        var list = List.of();
    }
}
""".trimIndent()
