package capstonedesign.globalrounge.MainJob

import android.content.Context

interface MainMVP {
    interface View {
        fun noInformation(text: String)
        fun rejectPermission(text: String)
        fun startActivity()
    }

    interface Presenter {
        fun init(view:View,context:Context)
        fun loginClicked(user:User)
        fun rejectPermission(text: String)
        fun approvalPermission(user: User)
        fun changeCheckState(isChecked:Boolean)
        fun checkAutoLogin() : Boolean
    }

    interface Model {
        fun requestPermission(user:User)
        fun saveUserInfo(user: User)
        fun getUserInfo():User
        fun deleteUserInfo()
        var checkBoxState : Boolean
    }
}