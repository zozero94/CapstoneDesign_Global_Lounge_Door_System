package capstonedesign.globalrounge.mainjob.presenter

import Encryption.Encryption
import android.content.Context
import android.util.Log
import capstonedesign.globalrounge.mainjob.ADMIN
import capstonedesign.globalrounge.mainjob.MainContract
import capstonedesign.globalrounge.mainjob.model.MainModel
import capstonedesign.globalrounge.mainjob.model.ServerPermission
import capstonedesign.globalrounge.mainjob.model.ServerPermission.LOGIN_ALREADY
import capstonedesign.globalrounge.mainjob.model.ServerPermission.LOGIN_NO_DATA
import capstonedesign.globalrounge.mainjob.model.ServerPermission.LOGIN_OK
import capstonedesign.globalrounge.mainjob.STUDENT
import capstonedesign.globalrounge.mainjob.User
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import moe.codeest.rxsocketclient.SocketSubscriber
import java.nio.charset.StandardCharsets


class MainPresenter (private val view: MainContract.View, context: Context) : MainContract.Presenter {

    private val model: MainContract.Model
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    /**
     * Presenter 초기화 함수
     * @param view MVP의 View
     * @param context MainActivity의 Context
     */
    init {
        this.model = MainModel(this, context)
    }


    /**
     * 로그인 버튼이 눌렸을 때 동작하는 함수
     * Id, Pw의 상태를 내부적으로 확인
     *
     * Model에게 우회 로그인 접근
     * @see MainModel.requestPermission
     * 자동로그인을 위한 정보 저장
     * @see MainModel.saveUserInfo
     *
     * @param user EditText에 저장된 id,pw를 저장하는 dataClass
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
            model.requestSejongPermission(user) //사용자 확인
        }
    }

    /**
     * 우회접근에서 거절되었을 때 호출
     * @see MainModel.requestPermission
     * @param text view로 전송할 text
     */
    override fun rejectPermission(text: String) {
        view.alertToast(text)
    }

    /**
     * 우회접근에서 승인되었을 때 호출
     * Server 로 다시한번 승인을 요청한다.
     * @see MainModel.requestSejongPermission
     * @see MainPresenter.loginClicked
     * @param user 로그인 사용자의 dataClass
     */
    override fun approvalPermission(user: User) {
        ServerPermission.connectSocket()
        val ref = ServerPermission.socket!!.connect()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SocketSubscriber() {
                override fun onConnected() {
                    view.alertToast("연결띠")
                    model.requestServerPermission(user)
                }

                override fun onDisconnected() {
                    view.alertToast("연결해제띠")
                }

                override fun onResponse(data: ByteArray) {
                    val str = String(data, StandardCharsets.UTF_8)
                    val result = JsonParser().parse(str) as JsonObject
                    when (result.get("seqType").asInt) {
                        LOGIN_OK -> {
                            val stu = Encryption.getDecodedString(result.get("data").asString)!!
                            model.saveUserInfo(user)//체크박스에 따른 자동로그인 저장
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
        compositeDisposable.add(ref)


    }

    /**
     * checkBox의 상태가 변하였을때 호출되는 함수
     * @see view.onCreate
     * @param isChecked checkBox 체크여부
     */
    override fun changeCheckState(isChecked: Boolean) {
        model.checkBoxState = isChecked
        if (!isChecked) {
            model.deleteUserInfo()
        }
    }

    /**
     * 자동로그인 check 여부
     * 앱 실행시 자동로그인을 체크한다
     * @see view.onCreate
     * @return Boolean
     */
    override fun checkAutoLogin(): Boolean {
        val user = model.getUserInfo()
        if (user.id != "" && user.pw != "") {//자동로그인이 되어있다면
            model.requestSejongPermission(user)
            return true
        }
        return false
    }

    /**
     * 로그아웃
     * sharedPreference에 데이터를 삭제한다
     * @see view.onActivityResult
     */
    override fun deletePreference() {
        model.deleteUserInfo()
    }

    /**
     * Rx 참조 제거
     * @see view.onPause
     */
    override fun dispose() {
        compositeDisposable.clear()
    }
}