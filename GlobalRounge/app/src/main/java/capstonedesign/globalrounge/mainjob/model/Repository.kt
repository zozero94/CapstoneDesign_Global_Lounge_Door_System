package capstonedesign.globalrounge.mainjob.model

import capstonedesign.globalrounge.mainjob.User

object Repository : DateSource{

//    private val sejongPermission :SejongPermission
    private val serverPermission : ServerPermission
    init {
//        sejongPermission= SejongPermission()
        serverPermission = ServerPermission
    }
    override fun requestPermission(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserInfo(): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteUserInfo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveUserInfo(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override var checkBoxState: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
}