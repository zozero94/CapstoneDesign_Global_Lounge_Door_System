package capstonedesign.globalrounge.MainJob.Model

import android.content.Context
import capstonedesign.globalrounge.MainJob.MainMVP
import capstonedesign.globalrounge.MainJob.Presenter.MainPresenter
import capstonedesign.globalrounge.MainJob.User


class MainModel constructor(presenter: MainPresenter, context: Context) : MainMVP.Model {
    private val presenter = presenter

    //자동로그인에 필요한 변수
    private val preferences = context.getSharedPreferences("auto", 0)
    private val editor = preferences.edit()
    private var saveCheck = false  //서버 결과에 따른 flag
    override var checkBoxState: Boolean = false //CheckBox의 isClicked


    //우회 로그인 요청메소드
    override fun requestPermission(user: User) {
        SejongPermission(object :
            SejongPermission.LoginCallback {
            override fun approval(user: User) {//성공
                presenter.approvalPermission(user)
                saveCheck = true
            }

            override fun reject(text: String) {//실패
                presenter.rejectPermission(text)
                saveCheck = false
            }
        }).requestUserInformation(user)
    }

    //CheckBox가 체크되어있고 서버에서 허가를 받은경우 저장
    override fun saveUserInfo(user: User) {
        if (checkBoxState and saveCheck) {
            with(editor) {
                putString("id", user.id)
                putString("pw", user.pw)
                commit()
            }
        }
    }

    //preferences 에 저장되어있는 데이터를 꺼내온다.
    override fun getUserInfo(): User {
        with(preferences) {
            val id = getString("id", "")
            val pw = getString("pw", "")
            return User(id, pw)
        }
    }

    //preferences 초기화
    override fun deleteUserInfo() {
        with(editor) {
            clear()
            commit()
        }
    }
}