package capstonedesign.globalrounge.MainJob.Presenter

import android.content.Context
import capstonedesign.globalrounge.MainJob.MainMVP
import capstonedesign.globalrounge.MainJob.Model.MainModel
import capstonedesign.globalrounge.MainJob.User

class MainPresenter : MainMVP.Presenter {


    private lateinit var view:MainMVP.View
    private lateinit var context:Context
    private lateinit var model :MainMVP.Model


    override fun init(view: MainMVP.View, context: Context) {
        this.view = view
        this.context = context
        this.model = MainModel(this, context)
    }

    override fun loginClicked(user: User) {
        when {
            user.id == "" -> view.noInformation("아이디를 입력하세요")
            user.pw == "" -> view.noInformation("패스워드를 입력하세요")
            else -> {
                model.requestPermission(user) //사용자 확인
                model.saveUserInfo(user) //
            }
        }
    }

    override fun rejectPermission(text: String) {
        view.rejectPermission(text)
    }

    override fun approvalPermission(user: User) {
        //TODO 서버로 공개키와 개 젓같은것들을 보내고 인텐트 시작하셈
        model.saveUserInfo(user)
        view.startActivity()
    }

    override fun changeCheckState(isChecked: Boolean) {

        model.checkBoxState = isChecked
        if (!isChecked) {
            model.deleteUserInfo()
        }


    }

    override fun checkAutoLogin(): Boolean {
        val user = model.getUserInfo()
        if (user.id != "" && user.pw != "") {//자동로그인이 되어있다면
            model.requestPermission(user)
            return true
        }
        return false
    }
}