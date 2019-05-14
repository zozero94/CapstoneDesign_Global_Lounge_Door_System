package capstonedesign.globalrounge.model.util

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

object ImageLoader {

    private lateinit var loader: Loader
    private val client = OkHttpClient()

    init {
        loader = with(Retrofit.Builder()) {
            baseUrl("https://user-images.githubusercontent.com/34762799/57696831-c38ff000-768c-11e9-8218-582cfafe9d9a.jpg")
            build()
        }.create(Loader::class.java)
    }

    private interface Loader {
        @GET("/34762799/{data}")
        fun getImage(@Path("data") data: String): Observable<ResponseBody>
    }

    fun request(url: String): Observable<ResponseBody> =
        loader.getImage(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}