plugins {
    kotlin("jvm") version "1.4.30"
    id("idea")
    java
    jacoco
    id("application")
    id("com.github.johnrengelman.shadow") version "6.0.0" // used to build a fat jar
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
}

group = "com.idiomcentric"

application {
    mainClassName = "com.idiomcentric.AppKt"
}

repositories {
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

object V {
    const val guice = "4.2.3"
    const val guava = "29.0-jre"
    const val kotlin = "1.4.21"
    const val ktor = "1.4.1"
    const val jackson = "2.10.4"
    const val exposed = "0.25.1"
    const val hikaricp = "3.4.5"
    const val postgres = "42.2.2"

    const val kotlinLogging = "1.7.8"
    const val logback = "0.1.5"
    const val logbackClassic = "1.2.3"

    const val junit = "5.4.0"
    const val kotest = "4.3.0"
    const val h2 = "1.4.199"
}

dependencies {
    implementation("io.github.microutils:kotlin-logging:${V.kotlinLogging}")
    implementation("ch.qos.logback:logback-classic:${V.logbackClassic}")
    implementation("ch.qos.logback.contrib:logback-json-classic:${V.logback}")
    implementation("ch.qos.logback.contrib:logback-jackson:${V.logback}")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:${V.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${V.kotlin}")

    implementation("com.zaxxer:HikariCP:${V.hikaricp}")
    implementation("org.postgresql:postgresql:${V.postgres}")

    implementation("org.jetbrains.exposed:exposed-core:${V.exposed}")
    implementation("org.jetbrains.exposed:exposed-dao:${V.exposed}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${V.exposed}")
    implementation("org.jetbrains.exposed:exposed-java-time:${V.exposed}")

    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:${V.jackson}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${V.jackson}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${V.jackson}")

    implementation("io.ktor:ktor-server-cio:${V.ktor}")
    implementation("io.ktor:ktor-locations:${V.ktor}")
    implementation("io.ktor:ktor-jackson:${V.ktor}")

    implementation("com.google.inject:guice:${V.guice}")

    testImplementation("io.ktor:ktor-server-test-host:${V.ktor}")
    testImplementation("org.junit.jupiter:junit-jupiter:${V.junit}")
    testImplementation("io.kotest:kotest-assertions-core:${V.kotest}")

    testImplementation("com.h2database:h2:${V.h2}")
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
            showExceptions = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            showCauses = true
            showStackTraces = true
        }
        finalizedBy(jacocoTestReport)
    }

    jacocoTestReport {
        enabled = true
        dependsOn(test)
    }
    shadowJar { isZip64 = true }
    distZip { enabled = false }
    distTar { enabled = false }
    shadowDistZip { enabled = false }
    shadowDistTar { enabled = false }

    register<JavaExec>("hydrate-database") {
        main = "com.idiomcentric.setup.HydrateDatabaseKt"
        classpath = sourceSets["main"].runtimeClasspath
    }
}
