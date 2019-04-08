package capstonedesign.globalrounge.MainJob.Presenter

import android.content.Context
import capstonedesign.globalrounge.MainJob.MainMVP
import capstonedesign.globalrounge.MainJob.Model.MainModel
import capstonedesign.globalrounge.MainJob.User

class MainPresenter : MainMVP.Presenter {


    private lateinit var view: MainMVP.View
    private lateinit var context: Context
    private lateinit var model: MainMVP.Model

    /**
     * Presenter 초기화 함수
     * @param view MVP의 View
     * @param context MainActivity의 Context
     */
    override fun init(view: MainMVP.View, context: Context) {
        this.view = view
        this.context = context
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
        else -> {
            model.requestPermission(user) //사용자 확인
            model.saveUserInfo(user) //정보 저장
            /*
            < QUESTION >
            TODO presenter에서 호출하기 위해 부득이하게 saveCheck변수를 사용함
            Model에서 Model 안에있는 함수를 호출해도 되는가? -> MVP
             */

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
     * @see MainModel.requestPermission
     * @param user 로그인 사용자의 dataClass
     */
    override fun approvalPermission(user: User) {
        //TODO 서버로 공개키와 user.id 전송 -> 데이터 有/無 확인 (RX 이용하기)
        //TODO 허가 : model에 데이터 저장 : model.saveUserInfo(user), 액티비티 실행 : view.startActivity(user)
        //TODO 거절 : view.alertToast("잘못된 접근입니다.")
        model.saveUserInfo(user)
        view.startActivity(user)
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
            model.requestPermission(user)
            return true
        }
        return false
    }

    /**
     * 로그아웃
     * sharedPreference에 데이터를 삭제한다
     * @see view.onActivityResult
     */
    override fun logout() {
        model.deleteUserInfo()
    }
}