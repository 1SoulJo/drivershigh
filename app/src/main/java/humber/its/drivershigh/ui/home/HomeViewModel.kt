package humber.its.drivershigh.ui.home

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.PolyUtil
import humber.its.drivershigh.api.DirectionClient
import humber.its.drivershigh.data.localdata.MainDatabase
import humber.its.drivershigh.data.localdata.Route
import humber.its.drivershigh.direction.Direction
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeViewModel(val app: Application) : AndroidViewModel(app) {
    val routes: LiveData<List<Route>>
    val routePoints: MutableLiveData<MutableList<LatLng>> = MutableLiveData(mutableListOf())

    private val db: MainDatabase = MainDatabase.build(app.applicationContext)

    init {
        routes = db.routeDao().observeAllRoutes()
    }

    fun fetchRouteInfo(data: Route) {
        val ctx = app.applicationContext
        ctx.packageManager.getApplicationInfo(ctx.packageName, PackageManager.GET_META_DATA)
            .apply {
                val key = metaData.getString("com.google.android.geo.DIRECTION_API_KEY")

                val api = DirectionClient.getInstance()
                val call = api?.getRoute(
                    "${data.startLat},${data.startLng}", "${data.endLat},${data.endLng}", key!!
                )

                call?.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        c: Call<ResponseBody>?,
                        res: Response<ResponseBody>?
                    ) {
                        res?.body()?.string()?.let {
                            val list = mutableListOf<LatLng>()
                            val r = Gson().fromJson(it, Direction::class.java)
                            for (i in r.routes[0].legs[0].steps.indices) {
                                val s = r.routes[0].legs[0].steps[i]
                                list += LatLng(s.start_location.lat, s.start_location.lng)
                                if (i == r.routes[0].legs[0].steps.size - 1) {
                                    list += LatLng(s.end_location.lat, s.end_location.lng)
                                }
                            }
                            routePoints.value =
                                PolyUtil.decode(r.routes[0].overview_polyline.points)
                        }
                    }

                    override fun onFailure(c: Call<ResponseBody>?, t: Throwable?) {
                        Log.d("HomeViewModel", "Exception : ${t.toString()}")
                    }
                })
            }
    }

    fun startDriving(route: Route) {
        saveHistory(route)
        sendIntent(route)
    }

    private fun sendIntent(route: Route) {
        val uri = "https://www.google.com/maps/dir/?api=1&" +
                "waypoints=${route.startLat},${route.startLng}&" +
                "destination=${route.endLat},${route.endLng}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        app.startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveHistory(route: Route) {
        viewModelScope.launch {
            val localDate: LocalDateTime = LocalDateTime.now() //For reference
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
            val formattedString: String = localDate.format(formatter)

            db.historyDao().insertHistory(route.id!!, formattedString)
        }
    }
}