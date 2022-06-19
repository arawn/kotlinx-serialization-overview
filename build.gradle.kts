plugins {
    kotlin("multiplatform") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"

    id("io.kotest.multiplatform") version "5.3.1"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()

            testLogging {
                events("passed", "skipped", "failed")

                showCauses = true
                showExceptions = true
                showStackTraces = true
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL

                showStandardStreams = false
            }
        }
    }

    js(IR) {
        binaries.executable()
        compilations.all {
            kotlinOptions {
                freeCompilerArgs = freeCompilerArgs.plus("-opt-in=kotlin.js.ExperimentalJsExport")
                sourceMap = true
            }
        }
        browser {
            commonWebpackConfig {
                cssSupport.enabled = false
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }

    val hostOs = System.getProperty("os.name")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        hostOs.startsWith("Windows") -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.3.3")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.3.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))

                implementation("io.kotest:kotest-framework-engine:5.3.1")
                implementation("io.kotest:kotest-assertions-core:5.3.1")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
                implementation("com.google.code.gson:gson:2.9.0")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("io.kotest:kotest-runner-junit5:5.3.1")
            }
        }
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}
