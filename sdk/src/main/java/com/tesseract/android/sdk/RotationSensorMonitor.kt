package com.tesseract.android.sdk

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

private const val FROM_RADS_TO_DEGS = -57
private const val ONE_MILLI_SEC_TO_NANO_SECS : Long = 1000000
private const val EIGHT_MILLI_SECS_TO_NANO_SECS : Long = 8 * ONE_MILLI_SEC_TO_NANO_SECS
private const val TAG = "RotationSensorMonitor"
class RotationSensorMonitor(context: Context): SensorEventListener {

    private val sensorManager: SensorManager
    private val sensor: Sensor
    private var previousTimeStampNanoSec: Long = 0

    var orientationData = OrientationData()

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    }

    fun start() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        Log.i(TAG, "Started!")
    }

    fun stop() {
        sensorManager.unregisterListener(this)
        Log.i(TAG, "Stopped!")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //DO NOTHING
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor == sensor) {
            if ((event.timestamp - EIGHT_MILLI_SECS_TO_NANO_SECS) > previousTimeStampNanoSec) {
                updateData(event.values)
                previousTimeStampNanoSec = event.timestamp
                Log.i(TAG, "Update timestamp: $previousTimeStampNanoSec")
            }
        }
    }

    private fun updateData(vectors: FloatArray) {
        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors)
        val worldAxisX = SensorManager.AXIS_X
        val worldAxisZ = SensorManager.AXIS_Z
        val adjustedRotationMatrix = FloatArray(9)
        SensorManager.remapCoordinateSystem(
            rotationMatrix,
            worldAxisX,
            worldAxisZ,
            adjustedRotationMatrix
        )
        val orientation = FloatArray(3)
        SensorManager.getOrientation(adjustedRotationMatrix, orientation)
        val azimuth: Float = orientation[0] * FROM_RADS_TO_DEGS
        val pitch: Float = orientation[1] * FROM_RADS_TO_DEGS
        val roll: Float = orientation[2] * FROM_RADS_TO_DEGS
        orientationData = OrientationData(azimuth, pitch, roll)
        Log.i(TAG, orientationData.toString())
    }
}