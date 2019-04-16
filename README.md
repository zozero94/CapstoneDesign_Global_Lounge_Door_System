# CapstoneDesign
## Global_Rounge_Door_System
<hr/>  

## MVP 패턴을 적용한 안드로이드 프로젝트
+ Kotlin
+ RxJava
+ Retrofit2
+ Gson
+ coddestX
+ Zxing

## (Model)  
 
#### Permission
 + 학교 서버 우회접근[SejongPermission.kt](
 https://github.com/zojae031/CapstoneDesign_Global_Rounge_Door_System/blob/android/GlobalRounge/app/src/main/java/capstonedesign/globalrounge/model/permission/SejongConnection.kt) ``Retrofit2`` 
 + DB서버 접근[ServerPermission.kt](https://github.com/zojae031/CapstoneDesign_Global_Rounge_Door_System/blob/android/GlobalRounge/app/src/main/java/capstonedesign/globalrounge/model/permission/ServerConnection.kt)``Rx (CoddestX)``
 
 #### Util  
 + 자동로그인  [SharedData ](https://github.com/zojae031/CapstoneDesign_Global_Rounge_Door_System/blob/android/GlobalRounge/app/src/main/java/capstonedesign/globalrounge/model/SharedData.kt)``SharedPreference``
 + QRCode 생성  [QrCode ](https://github.com/zojae031/CapstoneDesign_Global_Rounge_Door_System/blob/android/GlobalRounge/app/src/main/java/capstonedesign/globalrounge/model/QrCode.kt) ``Zxing``
 + 암호화  [Encryption](https://github.com/zojae031/CapstoneDesign_Global_Rounge_Door_System/blob/android/GlobalRounge/app/src/main/java/capstonedesign/globalrounge/model/Encryption.kt) ``RSA / Base64``
<hr>


### 전체 안드로이드 클래스 다이어그램
![Diagram](./ClassDiagram/Androidclass.jpg)
