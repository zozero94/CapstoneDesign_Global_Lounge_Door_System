package capstonedesign.globalrounge.mainjob.model

import capstonedesign.globalrounge.mainjob.User

interface DateSource{
    fun requestPermission(user: User)
    fun getUserInfo(): User
    fun deleteUserInfo()
    fun saveUserInfo(user: User)
    var checkBoxState : Boolean

}