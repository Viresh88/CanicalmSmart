plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-android")
    id("com.google.devtools.ksp") version "1.9.10-1.0.13"

}

android {
    namespace = "com.example.assignmentshaaysoft"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.assignmentshaaysoft"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures{
        viewBinding = true
    }

}

dependencies {
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation ("androidx.room:room-runtime:2.6.1")
    implementation ("androidx.room:room-paging:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")



    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.5")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.5")

    ksp("androidx.room:room-compiler:2.6.1")

    implementation ("androidx.recyclerview:recyclerview:1.3.1")


    //Gson
    implementation ("com.google.code.gson:gson:2.10.1")

    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation ("io.reactivex.rxjava2:rxjava:2.2.21")

    //PERMISSION
    implementation ("pub.devrel:easypermissions:3.0.0")

    //Navigation
    //noinspection GradleDependency
    implementation ("androidx.navigation:navigation-fragment-ktx:2.6.0")
    //noinspection GradleDependency
    implementation ("androidx.navigation:navigation-ui-ktx:2.6.0")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")


        implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0") // Required for Transformations
        implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1") // Optional for Kotlin LiveData extensions




    //noinspection GradleDependency

    //noinspection GradleDependency
    implementation ("androidx.core:core-splashscreen:1.0.0-beta02")

    implementation ("androidx.vectordrawable:vectordrawable:1.1.0")

    //PREFERENCES
    implementation ("androidx.preference:preference-ktx:1.2.1")









    //COROUTINE
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    //WORKER
    implementation ("androidx.work:work-runtime-ktx:2.8.1")


    testImplementation ("androidx.room:room-testing:2.5.0")





}