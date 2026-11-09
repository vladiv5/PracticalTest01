package ro.pub.cs.systems.eim.practicaltest01

import android.app.Service
import android.content.Intent
import android.os.IBinder
import ro.pub.cs.systems.eim.lab05.startedservice.ProcessingThread

class PracticalTest01Service : Service() {
    private var processingThread: ProcessingThread? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val first = intent?.getIntExtra("leftVal", 0) ?: 0
        val second = intent?.getIntExtra("rightVal", 0) ?: 0
        processingThread = ProcessingThread(this, first, second)
        processingThread?.start()
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        processingThread?.stopThread()
        super.onDestroy()
    }
}