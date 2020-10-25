package com.tesseract.android.sdk

import android.app.Service
import android.content.Intent
import android.os.IBinder

internal class OrientationService : Service() {
    private var monitor: RotationSensorMonitor? = null

    override fun onBind(intent: Intent?): IBinder? {
        monitor?.start()
        return object : IOrientationDataProvider.Stub() {
            override fun getOrientationData(): OrientationData? {
                return monitor?.orientationData
            }
        }
    }

    override fun onCreate() {
        if (monitor == null) {
            monitor = RotationSensorMonitor(this)
        }
        super.onCreate()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        monitor?.stop()
        return super.onUnbind(intent)
    }
}
