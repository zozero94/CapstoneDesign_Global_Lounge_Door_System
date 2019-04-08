package capstonedesign.globalrounge.MainJob.Model

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.IOException


class SejongPermission constructor(callback: RejectionCallback) {
    private val sejongPermission: Permission
    private val callback = callback

    //Callback Interface
    interface RejectionCallback {
        fun reject(text:String)
        fun approval(id: String)
    }

    //POST 를 보내기위한 Retrofit Interface
    private interface Permission {

        @POST("https://udream.sejong.ac.kr/main/loginPro.aspx/")
        fun getResult(@Query("rUserid") rUserid: String, @Query("rPW") rPW: String, @Query("pro") pro: Int): Call<ResponseBody>

        companion object {
            const val url = "https://udream.sejong.ac.kr/main/loginPro.aspx/"
        }
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(Permission.url) //연결할 url
            .build()

        sejongPermission = retrofit.create(Permission::class.java)


    }

    fun requestUserInformation(id: String, pw: String) {
        val request = sejongPermission.getResult(id, pw, 1)
        request.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val body: String
                try {
                    body = response.body()!!.string()
                    if (body.contains("alert")) {//없는정보
                        if (body.contains("패스워드")) {
                            callback.reject("패스워드가 잘못 되었습니다.")
                        } else if (body.contains("아이디")) {
                            callback.reject("아이디가 잘못 되었습니다.")
                        }
                    } else {//있는정보
                        callback.approval(id)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("RequestPermission Error", "무언가 잘못되었군")
            }
        })

    }

}