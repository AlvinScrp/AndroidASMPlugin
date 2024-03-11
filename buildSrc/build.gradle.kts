
plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.6.21"
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation("com.android.tools.build:gradle:7.4.2")
//    implementation("commons-io:commons-io:2.6")
//    compileOnly("commons-codec:commons-codec:1.15")
    compileOnly("org.ow2.asm:asm-commons:9.2")
    compileOnly("org.ow2.asm:asm-tree:9.2")
    compileOnly("com.google.code.gson:gson:2.8.2")
//    compileOnly("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.15.0")
}

sourceSets.main {
    java.srcDirs("../plugin/src/main/java")
}
//
//sourceSets{
//    main{
//        java.srcDirs.add(file("../plugin/src/main/java"))
//    }
//}