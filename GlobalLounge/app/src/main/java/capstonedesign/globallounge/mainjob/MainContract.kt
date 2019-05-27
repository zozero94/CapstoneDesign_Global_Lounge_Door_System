package capstonedesign.globallounge.mainjob

import capstonedesign.globallounge.dto.Student
import capstonedesign.globallounge.dto.User

interface MainContract {
    interface View {
        fun alertToast(text: String)
        fun startActivity(student: Student)
        fun updateUserInfo(user: User)
        fun loadingStart()
        fun loadingDestroy()
    }

    interface Presenter {
        fun loginClicked(user: User)
        fun changeCheckState(isChecked: Boolean)
        fun checkAutoLogin(): Boolean
        fun deletePreference()
        fun dispose()
    }

}