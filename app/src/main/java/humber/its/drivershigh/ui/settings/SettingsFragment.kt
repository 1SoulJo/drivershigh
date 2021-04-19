package humber.its.drivershigh.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import humber.its.drivershigh.R
import humber.its.drivershigh.data.localdata.DatabaseUtil

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val resetDbPref: Preference? = findPreference("reset_db") as Preference?
        resetDbPref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            DatabaseUtil.resetRoutes(this)
            Snackbar.make(requireView(), R.string.updated_database, Snackbar.LENGTH_SHORT).show()
            true
        }

        val clearHistoryPref: Preference? = findPreference("clear_history") as Preference?
        clearHistoryPref?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            DatabaseUtil.clearHistory(this)
            Snackbar.make(requireView(), R.string.cleared_history, Snackbar.LENGTH_SHORT).show()
            true
        }
    }
}