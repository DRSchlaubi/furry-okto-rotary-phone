import org.gradle.api.NamedDomainObjectProvider
import org.gradle.kotlin.dsl.named
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

val org.gradle.api.NamedDomainObjectContainer<KotlinSourceSet>.jvmMain: NamedDomainObjectProvider<KotlinSourceSet>
    get() = named<KotlinSourceSet>("jvmMain")

val org.gradle.api.NamedDomainObjectContainer<KotlinSourceSet>.jvmTest: NamedDomainObjectProvider<KotlinSourceSet>
    get() = named<KotlinSourceSet>("jvmTest")

val org.gradle.api.NamedDomainObjectContainer<KotlinSourceSet>.jsMain: NamedDomainObjectProvider<KotlinSourceSet>
    get() = named<KotlinSourceSet>("jsMain")

val org.gradle.api.NamedDomainObjectContainer<KotlinSourceSet>.jsTest: NamedDomainObjectProvider<KotlinSourceSet>
    get() = named<KotlinSourceSet>("jsTest")
