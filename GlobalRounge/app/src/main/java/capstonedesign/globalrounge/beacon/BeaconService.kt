package capstonedesign.globalrounge.beacon

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.annotation.RequiresApi
import android.util.Log
import capstonedesign.globalrounge.model.permission.BaseServer.Companion.STATE_OK
import capstonedesign.globalrounge.model.permission.ServerConnection
import com.estimote.sdk.Beacon
import com.estimote.sdk.BeaconManager
import com.estimote.sdk.Region
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import moe.codeest.rxsocketclient.SocketSubscriber
import java.nio.charset.StandardCharsets
import java.util.*


class BeaconService : Service() {
    private val sensorManager: SensorManager by lazy { this.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val vibrator: Vibrator by lazy { this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }
    private lateinit var ref: Disposable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        ref = ServerConnection
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
                            STATE_OK -> {
                                stopSelf()
                            }

                        }
                    }
                }
            })


        with(BeaconManager(this)) {
            setBackgroundScanPeriod(1000, 0)
            setForegroundScanPeriod(1000, 0)
            setMonitoringListener(object : BeaconManager.MonitoringListener {
                override fun onEnteredRegion(region: Region?, lists: MutableList<Beacon>?) {
                    Log.e("비콘 접속", "기모찌")
                    sensorManager.registerListener(
                        ShakeAlgorithm,
                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_NORMAL
                    )
                }

                override fun onExitedRegion(p0: Region?) {
                    Log.e("비콘 탈출", "기모찌")
                    sensorManager.unregisterListener(ShakeAlgorithm)
                }
            })
            connect {
                Region("monitored region", UUID.fromString("E20A39F4-73F5-4BC4-A12F-17D1AD07A961"), 1, 1).let {
                    this.startMonitoring(it)
                }
            }
        }

        startForeground(1, Notification())

        ShakeAlgorithm.setCallback(object :
            ShakeAlgorithm.ShakeCallback {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun vibrate() {
                vibrator.vibrate(300)
                ServerConnection.openAdmin()
                sensorManager.unregisterListener(ShakeAlgorithm)
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        ref.dispose()
    }


    companion object {
        fun getIntent(context: Context?) = Intent(context, BeaconService::class.java)
    }

}