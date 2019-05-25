package capstonedesign.globalrounge.beacon

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.RemoteException
import android.os.Vibrator
import android.support.annotation.RequiresApi
import android.util.Log
import capstonedesign.globalrounge.model.permission.ServerConnection
import org.altbeacon.beacon.*
import java.util.*

class Beacon(context: Context) : BeaconConsumer {
    private val manager by lazy { BeaconManager.getInstanceForApplication(context) }
    private val sensorManager: SensorManager by lazy { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val vibrator: Vibrator by lazy { context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }

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

    fun connect() {
        manager.bind(this@Beacon)
    }

    fun disconnect() {
        manager.unbind(this@Beacon)
    }


    override fun onBeaconServiceConnect() {
        with(manager) {
            removeAllMonitorNotifiers()
            foregroundScanPeriod = 20000
            updateScanPeriods()
            beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"))
            addMonitorNotifier(object : MonitorNotifier {
                override fun didDetermineStateForRegion(flag: Int, p1: Region?) {
                    Log.e("비콘 결정", flag.toString() + p1.toString())
                    if (flag == MonitorNotifier.INSIDE) {
                        registerListener()
                    }
                }

                override fun didEnterRegion(p0: Region?) {
                    Log.e("enter", p0.toString())
                    registerListener()

                }

                override fun didExitRegion(p0: Region?) {
                    Log.e("exit", p0.toString())
                    sensorManager.unregisterListener(ShakeAlgorithm)
                }
            })
            try {
                startMonitoringBeaconsInRegion(
                    Region(
                        "monitored region",
                        Identifier.fromUuid(UUID.fromString("E20A39F4-73F5-4BC4-A12F-17D1AD07A961")),
                        Identifier.fromInt(1),
                        Identifier.fromInt(1)
                    )

                )
            } catch (e: RemoteException) {
                e.printStackTrace()
            }

            addRangeNotifier { beacons, _ ->
                for (beacon in beacons) {
                    Log.e("비콘 감지", beacon.toString())
                    registerListener()
                }
            }
            try {
                startRangingBeaconsInRegion(
                    Region(
                        "monitored region",
                        Identifier.fromUuid(UUID.fromString("E20A39F4-73F5-4BC4-A12F-17D1AD07A961")),
                        Identifier.fromInt(1),
                        Identifier.fromInt(1)
                    )
                )
            } catch (e: RemoteException) {
                e.printStackTrace()
            }

        }
    }

    override fun getApplicationContext(): Context {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unbindService(p0: ServiceConnection?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindService(p0: Intent?, p1: ServiceConnection?, p2: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun registerListener() {
        sensorManager.registerListener(
            ShakeAlgorithm,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }
}