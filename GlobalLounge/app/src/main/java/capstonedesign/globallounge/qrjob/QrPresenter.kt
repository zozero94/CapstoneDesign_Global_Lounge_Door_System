package capstonedesign.globallounge.qrjob

import android.content.Context
import capstonedesign.globallounge.model.beacon.Beacon
import capstonedesign.globallounge.model.QrCode
import capstonedesign.globallounge.model.permission.BaseServer.Companion.STATE_CREATE
import capstonedesign.globallounge.model.permission.BaseServer.Companion.STATE_OK
import capstonedesign.globallounge.model.permission.BaseServer.Companion.STATE_URL
import capstonedesign.globallounge.model.permission.ServerConnection
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.reactivex.android.schedulers.AndroidSchedulers
import moe.codeest.rxsocketclient.SocketSubscriber
import java.nio.charset.StandardCharsets


class QrPresenter(private val view: QrContract.View) : QrContract.Presenter {
    private val beacon = Beacon(view as Context)

    /**
     * QrActivity 로 넘어옴과 동시에 새로운 Observable 을 구독하는 함수
     * @see capstonedesign.globallounge.qrjob.QrActivity
     */
    override fun subscribe() {
        val ref = ServerConnection
            .socketObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SocketSubscriber() {
                override fun onConnected() {

                }

                override fun onDisconnected() {

                }

                override fun onResponse(data: ByteArray) {
                    val str = String(data, StandardCharsets.UTF_8)

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
                            STATE_OK -> {
                                //TODO 비콘 완료 시 행동
                            }

                        }
                    }
                }
            })
        ServerConnection.imageRequest()
        ServerConnection.addDisposable(ref)
    }

    /**
     * Qr코드 상태 업데이트를 요청하는 함수
     * @see capstonedesign.globallounge.qrjob.QrActivity.onResume
     */
    override fun stateRequest() {
        ServerConnection.sendStateOn()
    }

    /**
     * Qr코드 상태를 파괴하는 함수
     * @see capstonedesign.globallounge.qrjob.QrActivity.onPause
     */
    override fun stateDelete() {
        ServerConnection.sendStateOff()
    }

    /**
     * 서버와의 통신 종료
     * @see capstonedesign.globallounge.qrjob.QrActivity.onDestroy
     */
    override fun dispose() {
        ServerConnection.clearDisposable()

    }

    /**
     * 서버에 로그아웃 요청을 보내는 함수
     * @see capstonedesign.globallounge.qrjob.QrActivity.onCreate
     */
    override fun logout() {
        ServerConnection.logout()
    }

    /**
     * 비콘 연결 시작
     * @see capstonedesign.globallounge.qrjob.QrActivity.onCreate
     */
    override fun beaconConnect() {
        beacon.connect()
    }

    /**
     * 비콘 연결 종료
     * @see capstonedesign.globallounge.qrjob.QrActivity.onDestroy
     */
    override fun beaconDisconnect() {
        beacon.disconnect()
    }
}