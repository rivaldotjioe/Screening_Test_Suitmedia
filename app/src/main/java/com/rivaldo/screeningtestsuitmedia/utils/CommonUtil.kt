package com.rivaldo.screeningtestsuitmedia.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import com.rivaldo.screeningtestsuitmedia.R
import com.rivaldo.screeningtestsuitmedia.model.Event

object CommonUtil {

    val PHOTOS_PROFILE = "photos_profile"
    val NAMES = "names"
    val EVENT_BUTTON = "event_button"
    val GUEST_BUTTON =  "guest_button"

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

    fun generateDummyDataEvent(resource : Resources) : MutableList<Event>{
        var listEvent : MutableList<Event> = mutableListOf()
        for (i in 0..10) {
            var event = Event("Event Dummy $i", "15 Jan 2021", "9:00 AM",resource.getString(R.string.lorem_ipsum) , R.drawable.img_cardview)
            listEvent.add(event)
        }
        return listEvent
    }
}