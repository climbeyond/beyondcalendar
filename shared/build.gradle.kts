import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    `maven-publish`

    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)

    alias(libs.plugins.serialization)
    alias(libs.plugins.compose)
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        publishLibraryVariantsGroupedByFlavor = true
    }

    val xcf = XCFramework("BeyondCalendar")
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "BeyondCalendar"
            xcf.add(this)
            isStatic = true
        }
    }

    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlin.serialization)
            implementation(libs.kotlinx.serialization.json)
        }
    }

    targets.all {
        compilations.all {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }
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

android {
    namespace = "app.climbeyond.beyondcalendar"
    compileSdk = rootProject.ext.get("androidCompileSdk") as Int

    val versionName = rootProject.ext.get("versionName") as String

    androidComponents.beforeVariants {
        it.enable = run {
            it.buildType != "debug"
        }
    }

    defaultConfig {
        minSdk = rootProject.extra.get("androidMinSdk") as Int

        buildConfigField("String", "BUILD_TIME", "\"${SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                .format(Date())}\"")
        buildConfigField("String", "VERSION_NAME", "\"${versionName}\"")
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packaging {
        resources {
            excludes += "/values/strings.xml"
            excludes += "/drawable/*"
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

task("testClasses").doLast {
    println("This is a dummy testClasses task")
}

afterEvaluate {
    tasks.getByName("testClasses") {
        onlyIf { return@onlyIf false }
    }
    tasks.getByName("linkDebugFrameworkIosX64") {
        onlyIf { return@onlyIf false }
    }
    tasks.getByName("linkDebugFrameworkIosArm64") {
        onlyIf { return@onlyIf false }
    }
    tasks.getByName("linkDebugFrameworkIosSimulatorArm64") {
        onlyIf { return@onlyIf false }
    }
}

tasks.create<Copy>("publish-android") {
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

tasks.create<Copy>("publish-ios") {
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
