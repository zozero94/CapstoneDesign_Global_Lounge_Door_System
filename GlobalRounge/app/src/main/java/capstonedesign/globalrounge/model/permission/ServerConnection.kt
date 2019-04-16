package capstonedesign.globalrounge.model.permission

import Encryption.Encryption
import capstonedesign.globalrounge.dto.User
import capstonedesign.globalrounge.mainjob.MainPresenter
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import moe.codeest.rxsocketclient.RxSocketClient
import moe.codeest.rxsocketclient.SocketClient
import moe.codeest.rxsocketclient.meta.DataWrapper
import moe.codeest.rxsocketclient.meta.SocketConfig
import moe.codeest.rxsocketclient.meta.ThreadStrategy
import java.nio.charset.Charset

/**
 * 서버로부터 인증정보를 받아오는 클래스
 * 1차인증 후 2차인증 실행
 * @see MainPresenter.approvalPermission
 */
object ServerConnection : BaseServer(){

    var socketObservable: Observable<DataWrapper>? = null
    private var socket : SocketClient?=null

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }
    fun clearDisposable(){
        compositeDisposable.clear()
    }
    /**
     * 서버와 연결하는 함수
     * @see MainPresenter.approvalPermission
     */
    fun connectSocket() {

        socket = RxSocketClient.create(
            SocketConfig.Builder()
                .setIp(ip)
                .setPort(port)
                .setCharset(Charset.defaultCharset())
                .setThreadStrategy(ThreadStrategy.ASYNC)
                .setTimeout(2 * 1000)
                .build()
        )
        socketObservable = socket!!.connect()

    }

    /**
     * Server로 로그인정보 요청
     * @See MainPresenter.approvalPermission
     * @param user 전송할 이용자 정보
     * @sample
     * {
     * “seqType”:“100”, “data” :
     *        {“id”:“14011038”, “key”:“publicKey”,“type”:“[0:학생, 1:관리자]”}
     * }
     */
    fun requestServerPermission(user: User) {
        val message = JsonObject().apply {
            addProperty("seqType", LOGIN)
            addProperty("data", JsonObject().apply {
                addProperty("id", user.id)
                addProperty("key", Encryption.strPublicKey)
                addProperty("type", user.tag)
            }.toString())
        }

        socket!!.sendData(message.toString() + "\n")
        //\n을 붙이지 않으면 서버에서 ReadLine으로 읽을 수 없음
    }

    /**
     * 서버로 사용자 상태 on을 요청
     * @see capstonedesign.globalrounge.qrjob.QrPresenter.stateRequest
     */
    fun sendStateOn(){
        val data = JsonObject()
        data.addProperty("seqType", STATE_REQ)
        socket!!.sendData(data.toString()+"\n")

    }

    /**
     * 서버로 사용자 상태 off를 요청
     * @see capstonedesign.globalrounge.qrjob.QrPresenter.stateDelete
     */
    fun sendStateOff(){
        val data = JsonObject()
        data.addProperty("seqType", STATE_DEL)
        socket!!.sendData(data.toString()+"\n")
    }

    /**
     * 사용자 로그아웃을 요청
     * @see capstonedesign.globalrounge.qrjob.QrPresenter.logout
     */
    fun logout(){
        val data = JsonObject()
        data.addProperty("seqType", LOGOUT)
        socket!!.sendData(data.toString()+"\n")
    }


}