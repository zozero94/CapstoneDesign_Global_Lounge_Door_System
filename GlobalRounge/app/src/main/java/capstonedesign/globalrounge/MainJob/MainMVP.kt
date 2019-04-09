package capstonedesign.globalrounge.MainJob

import android.content.Context

interface MainMVP {
    interface View {
        fun alertToast(text:String)
        fun startActivity(user: User)
    }

    interface Presenter {
        fun init(view:View,context:Context)
        fun loginClicked(user:User)
        fun rejectPermission(text: String)
        fun approvalPermission(user: User)
        fun changeCheckState(isChecked:Boolean)
        fun checkAutoLogin() : Boolean
        fun logout()
    }

    interface Model {
        fun requestSejongPermission(user:User)
        fun requestServerPermission(user:User)
        fun getUserInfo():User
        fun deleteUserInfo()
        var checkBoxState : Boolean
    }
}