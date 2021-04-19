package humber.its.drivershigh.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService


class DriveService : JobIntentService() {
    companion object {
        const val id = 1

        fun enqueue(ctx: Context, intent: Intent) {
            enqueueWork(ctx, DriveService::class.java, id, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.d("DriveService", "handling work!")
    }
}