package capstonedesign.globalrounge.model.auto_login

import android.content.Context
import android.content.SharedPreferences
import capstonedesign.globalrounge.mainjob.MainPresenter
import capstonedesign.globalrounge.dto.User

object SharedData {
    //자동로그인에 필요한 변수

    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    var checkBoxState: Boolean = false //CheckBox의 isClicked

    fun setSharedPreferences(context: Context) {
        preferences = context.getSharedPreferences("auto", 0)
        editor = preferences.edit()
    }

    /**
     * preferences 에 저장되어있는 데이터를 꺼내온다.
     * @see MainPresenter.checkAutoLogin
     * @return User() : 저장되어있는 사용자 데이터 반환
     */

    fun getUserInfo(): User {
        with(preferences) {
            val id = getString("id", "")!!
            val pw = getString("pw", "")!!
            return User(id, pw)
        }
    }

    /**
     * preferences 초기화
     * @see MainPresenter.changeCheckState : 체크박스 상태를 확인 (체크가 안되어있을시 실행)
     * @see MainPresenter.deletePreference : 로그아웃 버튼이 클릭되었을 때 실행
     */
    fun deleteUserInfo() {
        with(editor) {
            clear()
            commit()
        }
    }

    /**
     * CheckBox가 활성화 상태면 SharedPreference 에 데이터 저장
     * @see MainPresenter.approvalPermission : 모든 인증완료가 되었을 때 데이터 저장
     * @param user sharedPreference 에 저장할 사용자 데이터
     */
    fun saveUserInfo(user: User) {
        if (checkBoxState) {
            editor.apply {
                putString("id", user.id)
                putString("pw", user.pw)
                commit()
            }
        }
    }


}