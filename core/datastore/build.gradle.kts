plugins {
    id("newquiz.android.library")
//    alias(libs.plugins.newquiz.android.library)
    alias(libs.plugins.newquiz.android.hilt)
    id("newquiz.detekt")
}

android {
    namespace = "com.infinitepower.newquiz.core.datastore"
}

dependencies {
    api(libs.androidx.dataStore.preferences)

    implementation(projects.model)
    implementation(projects.core)
}
