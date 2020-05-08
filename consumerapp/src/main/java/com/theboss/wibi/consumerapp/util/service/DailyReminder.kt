package com.theboss.wibi.submiss2appgithubuserwibi.util.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.theboss.wibi.consumerapp.R
import com.theboss.wibi.consumerapp.ui.view.MainActivity
import java.util.*

class DailyReminder: BroadcastReceiver() {

    companion object{
        private const val REQUEST_CODE = 8

        private const val CHANNEL_ID = "channel_08"
        private const val CHANNEL_NAME = "9 am daily reminder channel"
        private const val NOTIFICATION_ID = REQUEST_CODE
    }

    override fun onReceive(context: Context, intent: Intent) {
        //implementasi pesan reminder
        showNotification(context)
    }

    //setup pesan reminder
    fun showNotification(context: Context){
        val title = context.getString(R.string.daily_reminder)
        val message = context.getString(R.string.info_daily_reminder_notification)
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        //intent menuju MainActivity dari pesan
        val notificationIntent = Intent(context, MainActivity::class.java)
        //setup pendingIntent
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setAutoCancel(true)

        //buat channel untuk OS versi O keatas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(NOTIFICATION_ID, notification)
    }

    //mengatur jadwal reminder berulang dengan menggunakan AlarmManager jenis RTC_WAKEUP
    fun setRepeatingAlarm(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminder::class.java)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 19)
        calendar.set(Calendar.MINUTE, 6)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    //berhentikan reminder
    fun cancelRepeatingAlarm(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminder::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }

    //
    fun isAlarmSet(context: Context): Boolean{
        val intent = Intent(context, DailyReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_NO_CREATE)
        return  pendingIntent != null
    }
}