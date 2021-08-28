package com.example.dogsapp

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.dogsapp.utils.RELOAD_LIST


class FilterPreferences : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.filter_preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        // when sorting is changed user should be scrolled back to the top, but recycler view saves
        // the scrolling position for other cases. This just a dirty hack to indicate that this time
        // list should be scrolled to the top after the new list is submitted
        if (key == getString(R.string.sorting_key)) {
            with(sharedPreferences?.edit()) {
                this?.putBoolean(RELOAD_LIST, true)
                this?.apply()
            }
        }
    }
}

