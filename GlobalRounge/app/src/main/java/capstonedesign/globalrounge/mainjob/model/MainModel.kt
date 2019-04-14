package capstonedesign.globalrounge.mainjob.model

import Encryption.Encryption
import android.content.Context
import capstonedesign.globalrounge.Student
import capstonedesign.globalrounge.mainjob.MainContract
import capstonedesign.globalrounge.mainjob.model.ServerPermission.LOGIN
import capstonedesign.globalrounge.mainjob.presenter.MainPresenter
import capstonedesign.globalrounge.mainjob.User
import com.google.gson.JsonObject


class MainModel(private val presenter: MainContract.Presenter, context: Context) : MainContract.Model {


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
     * @see MainPresenter.deletePreference
     */
    override fun deleteUserInfo() {
        with(editor) {
            clear()
            commit()
        }
    }

    /**
     * Server로 로그인정보 요청
     * @See MainPresenter.approvalPermission
     * @param user 전송할 이용자 정보
     * @sample
     * {
     * “seqType”:“100”, “data” :
     *        {“id”:“14011038”, “key”:“publicKey”,“type”:“[0:학생, 1:관리자]”}
     * }
     */
    override fun requestServerPermission(user: User) {
        Encryption.newKey()

        val message = JsonObject().apply {
            addProperty("seqType", LOGIN)
            addProperty("data", JsonObject().apply {
                addProperty("id", user.id)
                addProperty("key", Encryption.strPublicKey)//TODO 암호화 키 삽입
                addProperty("type", user.tag)
            }.toString())
        }
        ServerPermission.socket?.sendData(message.toString() + "\n")
        //\n을 붙이지 않으면 서버에서 ReadLine으로 읽을 수 없음
    }

    /**
     * CheckBox가 활성화 상태면 SharedPreference 에 데이터 저장
     * @see MainModel.requestSejongPermission
     * @param user sharedPreference 에 저장할 데이터
     */
    override fun saveUserInfo(user: User) {
        if (checkBoxState) {
            editor.apply {
                putString("id", user.id)
                putString("pw", user.pw)
                commit()
            }
        }
    }
}