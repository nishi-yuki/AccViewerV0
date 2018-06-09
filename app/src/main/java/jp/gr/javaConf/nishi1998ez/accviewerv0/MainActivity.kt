package jp.gr.javaConf.nishi1998ez.accviewerv0

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.pow
import kotlin.math.sqrt

const val DISPLAY_CYCLE = 5


class MainActivity : AppCompatActivity() {

    private lateinit var mSensorManager : SensorManager
    private lateinit var msel : MySensorEventListener

    internal lateinit var text4acc : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text4acc = findViewById(R.id.text_view4acc)
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        msel = MySensorEventListener(this)
    }

    override fun onResume() {
        super.onResume()

        val mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor
        mSensorManager.registerListener(msel, mAccSensor, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(msel)
    }
}


class MySensorEventListener(mActivity : MainActivity) : SensorEventListener {

    private val mAct = mActivity
    private var accAllMax : Float = 0F
    private var accAllMin : Float = -1F
    private var count = 0

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //nothing to do
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if (event != null && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            val accX = event.values[0]
            val accY = event.values[1]
            val accZ = event.values[2]

            val accAll = sqrt(
                           accX.pow(2)
                            + accY.pow(2)
                            + accZ.pow(2))

            if (accAllMax < accAll) {
                accAllMax = accAll
            }
            if (accAllMin > accAll || accAllMin < 0 ) {
                accAllMin = accAll
            }


            count += 1
            if (count > DISPLAY_CYCLE) {
                count = 0
                val strTmp =
                        "X : $accX\nY : $accY\nZ : $accZ\nALL : $accAll\n\n" +
                                "MAX : $accAllMax\nMIN : $accAllMin"
                mAct.text4acc.setText(strTmp)
            }
        }
    }
}