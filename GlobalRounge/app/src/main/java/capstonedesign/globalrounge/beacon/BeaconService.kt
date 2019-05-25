package capstonedesign.globalrounge.beacon

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Vibrator
import android.support.annotation.RequiresApi
import android.util.Log
import capstonedesign.globalrounge.model.permission.ServerConnection
import com.estimote.sdk.Beacon
import com.estimote.sdk.BeaconManager
import com.estimote.sdk.Region
import java.util.*


class BeaconService(context: Context) {
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private val manager = BeaconManager(context)

    companion object {
        val region = Region("monitored region", UUID.fromString("E20A39F4-73F5-4BC4-A12F-17D1AD07A961"), 1, 1)
    }

    init {
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

    fun connectBeacon() {
        with(manager) {
            setForegroundScanPeriod(100, 0)
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
                this.startMonitoring(region)
            }
        }
    }

    fun disconnectBeacon() {
        with(manager) {
            stopMonitoring(region)
            disconnect()
        }

    }

}