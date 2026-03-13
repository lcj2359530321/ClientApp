import com.google.protobuf.gradle.GenerateProtoTask

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    //kotlin序列化
    kotlin("plugin.serialization") version "1.9.23"

    //依赖注入
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)

    //protobuf插件
    alias(libs.plugins.protobuf)
}

android {
    namespace = libs.versions.namespace.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.namespace.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true

        //开启buildConfig
        buildConfig = true
    }
    composeOptions {
        //和kotlin版本对应关系
        //https://developer.android.com/jetpack/androidx/releases/compose-kotlin?hl=zh-cn
        kotlinCompilerExtensionVersion = "1.5.12"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    flavorDimensions += listOf("env", "channel")
    productFlavors {
        create("dev") {
            dimension = "env"

            //API端点
            buildConfigField("String", "ENDPOINT", "\"https://quick-server-sp.ixuea.com/\"")

            //资源端点
//            buildConfigField(
//                "String",
//                "RESOURCE_ENDPOINT",
//                "\"https://com-quick-project.oss-cn-beijing.aliyuncs.com/%s\""
//            )

            //新资源地址
            buildConfigField(
                "String",
                "RESOURCE_ENDPOINT",
                "\"https://rs.ixuea.com/quick/%s\""
            )

        }
        create("local") {
            dimension = "env"

            //API端点
            buildConfigField("String", "ENDPOINT", "\"http://192.168.2.98:8080/\"")

            //如果模拟器局域网ip无法访问，用这个试试
//            buildConfigField("String", "ENDPOINT", "\"http://10.0.2.2:8080/\"")

            //资源端点
            buildConfigField(
                "String",
                "RESOURCE_ENDPOINT",
                "\"https://rs.ixuea.com/quick/%s\""
            )
        }
        create("prod") {
            dimension = "env"

            //API端点
            buildConfigField("String", "ENDPOINT", "\"https://quick-server-sp-prod.ixuea.com/\"")

            //资源端点
            buildConfigField(
                "String",
                "RESOURCE_ENDPOINT",
                "\"https://rs.ixuea.com/quick/%s\""
            )
        }
        create("default") {
            dimension = "channel"
            versionNameSuffix = "-default"
        }
        create("play") {
            dimension = "channel"
            versionNameSuffix = "-play"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material.iconsExtended)

    implementation(libs.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //kotlin序列化
    //https://kotlinlang.org/docs/serialization.html
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    //region 网络框架
    //https://github.com/square/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    //网络框架日志框架
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    //类型安全网络框架
    //https://github.com/square/retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    //让Retrofit支持Kotlinx Serialization
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    //endregion

    //图片加载框架
    //https://github.com/coil-kt/coil
    implementation("io.coil-kt:coil-compose:2.6.0")

    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)

    //region 依赖注入
    //https://developer.android.google.cn/training/dependency-injection/hilt-android?hl=zh-cn
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kspAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    //endregion

    compileOnly(libs.ksp.gradlePlugin)

    val androidx_media3_version = "1.2.1"
    implementation("androidx.media3:media3-exoplayer:$androidx_media3_version")
    implementation("androidx.media3:media3-datasource:$androidx_media3_version")
    implementation("androidx.media3:media3-ui:$androidx_media3_version")
    implementation("androidx.media3:media3-session:$androidx_media3_version")
    implementation("androidx.media3:media3-cast:$androidx_media3_version")

    //browser?.getChildren()?.await()
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:1.7.1")

    //约束布局
    //https://developer.android.com/develop/ui/compose/layouts/constraintlayout?hl=zh-cn
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    //自定义toast提示
    //https://github.com/tfaki/ComposableSweetToast
    implementation("com.github.tfaki:ComposableSweetToast:1.0.1")

    //数据存储
    //https://developer.android.google.cn/topic/libraries/architecture/datastore?hl=zh-cn#prefs-vs-proto
    implementation("androidx.datastore:datastore:1.0.0")

    //Compose开发AppWidget
    //https://developer.android.com/develop/ui/compose/glance/setup?hl=zh-cn
    implementation("androidx.glance:glance-appwidget:1.1.0-beta01")
    implementation("androidx.glance:glance-material3:1.1.0-beta01")

    implementation(libs.protobuf.kotlin.lite)

    //region room数据库
    //https://developer.android.com/training/data-storage/room?hl=zh-cn

    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    //annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
//    kapt("androidx.room:room-compiler:$room_version")
    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$room_version")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // optional - RxJava2 support for Room
//    implementation("androidx.room:room-rxjava2:$room_version")

    // optional - RxJava3 support for Room
//    implementation("androidx.room:room-rxjava3:$room_version")

    // optional - Guava support for Room, including Optional and ListenableFuture
//    implementation("androidx.room:room-guava:$room_version")

    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$room_version")

    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:$room_version")
    //endregion

    //富文本框架依赖
    implementation("org.jsoup:jsoup:1.17.2")

    //Hutool是一个小而全的Java工具类库
    // 通过静态方法封装，降低相关API的学习成本
    // 提高工作效率，使Java拥有函数式语言般的优雅
    //https://github.com/looly/hutool
    implementation("cn.hutool:hutool-all:5.8.18")

    //通用IO相关工具类
    //http://commons.apache.org/proper/commons-io/
    implementation("commons-io:commons-io:2.0")

    //日志框架
    //https://github.com/JakeWharton/timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation(libs.androidx.palette.ktx)

    //region emo框架
    //提供很多功能,例如:图片选择,预览,网络状态监听等
    //https://github.com/cgspine/emo-public
    implementation(platform("cn.qhplus.emo:bom:2023.08.00"))

    // 默认使用 coil 作为图片加载器
    implementation("cn.qhplus.emo:photo-coil")
    // 可选：如果需要使用其它库，则引入 photo 库，自定义实现 PhotoProvider 即可
    implementation("cn.qhplus.emo:photo")
    //endregion

    //更方便的日期时间，运算，解析格式化框架
    //https://www.joda.org/joda-time/index.html
    implementation("joda-time:joda-time:2.12.2")

    //动画框架
    //https://airbnb.io/lottie/#/android-compose
    implementation("com.airbnb.android:lottie-compose:6.3.0")

    //Compose开发AppWidget
    //https://developer.android.com/develop/ui/compose/glance/setup?hl=zh-cn
    implementation("androidx.glance:glance-appwidget:1.1.0-beta01")
    implementation("androidx.glance:glance-material3:1.1.0-beta01")
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

//https://github.com/google/dagger/issues/4097#issuecomment-1763781846
androidComponents {
    onVariants(selector().all()) { variant ->
        afterEvaluate {
            val protoTask =
                project.tasks.getByName("generate" + variant.name.replaceFirstChar { it.uppercaseChar() } + "Proto") as GenerateProtoTask

            project.tasks.getByName("ksp" + variant.name.replaceFirstChar { it.uppercaseChar() } + "Kotlin") {
                dependsOn(protoTask)
                (this as org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompileTool<*>).setSource(
                    protoTask.outputBaseDir
                )
            }
        }
    }
}
