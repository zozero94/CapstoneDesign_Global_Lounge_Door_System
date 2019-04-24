package capstonedesign.globalrounge.qrjob

import android.graphics.BitmapFactory
import android.util.Base64
import capstonedesign.globalrounge.model.QrCode
import capstonedesign.globalrounge.model.permission.BaseServer.Companion.STATE_CREATE
import capstonedesign.globalrounge.model.permission.ServerConnection
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
                        (JsonParser().parse(str) as JsonObject).let {
                            when (it.get("seqType").asInt) {
                                STATE_CREATE -> {
                                    QrCode.makeQrCode(it.get("qr").asString).let { bitmap ->
                                        view.makeQrCode(bitmap)
                                    }
                                }
                            }
                        }

                    } catch (e: JsonSyntaxException) {
                        buffer += str
                        if (buffer[buffer.length - 1] == '\n') {
                            Base64.decode(buffer, Base64.DEFAULT).let {
                                makeUserImages(it)
                            }
                            //TODO Glide 써보자 -> View로 ByteArray보내서 View에서 생성
                            //view.drawUserImages(realImage)
                            buffer = ""
                        }
                    }
                    catch (e : ClassCastException){
                        e.printStackTrace()
                    }
                }
            })
        ServerConnection.addDisposable(ref)
    }


    fun makeUserImages(imagesByte: ByteArray) {
        //TODO Glide 써보자 -> View로 ByteArray보내서 View에서 생성
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