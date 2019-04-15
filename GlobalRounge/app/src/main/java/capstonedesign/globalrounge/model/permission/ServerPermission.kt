package capstonedesign.globalrounge.model.permission

import Encryption.Encryption
import capstonedesign.globalrounge.model.User
import capstonedesign.globalrounge.mainjob.MainPresenter
import com.google.gson.JsonObject
import moe.codeest.rxsocketclient.RxSocketClient
import moe.codeest.rxsocketclient.SocketClient
import moe.codeest.rxsocketclient.meta.SocketConfig
import moe.codeest.rxsocketclient.meta.ThreadStrategy
import java.nio.charset.Charset

object ServerPermission {
    private const val ip = "223.195.38.105"
    private const val port = 5050

    const val LOGIN = 100
    const val LOGIN_OK = 101
    const val LOGIN_ALREADY = 102
    const val LOGIN_NO_DATA = 103

    var socket: SocketClient? = null
    fun connectSocket() {
        socket = RxSocketClient.create(
            SocketConfig.Builder()
                .setIp(ip)
                .setPort(port)
                .setCharset(Charset.defaultCharset())
                .setThreadStrategy(ThreadStrategy.ASYNC)
                .setTimeout(30 * 1000)
                .build()
        )
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
        Encryption.newKey()
        val message = JsonObject().apply {
            addProperty("seqType", LOGIN)
            addProperty("data", JsonObject().apply {
                addProperty("id", user.id)
                addProperty("key", Encryption.strPublicKey)//TODO 암호화 키 삽입
                addProperty("type", user.tag)
            }.toString())
        }
        ServerPermission.socket?.sendData(message.toString() + "\n")
        //\n을 붙이지 않으면 서버에서 ReadLine으로 읽을 수 없음
    }

}
