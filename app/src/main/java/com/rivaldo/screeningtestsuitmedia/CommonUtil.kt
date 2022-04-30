package com.rivaldo.screeningtestsuitmedia

import android.app.Activity
import android.content.Context

object CommonUtil {

    val PHOTOS_PROFILE = "photos_profile"
    val NAMES = "names"

    fun saveSharedPrefs(activity: Activity, key: String, value: String) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun getSharedPrefs(activity: Activity, key: String): String? {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return null
        if (sharedPref.contains(key)) {
            return sharedPref.getString(key, "")
        } else {
            return null
        }
    }
}