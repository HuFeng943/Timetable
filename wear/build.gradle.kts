import com.android.build.api.dsl.ApplicationExtension

val versionPrefix = "0.6.7"


fun getGitCommitCount(): Int {
    return try {
        val process = ProcessBuilder("git", "rev-list", "--count", "HEAD")
            .redirectErrorStream(true).start()
        val output = process.inputStream.bufferedReader().readText().trim()
        process.waitFor()
        if (process.exitValue() != 0) 1 else output.toInt()
    } catch (_: Exception) {
        1
    }
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.aboutLibraries)
}

configure<ApplicationExtension> {
    namespace = "com.hufeng943.timetable"
    compileSdk = 36

    val isRelease = gradle.startParameter.taskNames.any {
        it.contains("Release", ignoreCase = true)
    }

    defaultConfig {
        applicationId = "com.hufeng943.timetable"
        minSdk = 28
        targetSdk = 36
        versionCode = if (isRelease) getGitCommitCount() else 1
        versionName =
            if (isRelease) "$versionPrefix-${getGitCommitCount()}" else "$versionPrefix-debug"
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("armeabi-v7a", "arm64-v8a")

            isUniversalApk = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    useLibrary("wear-sdk")
    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
        freeCompilerArgs.add("-Xannotation-default-target=param-property")
    }
}

dependencies {
    // 核心基础与 AndroidX
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.profileinstaller)
    implementation(project(":shared"))

    // Compose 基础体系
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material.icons.core)

    // Wear OS 核心与 Compose Material 3
    implementation(libs.play.services.wearable)
    implementation(libs.androidx.wear.compose.material3)
    implementation(libs.androidx.wear.compose.foundation)
    implementation(libs.androidx.wear.compose.navigation)
    implementation(libs.androidx.wear.tooling.preview)
    implementation(libs.androidx.wear.remote)

    // Wear Tiles 与 Complications (Horologist)
    implementation(libs.androidx.tiles)
    implementation(libs.androidx.tiles.tooling.preview)
    implementation(libs.androidx.watchface.complications.data.source.ktx)
    implementation(libs.horologist.compose.tools)
    implementation(libs.horologist.tiles)

    // 依赖注入 (Hilt)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // 数据存储 (Room & DataStore)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.androidx.datastore.preferences)

    // KotlinX 扩展与辅助工具
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.aboutlibraries.compose.m3)
}