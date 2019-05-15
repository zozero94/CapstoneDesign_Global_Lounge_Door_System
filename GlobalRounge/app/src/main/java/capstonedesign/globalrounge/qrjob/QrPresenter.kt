package capstonedesign.globalrounge.qrjob

import capstonedesign.globalrounge.model.QrCode
import capstonedesign.globalrounge.model.permission.BaseServer.Companion.STATE_CREATE
import capstonedesign.globalrounge.model.permission.BaseServer.Companion.STATE_URL
import capstonedesign.globalrounge.model.permission.ServerConnection
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import io.reactivex.android.schedulers.AndroidSchedulers
import moe.codeest.rxsocketclient.SocketSubscriber
import java.nio.charset.StandardCharsets


class QrPresenter(private val view: QrContract.View) : QrContract.Presenter {

    override fun subscribe() {
        val ref = ServerConnection.socketObservable!!.observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SocketSubscriber() {
                override fun onConnected() {

                }

                override fun onDisconnected() {

                }

                override fun onResponse(data: ByteArray) {
                    val str = String(data, StandardCharsets.UTF_8)
                    try {
                        (JsonParser().parse(str) as JsonObject).let { jsonObject ->
                            when (jsonObject.get("seqType").asInt) {
                                STATE_CREATE -> {
                                    QrCode.makeQrCode(jsonObject.get("qr").asString).let { bitmap ->
                                        view.makeQrCode(bitmap)
                                    }
                                }
                                STATE_URL -> {
                                    jsonObject.get("img").asString.let { url ->
                                        view.drawUserImages(url)
                                    }
                                }
                            }
                        }

                    } catch (e: JsonSyntaxException) {
                        e.printStackTrace()
                    } catch (e: ClassCastException) {
                        e.printStackTrace()
                    }
                }
            })
        ServerConnection.imageRequest()
        ServerConnection.addDisposable(ref)
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