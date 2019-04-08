package capstonedesign.globalrounge.MainJob.Presenter

import android.util.Log
import capstonedesign.globalrounge.MainJob.MainMVP
import capstonedesign.globalrounge.MainJob.Model.MainModel

class MainPresenter constructor(view: MainMVP.View) :
    MainMVP.Presenter {


    private val view = view
    private val model = MainModel(this)

    override fun loginClicked(id: String, pw: String) {
        when {
            id == "" -> view.noInformation("아이디를 입력하세요")
            pw == "" -> view.noInformation("패스워드를 입력하세요")
            else -> model.requestPermission(id, pw)
        }
    }

    override fun rejectPermission(text: String) {
        view.rejectPermission(text)
    }

    override fun approvalPermission(id: String) {
        Log.e("성공성공","앙 로그인 성공띠~~")
        //TODO 서버로 공개키와 개 젓같은것들을 보내고 인텐트 시작하셈
    }
}