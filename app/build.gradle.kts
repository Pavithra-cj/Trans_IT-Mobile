plugins {
    id("com.android.application")
}

android {
    namespace = "lk.nibm.furious5.scorpio.transit"
    compileSdk = 33

    defaultConfig {
        applicationId = "lk.nibm.furious5.scorpio.transit"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Scalable Size Unit (support for different screen size)

    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation("com.intuit.ssp:ssp-android:1.0.6")

    //Rounded Image View
    implementation("com.makeramen:roundedimageview:2.3.0")

    //MultiDex
    implementation("androidx.multidex:multidex:2.0.1")

    implementation("io.github.chaosleung:pinview:1.4.4")

    // adding volley dependency
    implementation("com.android.volley:volley:1.2.1")

    //QR code
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.journeyapps:zxing-android-embedded:4.2.0")

    //location
    implementation("com.google.android.gms:play-services-location:21.0.1")

    //RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    //CardView
    implementation("androidx.cardview:cardview:1.0.0")

    //Picasso
    implementation("com.squareup.picasso:picasso:2.5.2")
}