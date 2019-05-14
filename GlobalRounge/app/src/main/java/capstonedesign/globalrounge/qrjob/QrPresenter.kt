package capstonedesign.globalrounge.qrjob

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import capstonedesign.globalrounge.model.QrCode
import capstonedesign.globalrounge.model.permission.BaseServer.Companion.STATE_CREATE
import capstonedesign.globalrounge.model.permission.BaseServer.Companion.STATE_URL
import capstonedesign.globalrounge.model.permission.ServerConnection
import capstonedesign.globalrounge.model.util.ImageLoader
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import io.reactivex.android.schedulers.AndroidSchedulers
import moe.codeest.rxsocketclient.SocketSubscriber
import java.nio.charset.StandardCharsets


class QrPresenter(private val view: QrContract.View) : QrContract.Presenter {

    override fun subscribe() {
        var buffer = ""
        val ref = ServerConnection.socketObservable!!.observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SocketSubscriber() {
                override fun onConnected() {

                }

                override fun onDisconnected() {

                }

                override fun onResponse(data: ByteArray) {
                    val str = String(data, StandardCharsets.UTF_8)
                    try {
                        (JsonParser().parse(str) as JsonObject).let {jsonObject->
                            when (jsonObject.get("seqType").asInt) {
                                STATE_CREATE -> {
                                    QrCode.makeQrCode(jsonObject.get("qr").asString).let { bitmap ->
                                        view.makeQrCode(bitmap)
                                    }
                                }
                                STATE_URL ->{
                                    jsonObject.get("img").asString.let {url->
                                        ImageLoader.request(url).subscribe {
                                            val result = it.string()
                                            //TODO 결과값 받아오기. Retrofit2 사용해서 유동적 url에 따른 이미지 받아오기
                                        }
                                    }
                                    //view.drawUserImages()
                                }
                                else->{

                                }

                            }
                        }

                    } catch (e: JsonSyntaxException) {
                        buffer += str
                        if (buffer[buffer.length - 1] == '\n') {
                            makeUserImages(Base64.decode(buffer, Base64.DEFAULT))
                            buffer = ""
                        }
                    } catch (e: ClassCastException) {
                        e.printStackTrace()
                    }
                }
            })
        ServerConnection.image_request()
        ServerConnection.addDisposable(ref)
    }


    fun makeUserImages(imagesByte: ByteArray) {
        BitmapFactory.decodeByteArray(imagesByte, 0, imagesByte.size)?.let {
            view.drawUserImages(it)
        }
    }

    override fun stateRequest() {
        ServerConnection.sendStateOn()
    }

    override fun stateDelete() {
        ServerConnection.sendStateOff()
    }

    override fun dispose() {
        ServerConnection.clearDisposable()
    }

    override fun logout() {
        ServerConnection.logout()
    }
}