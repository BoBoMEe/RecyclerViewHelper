- edit IConstant
APP_ID ,TENCENT_APP_ID ...
- edit manifest
``` xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.yaodu.drug">
    //////////
<intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:scheme="tencent1104445688" />
                <!--官方测试用的appid222222-->
            </intent-filter>
```
- copy app.keystore to app module
- edit build.gradle 

``` java
defaultConfig {
        applicationId "com.yaodu.drug"
        minSdkVersion 15
        targetSdkVersion 23
        versionCode 1
        versionName "1.0"
    }
    
    ////////
    
    //签名
    signingConfigs {
        debug {
        }
        relealse {
//            storeFile file('***.keystore')
//            storePassword '***'
//            keyAlias '***'
//            keyPassword '***'
        }
    }
    buildTypes {
        debug {
            minifyEnabled false
            zipAlignEnabled false
            shrinkResources false
//            signingConfig signingConfigs.relealse
        }
    }
```
