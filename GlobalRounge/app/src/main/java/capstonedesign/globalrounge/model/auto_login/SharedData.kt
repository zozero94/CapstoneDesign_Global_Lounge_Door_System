package capstonedesign.globalrounge.model.auto_login

import android.content.Context
import android.content.SharedPreferences
import capstonedesign.globalrounge.model.User
import capstonedesign.globalrounge.mainjob.MainPresenter

object SharedData : DataStore {
    //자동로그인에 필요한 변수

    override lateinit var preferences: SharedPreferences
    override lateinit var editor: SharedPreferences.Editor

    override var checkBoxState: Boolean = false //CheckBox의 isClicked

    override fun setSharedPreferences(context: Context) {
        preferences = context.getSharedPreferences("auto", 0)
        editor = preferences.edit()
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
     * CheckBox가 활성화 상태면 SharedPreference 에 데이터 저장
     * @see requestSejongPermission
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