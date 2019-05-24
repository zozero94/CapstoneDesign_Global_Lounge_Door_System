package capstonedesign.globalrounge.qrjob

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.estimote.sdk.Beacon
import com.estimote.sdk.BeaconManager
import com.estimote.sdk.Region
import java.util.*


class BeaconService : Service() {
    private val manager by lazy {
        BeaconManager(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        manager.setBackgroundScanPeriod(5000, 0)
        manager.setForegroundScanPeriod(5000,0)
        //모니터링
        manager.setMonitoringListener(object : BeaconManager.MonitoringListener {
            override fun onEnteredRegion(p0: Region?, p1: MutableList<Beacon>?) {
                if (p1?.size != 0) {
                    Log.e("비콘 감지 : ", p0.toString())
                }
            }

            override fun onExitedRegion(p0: Region?) {
                Log.e("비콘 탈출", "")
            }
        })
        //거리 탐색
        manager.setRangingListener { region, list ->
            for (lists in list) {
                Log.e("UUiD : ", lists.proximityUUID.toString())
                Log.e("Major : ", lists.major.toString())
                Log.e("Minor : ", lists.minor.toString())
            }
        }
        manager.connect {
            Region("monitored region", UUID.fromString("E20A39F4-73F5-4BC4-A12F-17D1AD07A961"), 1, 1).let {
                manager.startMonitoring(it)
                manager.startRanging(it)
            }
        }

        startForeground(1, Notification())
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    companion object {
        fun getIntent(context: Context?) = Intent(context, BeaconService::class.java)
    }

}