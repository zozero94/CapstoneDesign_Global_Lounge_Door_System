package capstonedesign.globalrounge.qrjob

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import capstonedesign.globalrounge.model.QrCode
import capstonedesign.globalrounge.model.permission.BaseServer.Companion.STATE_CREATE
import capstonedesign.globalrounge.model.permission.ServerConnection
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import io.reactivex.android.schedulers.AndroidSchedulers
import moe.codeest.rxsocketclient.SocketSubscriber
import java.net.SocketException
import java.nio.charset.StandardCharsets


class QrPresenter(private val view: QrContract.View) : QrContract.Presenter {

    override fun subscribe() {
        try {
            var buffer =""
            val ref = ServerConnection.socketObservable!!.observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SocketSubscriber() {
                    override fun onConnected() {

                    }

                    override fun onDisconnected() {

                    }

                    override fun onResponse(data: ByteArray) {
                        val str = String(data, StandardCharsets.UTF_8)
                        try {
                            val result = JsonParser().parse(str) as JsonObject
                            when (result.get("seqType").asInt) {
                                STATE_CREATE -> {
                                    val bitmap = QrCode.makeQrCode(result.get("qr").asString)
                                    view.makeQrCode(bitmap)
                                }
                            }
                        } catch (e: IllegalStateException) {
                            e.printStackTrace()
                        }catch (e: JsonSyntaxException){
                            buffer +=str
                            if(buffer[buffer.length-1] =='\n'){
                                val realImage = Base64.decode(buffer,Base64.DEFAULT)
                                makeUserImages(realImage)
                                buffer=""
                            }
                        }
                    }
                })
            ServerConnection.addDisposable(ref)
        }catch (e : SocketException){
            e.printStackTrace()
        }
    }

    fun makeUserImages(imagesByte: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(imagesByte, 0, imagesByte.size)
        view.drawUserImages(bitmap)
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