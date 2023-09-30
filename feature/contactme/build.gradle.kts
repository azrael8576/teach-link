plugins {
    id("at.android.feature")
    id("at.android.library.compose")
    id("at.android.hilt")
    alias(libs.plugins.navigation.safeargs.kotlin)
}

android {
    namespace = "com.wei.amazingtalker_recruit.feature.contactme"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    androidTestImplementation(project(":core:designsystem"))
    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))

    // PublicLibs
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.tracing.ktx)
    coreLibraryDesugaring(libs.android.desugarJdkLibs)

    // LifeCycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    // Navigation
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Timber
    implementation(libs.timber)
}