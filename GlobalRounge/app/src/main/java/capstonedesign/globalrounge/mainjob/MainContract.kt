package capstonedesign.globalrounge.mainjob

import capstonedesign.globalrounge.Student

interface MainContract {
    interface View {
        fun alertToast(text:String)
        fun startActivity(student: Student)
    }

    interface Presenter {
        fun loginClicked(user:User)
        fun rejectPermission(text: String)
        fun approvalPermission(user: User)
        fun changeCheckState(isChecked:Boolean)
        fun checkAutoLogin() : Boolean
        fun deletePreference()
        fun dispose()
    }

    interface Model {
        fun requestSejongPermission(user:User)
        fun requestServerPermission(user: User)
        fun getUserInfo():User
        fun deleteUserInfo()
        fun saveUserInfo(user: User)
        var checkBoxState : Boolean
    }
}