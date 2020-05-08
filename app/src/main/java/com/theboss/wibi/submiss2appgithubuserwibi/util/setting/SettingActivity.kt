package com.theboss.wibi.submiss2appgithubuserwibi.util.setting

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.theboss.wibi.submiss2appgithubuserwibi.R
import com.theboss.wibi.submiss2appgithubuserwibi.data.preference.UserPreference
import com.theboss.wibi.submiss2appgithubuserwibi.util.service.DailyReminder
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var userPreference: UserPreference
    private lateinit var dailyReminder: DailyReminder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        dailyReminder = DailyReminder()
        userPreference =
            UserPreference(
                this
            )

        //setup daily reminder pada switch
        switch_daily_reminder.isChecked = dailyReminder.isAlarmSet(this)
        switch_daily_reminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                dailyReminder.setRepeatingAlarm(this)
                userPreference.setDailyReminder(true)
                Toast.makeText(this, getString(R.string.switch_reminder_on),Toast.LENGTH_SHORT).show()
            }else{
                dailyReminder.cancelRepeatingAlarm(this)
                userPreference.setDailyReminder(false)
                Toast.makeText(this, getString(R.string.switch_reminder_off),Toast.LENGTH_SHORT).show()
            }
        }

        //btn tes pesan reminder
        btn_test_notify.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_test_notify -> {
                dailyReminder.showNotification(this)
                Toast.makeText(this, getString(R.string.test_notify),Toast.LENGTH_SHORT).show()
            }
        }
    }

}
