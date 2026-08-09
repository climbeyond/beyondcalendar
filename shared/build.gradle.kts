import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    `maven-publish`

    alias(libs.plugins.kotlinMultiplatform)
    id("com.android.kotlin.multiplatform.library")

    alias(libs.plugins.serialization)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvmToolchain(21)

    android {
        namespace = "app.climbeyond.beyondlogin"
        compileSdk = rootProject.ext.get("androidCompileSdk") as Int
        minSdk = rootProject.extra.get("androidMinSdk") as Int
    }

    val xcf = XCFramework("BeyondCalendar")
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "BeyondCalendar"
            xcf.add(this)
            isStatic = true
            binaryOption("bundleId","app.climbeyond.beyondcalendar.ios")
        }
    }

    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
                optIn("kotlin.time.ExperimentalTime")
            }
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlin.serialization)
            implementation(libs.kotlinx.serialization.json)
        }
    }

    targets.all {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }
}

compose.resources {
    publicResClass = true
}

publishing {
    val GITHUB_USER: String? by project
    val GITHUB_TOKEN: String? by project

    repositories {
        version = rootProject.ext.get("versionName") as String
        maven {
            setUrl("https://maven.pkg.github.com/climbeyond/beyondcalendar")
            credentials {
                username = GITHUB_USER
                password = GITHUB_TOKEN
            }
        }
    }
}

tasks.register("testClasses") {
    doLast {
        println("This is a dummy testClasses task")
    }
}

afterEvaluate {
    tasks.getByName("testClasses") {
        onlyIf { return@onlyIf false }
    }
    tasks.getByName("linkDebugFrameworkIosArm64") {
        onlyIf { return@onlyIf false }
    }
    tasks.getByName("linkDebugFrameworkIosSimulatorArm64") {
        onlyIf { return@onlyIf false }
    }
}

tasks.register<Copy>("publish-android") {
    val androidName = "beyondcalendar-${rootProject.extra.get("versionName") as String}.aar"
    val apkDir = file("${project.rootDir.absolutePath}/shared/build/outputs/aar/beyondcalendar-release.aar")
    val outDir = file("${project.rootDir.absolutePath}/aar")

    from(apkDir)
    into(outDir)
    include("**/*")
    rename("beyondcalendar-release.aar", androidName)
    doLast {
        println(">>>publish $androidName success!" +
                "\nfrom: $apkDir" +
                "\ninto: $outDir")
    }
}

tasks.register<Copy>("publish-ios") {
    // iOS xcFramework copy
    val xcSaveDir = "beyondcalendar-${rootProject.extra.get("versionName") as String}.xcframework"
    val xcDir = file("${project.rootDir.absolutePath}/shared/build/XCFrameworks/release/BeyondCalendar.xcframework")
    val outXcDir = file("${project.rootDir.absolutePath}/xcframework/${xcSaveDir}")

    from(xcDir)
    into(outXcDir)
    include("**/*")
    doLast {
        println(">>>publish $xcSaveDir success!" +
                "\nfrom: $xcDir" +
                "\ninto: $outXcDir")
    }
}
