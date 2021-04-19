package humber.its.drivershigh.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import humber.its.drivershigh.data.localdata.History
import humber.its.drivershigh.data.localdata.HistoryAndRoute
import humber.its.drivershigh.data.localdata.MainDatabase

class DashboardViewModel(val app: Application) : AndroidViewModel(app) {

    private val db: MainDatabase = MainDatabase.build(app.applicationContext)

    val histories: LiveData<List<HistoryAndRoute>> = db.historyDao().observeAllHistoryAndRoute()

}