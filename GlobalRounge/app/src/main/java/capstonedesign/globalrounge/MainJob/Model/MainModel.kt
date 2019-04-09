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
    override var checkBoxState: Boolean = false //CheckBox의 isClicked


    /**************** [ Override Function ] ****************/
    /**
     * 우회 로그인 요청 메소드
     * @see MainPresenter.loginClicked
     * @see MainPresenter.checkAutoLogin
     * @param user sejong udream에 던질 데이터
     */

    override fun requestSejongPermission(user: User) {
        SejongPermission(object :
            SejongPermission.LoginCallback {
            override fun approval(user: User) {//성공
                saveUserInfo(user)
                presenter.approvalPermission(user)
            }

            override fun reject(text: String) {//실패
                presenter.rejectPermission(text)
            }
        }).requestUserInformation(user)
    }


    /**
     * preferences 에 저장되어있는 데이터를 꺼내온다.
     * @see MainPresenter.checkAutoLogin
     * @return User()
     */

    override fun getUserInfo(): User {
        with(preferences) {
            val id = getString("id", "")!!
            val pw = getString("pw", "")!!
            return User(id, pw)
        }
    }

    /**
     * preferences 초기화
     * @see MainPresenter.changeCheckState
     * @see MainPresenter.logout
     */
    override fun deleteUserInfo() {
        with(editor) {
            clear()
            commit()
        }
    }

    /**
     * Server로 로그인정보 요청
     */
    override fun requestServerPermission(user: User) {
        //TODO Rx를 사용해서 정보 받아오기
    }

    /**************** [ Local Function ] ****************/
    /**
     * CheckBox가 활성화 상태면 SharedPreference 에 데이터 저장
     * @see MainModel.requestPermission
     * @param user sharedPreference 에 저장할 데이터
     */
     private fun saveUserInfo(user: User) {
        if (checkBoxState) {
            editor.apply {
                putString("id", user.id)
                putString("pw", user.pw)
                commit()
            }
        }
    }
}