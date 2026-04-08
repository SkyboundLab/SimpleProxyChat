import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.withType

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":projects:proxy:shared"))
    implementation(project(":projects:common"))
    implementation(project(":projects:server:shared"))

    implementation("com.github.unilock:yeplib:2.3.1")

    compileOnly("com.velocitypowered", "velocity-api", "3.4.0-SNAPSHOT")
    testImplementation("com.velocitypowered", "velocity-api", "3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered", "velocity-api", "3.4.0-SNAPSHOT")
}

tasks.withType<ShadowJar> {
    relocate("org.bstats", "com.beanbeanjuice.simpleproxychat.libs.org.bstats")
    relocate("cc.unilock.yep", "com.beanbeanjuice.simpleproxychat.libs.yep")
    mergeServiceFiles()
}
