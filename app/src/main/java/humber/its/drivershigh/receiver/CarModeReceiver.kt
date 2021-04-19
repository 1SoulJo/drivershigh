package humber.its.drivershigh.receiver

import android.app.UiModeManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import humber.its.drivershigh.service.DriveService


class CarModeReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val action = intent!!.action
        if (UiModeManager.ACTION_ENTER_CAR_MODE == action) {
            Log.d("CarModeReceiver", "Entered Car Mode")
            val i = Intent(context, DriveService::class.java)
            DriveService.enqueue(context!!, i)
        } else if (UiModeManager.ACTION_EXIT_CAR_MODE == action) {
            Log.d("CarModeReceiver", "Exited Car Mode")
        }
    }
}