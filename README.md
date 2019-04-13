# CapstoneDesign
## Global_Rounge_Door_System
<hr/>  

## (View)  

### MainActivity  
 + 기존 세종대학교 모바일 이용증의 UI와 유사하게 구현
 - 적용 된 기술 :  ``DataBinding ``

</hr>  

## (Presenter)  

### MainPresenter  
 + 비즈니스 로직
 </hr>  
 
 ## (Model)  
 
### MainModel
 + 학교 서버 우회접근[SejongPermission.kt](
 https://github.com/zojae031/CapstoneDesign_Global_Rounge_Door_System/blob/android/GlobalRounge/app/src/main/java/capstonedesign/globalrounge/MainJob/Model/SejongPermission.kt) ``Retrofit2`` 
 + DB서버 접근[ServerPermission.kt](https://github.com/zojae031/CapstoneDesign_Global_Rounge_Door_System/blob/android/GlobalRounge/app/src/main/java/capstonedesign/globalrounge/MainJob/Model/ServerPermission.kt)``Rx (CoddestX)``
 + 자동로그인 ``SharedPreference``
<hr>

