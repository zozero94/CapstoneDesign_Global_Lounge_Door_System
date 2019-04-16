package capstonedesign.globalrounge.mainjob

import capstonedesign.globalrounge.dto.Student
import capstonedesign.globalrounge.dto.User

interface MainContract {
    interface View {
        fun alertToast(text: String)
        fun startActivity(student: Student)
    }

    interface Presenter {
        fun loginClicked(user: User)
        fun changeCheckState(isChecked: Boolean)
        fun checkAutoLogin(): Boolean
        fun deletePreference()
        fun dispose()
    }

}