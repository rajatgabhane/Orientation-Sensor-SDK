package com.tesseract.android.sdk.connector

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.widget.Toast
import com.tesseract.android.sdk.IOrientationDataProvider
import com.tesseract.android.sdk.OrientationService

object OrientationServiceConnector {

    private var provider: IOrientationDataProvider? = null

    private var serviceConnection : ServiceConnection? = null


    fun connect(context: Context): Boolean {
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(
                name: ComponentName, boundService: IBinder
            ) {
                provider = IOrientationDataProvider.Stub.asInterface(boundService)
                Toast.makeText(context, "Service Connected", Toast.LENGTH_LONG).show()
            }

            override fun onServiceDisconnected(name: ComponentName) {
                provider = null
                Toast.makeText(context, "Service  Disconnected", Toast.LENGTH_LONG).show()
            }
        }

        val intent= Intent(context, OrientationService::class.java)
        return context.bindService(intent, serviceConnection!!, Context.BIND_AUTO_CREATE)
    }

    fun disconnect(context: Context) {
        serviceConnection?.let { context.unbindService(it) }
    }

    fun getOrientationData() = provider?.orientationData
}