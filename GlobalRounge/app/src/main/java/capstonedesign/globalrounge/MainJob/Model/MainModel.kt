package capstonedesign.globalrounge.MainJob.Model

import android.content.Context
import capstonedesign.globalrounge.MainJob.MainMVP
import capstonedesign.globalrounge.MainJob.Presenter.MainPresenter
import capstonedesign.globalrounge.MainJob.User

class MainModel constructor(presenter: MainPresenter,context: Context): MainMVP.Model{
    private val presenter = presenter

    //자동로그인에 필요한 변수
    private val preferences = context.getSharedPreferences("auto", 0)
    private val editor = preferences.edit()
    var saveCheck = false
    override var checkBoxState: Boolean = false


    override fun requestPermission(user: User) {
        val permission = SejongPermission(object :
            SejongPermission.RejectionCallback {
            override fun approval(user: User) {
                presenter.approvalPermission(user)
                saveCheck=true
            }
            override fun reject(text: String) {
                presenter.rejectPermission(text)
                saveCheck=false
            }
        })
        permission.requestUserInformation(user)
    }

    override fun saveUserInfo(user:User) {
        if(checkBoxState and saveCheck){
            with(editor){
                putString("id",user.id)
                putString("pw",user.pw)
                commit()
            }
        }
    }

    override fun getUserInfo(): User {
         with(preferences){
            val id = getString("id","")
            val pw = getString("pw","")
            return User(id,pw)
        }
    }

    override fun deleteUserInfo() {
        with(editor){
            clear()
            commit()
        }
    }
}