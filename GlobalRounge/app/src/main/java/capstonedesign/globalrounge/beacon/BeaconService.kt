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
import com.estimote.sdk.Beacon
import com.estimote.sdk.BeaconManager
import com.estimote.sdk.Region
import java.util.*


class BeaconService : Service() {
    private val sensorManager :SensorManager = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val vibrator : Vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        with(BeaconManager(this)) {
            setBackgroundScanPeriod(5000, 0)

            setMonitoringListener(object : BeaconManager.MonitoringListener {
                override fun onEnteredRegion(region: Region?, lists: MutableList<Beacon>?) {
                    sensorManager.registerListener(ShakeAlgorithm,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL)
                }

                override fun onExitedRegion(p0: Region?) {
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
            override fun vibrate() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    VibrationEffect.createOneShot(500, 255)
                } else {
                    vibrator.vibrate(500)
                }
                sensorManager.unregisterListener(ShakeAlgorithm)
            }
        })

    }


    companion object {
        fun getIntent(context: Context?) = Intent(context, BeaconService::class.java)
    }

}