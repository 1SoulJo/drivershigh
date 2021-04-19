package humber.its.drivershigh

import android.app.UiModeManager
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import humber.its.drivershigh.databinding.ActivityMainBinding
import humber.its.drivershigh.receiver.CarModeReceiver
import humber.its.drivershigh.ui.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val carModeReceiver = CarModeReceiver()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

        supportActionBar?.hide()

        // register car mode receiver
        val filter = IntentFilter(UiModeManager.ACTION_ENTER_CAR_MODE)
        filter.addAction(UiModeManager.ACTION_EXIT_CAR_MODE)
        registerReceiver(carModeReceiver, filter)
    }

    override fun onBackPressed() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        var handled = false
        for (f in navHost?.childFragmentManager?.fragments!!) {
            if (f is BaseFragment) {
                handled = f.onBackPressed()
                if (handled) {
                    break
                }
            }

            if (!handled) {
                super.onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(carModeReceiver)
    }
}