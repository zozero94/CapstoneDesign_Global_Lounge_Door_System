package capstonedesign.globalrounge.qrjob

import android.graphics.BitmapFactory
import android.util.Log
import capstonedesign.globalrounge.model.QrCode
import capstonedesign.globalrounge.model.permission.BaseServer.Companion.STATE_CREATE
import capstonedesign.globalrounge.model.permission.ServerConnection
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.reactivex.android.schedulers.AndroidSchedulers
import moe.codeest.rxsocketclient.SocketSubscriber
import java.net.SocketException
import java.nio.charset.StandardCharsets


class QrPresenter(private val view: QrContract.View) : QrContract.Presenter {

    override fun subscribe() {
        try {
            val ref = ServerConnection.socketObservable!!.observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SocketSubscriber() {
                    override fun onConnected() {

                    }

                    override fun onDisconnected() {
                        Log.d("기모띄", "onDisConnect")
                    }

                    override fun onResponse(data: ByteArray) {
                        val str = String(data, StandardCharsets.UTF_8)
                        val result = JsonParser().parse(str) as JsonObject
                        try {
                            when (result.get("seqType").asInt) {
                                STATE_CREATE -> {
                                    val bitmap = QrCode.makeQrCode(result.get("qr").asString)
                                    view.makeQrCode(bitmap)
                                }
                            }
                        } catch (e: IllegalStateException) {
                            e.printStackTrace()
                        }
                    }
                })
            ServerConnection.addDisposable(ref)
        }catch (e : SocketException){
            e.printStackTrace()
        }
    }

    override fun makeUserImages(imagesByte: ByteArray) {
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