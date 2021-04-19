package humber.its.drivershigh.data.localdata

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceFragmentCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class DatabaseUtil {
    companion object {
        val routes = """
            [
                {
                    "name": "Oakville Beauty",
                    "duration": 12,
                    "length": 4,
                    "startLat": 43.449808,
                    "startLng": -79.664025,
                    "endLat": 43.435459,
                    "endLng": -79.679324
                },
                {
                    "name": "Old Toronto",
                    "duration": 12,
                    "length": 4,
                    "startLat": 43.645934,
                    "startLng": -79.379137,
                    "endLat": 43.654287,
                    "endLng": -79.383160
                },
                {
                    "name": "Port Credit Vibe",
                    "duration": 12,
                    "length": 4,
                    "startLat": 43.570277,
                    "startLng": -79.565814,
                    "endLat": 43.545132,
                    "endLng": -79.592842
                },
                {
                    "name": "Niagara Lover",
                    "duration": 50,
                    "length": 80,
                    "startLat": 43.428873,
                    "startLng": -79.686298,
                    "endLat": 43.078123152380094,
                    "endLng": -79.07998939139082
                }
            ]
        """.trimIndent()

        fun resetRoutes(frg: Fragment) {
            val dao = MainDatabase.build(frg.requireContext()).routeDao()

            (frg as PreferenceFragmentCompat).lifecycleScope.launch {
                dao.deleteAll()

                val type = object : TypeToken<List<Route>>() {}.type
                val list = Gson().fromJson<List<Route>>(routes, type)
                for (r in list) {
                    dao.insertRoute(r)
                }
            }
        }

        fun clearHistory(frg: Fragment) {
            val dao = MainDatabase.build(frg.requireContext()).historyDao()

            (frg as PreferenceFragmentCompat).lifecycleScope.launch {
                dao.deleteAll()
            }
        }
    }
}