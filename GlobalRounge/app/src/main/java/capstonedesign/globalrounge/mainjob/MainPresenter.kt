package capstonedesign.globalrounge.mainjob

import Encryption.Encryption
import android.content.Context
import capstonedesign.globalrounge.dto.ADMIN
import capstonedesign.globalrounge.dto.STUDENT
import capstonedesign.globalrounge.dto.User
import capstonedesign.globalrounge.model.auto_login.SharedData
import capstonedesign.globalrounge.model.permission.SejongPermission
import capstonedesign.globalrounge.model.permission.ServerConnection
import capstonedesign.globalrounge.model.permission.ServerConnection.LOGIN_ALREADY
import capstonedesign.globalrounge.model.permission.ServerConnection.LOGIN_NO_DATA
import capstonedesign.globalrounge.model.permission.ServerConnection.LOGIN_OK
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.reactivex.android.schedulers.AndroidSchedulers
import moe.codeest.rxsocketclient.SocketSubscriber
import java.nio.charset.StandardCharsets


class MainPresenter(private val view: MainContract.View, context: Context) : MainContract.Presenter {




    init {
        SharedData.setSharedPreferences(context)
    }
    /**************** [ Override Function ] ****************/
    /**
     * 로그인 버튼이 눌렸을 때 동작하는 함수
     * Id, Pw의 상태를 내부적으로 확인
     * 일반 사용자 로그인
     * @see requestSejongPermission : sejong 로그인 시도*
     * 관리자 로그인
     * @see approvalPermission : sejong 로그인 허가
     * @param user : 로그인 할 유저정보
     */
    override fun loginClicked(user: User) = when {
        user.id == "" -> view.alertToast("아이디를 입력하세요")
        user.pw == "" -> view.alertToast("패스워드를 입력하세요")
        user.id.contains("admin") -> {
            if (user.pw == "admin") {
                user.tag = ADMIN
                this.approvalPermission(user)
            } else view.alertToast("잘못된 정보")
        }
        else -> {
            user.tag = STUDENT
            this.requestSejongPermission(user)
        }
    }


    /**
     * checkBox의 상태가 변하였을때 호출되는 함수
     * @see capstonedesign.globalrounge.mainjob.MainActivity.onCreate
     *
     * 자동로그인 체크가 안되있을 시
     * @see SharedData.deleteUserInfo
     *
     * @param isChecked checkBox 체크여부
     */
    override fun changeCheckState(isChecked: Boolean) {
        SharedData.checkBoxState = isChecked
        if (!isChecked) {
            SharedData.deleteUserInfo()
        }
    }

    /**
     * 앱 실행시 자동로그인 check 여부 판단
     * @see capstonedesign.globalrounge.mainjob.MainActivity.onCreate
     * @see SharedData.getUserInfo
     *
     * 자동로그인이 되어있는 경우
     * @see requestSejongPermission
     *
     * @return Boolean
     */
    override fun checkAutoLogin(): Boolean {
        val user = SharedData.getUserInfo()
        if (user.id != "" && user.pw != "") {//자동로그인이 되어있다면
            requestSejongPermission(user)
            return true
        }
        return false
    }

    /**
     * 로그아웃 버튼 클릭시
     * sharedPreference 에 데이터를 삭제한다
     * @see capstonedesign.globalrounge.mainjob.MainActivity.onActivityResult
     */
    override fun deletePreference() {
        SharedData.deleteUserInfo()
    }

    /**
     * 서버와의 통신 종료
     * @see capstonedesign.globalrounge.mainjob.MainActivity.onPause
     */
    override fun dispose() {
        ServerConnection.clearDisposable()
    }
    /**************** [ Local Function ] ****************/
    /**
     * 세종대학교의 인증을 얻어오는 함수
     *
     * 인증 성공시
     * @see approvalPermission
     * 인증 실패시
     * @see rejectPermission
     *
     * @param user : 요청할 사용자 정보
     */
    private fun requestSejongPermission(user: User) {
        SejongPermission.requestUserInformation(user, object : SejongPermission.LoginCallback {
            override fun approval(user: User) {
                approvalPermission(user)
            }

            override fun reject(text: String) {
                rejectPermission(text)
            }
        })
    }

    /**
     * 우회접근에서 승인되었을 때 호출
     * Server 로 다시한번 승인을 요청한다.
     * @see requestSejongPermission

     * @param user 학교인증에서 넘어온 사용자 정보
     */
    private fun approvalPermission(user: User) {
        ServerConnection.connectSocket()

        val ref = ServerConnection.socketObservable!!
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SocketSubscriber() {
                override fun onConnected() {
                    ServerConnection.requestServerPermission(user)
                }

                override fun onDisconnected() {
                    view.alertToast("연결이 원활하지 않습니다.")
                }

                override fun onResponse(data: ByteArray) {
                    val str = String(data, StandardCharsets.UTF_8)
                    val result = JsonParser().parse(str) as JsonObject
                    when (result.get("seqType").asInt) {
                        LOGIN_OK -> {
                            val stu = Encryption.getDecodedString(result.get("data").asString)!!
                            SharedData.saveUserInfo(user)//체크박스에 따른 자동로그인 저장
                            view.startActivity(stu)//액티비티 시작
                        }
                        LOGIN_ALREADY -> {
                            view.alertToast("이미 로그인 중입니다.")
                        }
                        LOGIN_NO_DATA -> {
                            view.alertToast("서버에 더미데이터가 없습니다.")
                        }
                    }
                }
            })
        ServerConnection.addDisposable(ref)
    }

    /**
     * 우회접근에서 거절되었을 때 호출
     * @see requestSejongPermission
     * @param text : 사용자에게 알릴 Text
     */
    private fun rejectPermission(text: String) {
        view.alertToast(text)
    }
}