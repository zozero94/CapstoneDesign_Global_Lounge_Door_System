package capstonedesign.globalrounge.mainjob

import Encryption.Encryption
import android.annotation.SuppressLint
import android.content.Context
import capstonedesign.globalrounge.dto.ADMIN
import capstonedesign.globalrounge.dto.STUDENT
import capstonedesign.globalrounge.dto.Student
import capstonedesign.globalrounge.dto.User
import capstonedesign.globalrounge.model.permission.BaseServer.Companion.LOGIN_ALREADY
import capstonedesign.globalrounge.model.permission.BaseServer.Companion.LOGIN_NO_DATA
import capstonedesign.globalrounge.model.permission.BaseServer.Companion.LOGIN_OK
import capstonedesign.globalrounge.model.permission.SejongConnection
import capstonedesign.globalrounge.model.permission.ServerConnection
import capstonedesign.globalrounge.model.util.AutoLogin
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moe.codeest.rxsocketclient.SocketSubscriber
import java.nio.charset.StandardCharsets


class MainPresenter(private val view: MainContract.View, context: Context) : MainContract.Presenter {
    init {
        AutoLogin.setSharedPreferences(context)
    }
    /**************** [ Override Function ] ****************/
    /**
     * 로그인 버튼이 눌렸을 때 동작하는 함수
     * Id, Pw의 상태를 내부적으로 확인
     * 일반 사용자 로그인
     * @see requestSejongPermission : sejong 로그인 시도
     * 관리자 로그인
     * @see approvalPermission : sejong 로그인 허가
     * @param user : 로그인 할 유저정보
     */
    override fun loginClicked(user: User) {
        when {
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

    }


    /**
     * checkBox의 상태가 변하였을때 호출되는 함수
     * @see capstonedesign.globalrounge.mainjob.MainActivity.onCreate
     *
     * 자동로그인 체크가 안되있을 시
     * @see AutoLogin.deleteUserInfo
     *
     * @param isChecked checkBox 체크여부
     */
    override fun changeCheckState(isChecked: Boolean) {
        AutoLogin.checkBoxState = isChecked
        if (!isChecked) {
            AutoLogin.deleteUserInfo()
        }
    }

    /**
     * 앱 실행시 자동로그인 check 여부 판단
     * @see capstonedesign.globalrounge.mainjob.MainActivity.onCreate
     * @see AutoLogin.getUserInfo
     *
     * 자동로그인이 되어있는 경우
     * @see requestSejongPermission
     *
     * @return Boolean
     */
    override fun checkAutoLogin(): Boolean {
        AutoLogin.getUserInfo().let {
            if (it.id != "" && it.pw != "") {//자동로그인이 되어있다면
                view.updateUserInfo(it)
                if (it.id == "admin") {approvalPermission(it)}
                else {requestSejongPermission(it)}
                return true
            }
            return false
        }
    }


    /**
     * 로그아웃 버튼 클릭시
     * sharedPreference 에 데이터를 삭제한다
     * @see capstonedesign.globalrounge.mainjob.MainActivity.onActivityResult
     */
    override fun deletePreference() {
        AutoLogin.deleteUserInfo()
    }

    /**
     * 서버와의 통신 종료
     * @see capstonedesign.globalrounge.mainjob.MainActivity.onPause
     */
    override fun dispose() {
        SejongConnection.clearDisposable()
    }
    /**************** [ Local Function ] ****************/
    /**
     * 세종대학교의 인증을 얻어오는 함수
     *
     * 인증 성공시
     * @see approvalPermission
     *
     * @param user : 요청할 사용자 정보
     */
    @SuppressLint("CheckResult")
    private fun requestSejongPermission(user: User) {
        val disposable = SejongConnection.requestUserInformation(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                response.string().let {
                    if (it.contains("alert")) {//없는정보
                        if (it.contains("패스워드")) {
                            view.alertToast("패스워드가 잘못 되었습니다.")
                        } else if (it.contains("아이디")) {
                            view.alertToast("아이디가 잘못 되었습니다.")
                        }
                    } else {//있는정보
                        approvalPermission(user)
                    }
                }
            }
        SejongConnection.addDisposable(disposable)
    }

    /**
     * 우회접근에서 승인되었을 때 호출
     * Server 로 다시한번 승인을 요청한다.
     * @see requestSejongPermission

     * @param user 학교인증에서 넘어온 사용자 정보
     */
    private fun approvalPermission(user: User) {
        ServerConnection.connect()
        val ref = ServerConnection.socketObservable
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.loadingStart() }
            .subscribe(object : SocketSubscriber() {
                override fun onConnected() {
                    Encryption.newKey()
                    ServerConnection.requestServerPermission(user)
                }

                override fun onDisconnected() {
                    view.alertToast("연결이 원활하지 않습니다.")
                    view.loadingDestroy()
                }

                override fun onResponse(data: ByteArray) {
                    val str = String(data, StandardCharsets.UTF_8)

                    (JsonParser().parse(str) as JsonObject).let { jsonObject ->
                        when (jsonObject.get("seqType").asInt) {
                            LOGIN_OK -> {
                                Encryption.getDecodedString(jsonObject.get("data").asString).let { decoded ->
                                    (Gson().fromJson<Any>(decoded, Student::class.java) as Student).let { student ->
                                        AutoLogin.saveUserInfo(user)//체크박스에 따른 자동로그인 저장
                                        view.startActivity(student)
                                    }
                                }
                            }
                            LOGIN_ALREADY -> {
                                view.alertToast("이미 로그인 중입니다.")
                            }
                            LOGIN_NO_DATA -> {
                                view.alertToast("정보가 잘못되었습니다.")
                            }
                        }
                    }
                    view.loadingDestroy()
                }


            })
        ServerConnection.addDisposable(ref)
    }


}