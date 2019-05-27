package capstonedesign.globallounge.model.beacon

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

object ShakeAlgorithm : SensorEventListener {
    interface ShakeCallback {
        fun vibrate()
    }

    private var lastTime: Long = 0
    private var speed: Float = 0.0f
    private const val SHAKE_POWER = 1200
    private val locationHolder = LocationHolder(0.0f, 0.0f)
    private lateinit var callback: ShakeCallback
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        val shakeTerm = 100 //Shake를 감지하는 시간 (초기 300)

        //센서의 값이 바뀔때 호출되는 메소드
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis(); // 현재시간 불러오기
            val gabOfTime = (currentTime - lastTime); //현재시간과 마지막시간의 차이

            if (gabOfTime > shakeTerm) {
                lastTime = currentTime
                locationHolder.x = event.values[0]
                /*
                x = values[0] : 디바이스 가로
                y = values[1] : 디바이스 뚫고나오는 방향
                z = values[2] : 디바이스 세로
                 */
                speed = Math.abs(locationHolder.calculate()) / gabOfTime * 10000 // 현재좌표와 이전좌표의 차이를 계산

                if (speed > SHAKE_POWER) {
                    callback.vibrate()
                }

                locationHolder.lastX = event.values[0]
            }
        }
    }

    fun setCallback(callback: ShakeCallback) {
        this.callback = callback
    }
    data class LocationHolder(var lastX: Float, var x: Float) {
        fun calculate(): Float = x - lastX
    }
}