package capstonedesign.globalrounge.model.auto_login

import android.content.Context
import android.content.SharedPreferences
import capstonedesign.globalrounge.model.User

interface DataStore {
    fun setSharedPreferences(context: Context)
    fun getUserInfo(): User
    fun deleteUserInfo()
    fun saveUserInfo(user: User)
    var checkBoxState: Boolean
    var preferences : SharedPreferences
    var editor : SharedPreferences.Editor
}