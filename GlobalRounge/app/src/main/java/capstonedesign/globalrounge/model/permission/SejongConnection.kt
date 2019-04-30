package capstonedesign.globalrounge.model.permission

import android.util.Log
import capstonedesign.globalrounge.dto.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 세종대학교 인증정보를 우회접근하여 받아오는 class
 * @see capstonedesign.globalrounge.mainjob.MainPresenter.requestSejongPermission
 */
object SejongConnection {
    private val sejongPermission: Permission

    /**
     * 우회로그인 결과를 되돌려주는 Callback Interface
     */
    interface LoginCallback {
        fun reject(text: String)
        fun approval(user: User)
    }

    /**
     * POST 를 보내기위한 Retrofit Interface
     */
    private interface Permission {

        @POST(url)
        fun getResult(@Query("rUserid") rUserid: String, @Query("rPW") rPW: String, @Query("pro") pro: Int): Call<ResponseBody>

        companion object {
            const val url = "https://udream.sejong.ac.kr/main/loginPro.aspx/"
        }
    }

    /**
     * retrofit 정보를 초기화하는 생성자
     */
    init {
        sejongPermission = with(Retrofit.Builder()) {
            baseUrl(Permission.url)
            build()
        }.create(Permission::class.java)
    }

    /**
     * 사용자 정보를 요청하는 구간
     * @param user : 입력된 사용자 정보
     * @param callback : 응답값에 따른 콜백
     * @see capstonedesign.globalrounge.mainjob.MainPresenter.requestSejongPermission
     *
     */
    fun requestUserInformation(user: User, callback: LoginCallback) {
        sejongPermission.getResult(user.id, user.pw, 1)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                    response.body()!!.string().let {
                        if (it.contains("alert")) {//없는정보
                            if (it.contains("패스워드")) {
                                callback.reject("패스워드가 잘못 되었습니다.")
                            } else if (it.contains("아이디")) {
                                callback.reject("아이디가 잘못 되었습니다.")
                            }
                        } else {//있는정보
                            callback.approval(user)
                        }
                    }


                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("RequestPermission Error", "무언가 잘못되었군")
                }
            })

    }

}