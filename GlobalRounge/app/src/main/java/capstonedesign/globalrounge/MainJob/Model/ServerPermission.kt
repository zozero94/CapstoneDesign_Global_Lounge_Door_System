package capstonedesign.globalrounge.MainJob.Model

import moe.codeest.rxsocketclient.RxSocketClient
import moe.codeest.rxsocketclient.SocketClient
import moe.codeest.rxsocketclient.meta.SocketConfig
import moe.codeest.rxsocketclient.meta.ThreadStrategy
import java.nio.charset.Charset

object ServerPermission {
    private const val ip = "192.168.0.12"
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


}
