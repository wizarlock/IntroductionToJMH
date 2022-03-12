plugins {
    kotlin("jvm") version "1.6.10"
    id ("me.champeau.jmh") version "0.6.6"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.openjdk.jmh:jmh-core:1.29")
    implementation("org.openjdk.jmh:jmh-generator-annprocess:1.29")
}
