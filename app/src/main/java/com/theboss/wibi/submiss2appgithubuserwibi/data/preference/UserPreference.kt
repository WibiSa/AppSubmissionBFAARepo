package com.theboss.wibi.submiss2appgithubuserwibi.data.preference

import android.content.Context

class UserPreference(context: Context) {
    companion object{
        private const val PREFS_NAME = "user_pref"
        private const val DAILY_REMINDER = "daily_reminder"
    }

    private val preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    //pref untuk daily reminder
    fun setDailyReminder(state: Boolean){
        val editor = preference.edit()
        editor.putBoolean(DAILY_REMINDER, state)
        editor.apply()
    }
}