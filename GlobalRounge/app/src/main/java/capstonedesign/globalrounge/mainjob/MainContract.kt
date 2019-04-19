package capstonedesign.globalrounge.mainjob

import capstonedesign.globalrounge.dto.Student
import capstonedesign.globalrounge.dto.User

interface MainContract {
    interface View {
        fun alertToast(text: String)
        fun startActivity(student: Student)
        fun updateUserInfo(user: User)
    }

    interface Presenter {
        fun loginClicked(user: User)
        fun changeCheckState(isChecked: Boolean)
        fun checkAutoLogin(): Boolean
        fun deletePreference()
        fun dispose()
    }

}