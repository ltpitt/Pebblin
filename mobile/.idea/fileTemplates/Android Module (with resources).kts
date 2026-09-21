plugins {
   androidLibraryModule
   di
}

android {
    namespace = "com.ltpitt.pebblin.${NAME}"

    buildFeatures {
        androidResources = true
    }
}

dependencies {
    testImplementation(projects.common.test)
}
