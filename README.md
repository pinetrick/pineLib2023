How to integration

1. check Project: build.gradle

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' } # Please add this line
    }
}


2. Check Module: build.gradle

dependencies {
   implementation 'com.github.pinetrick:pineLib2023:0.0.24'
}

3. Finished extend, may need do one of the following things
   1. make you all `Activity` extended from `PineAppCompatActivity` 
   2. make you all `Activity` extended from `PineComponentActivity` 
   3. or call `StaticPineActivity` functions, based on example of upper class
   
4. Optional, extend you application from PineApplication 
