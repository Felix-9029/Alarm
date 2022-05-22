package de.felix.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import java.time.LocalDateTime
import java.util.*


class SaveData(context: Context) {

    var context: Context? = context

    fun setAlarm() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.mainActivity)
        val hour: Int = sharedPreferences.getString("hour", "0")!!.toInt()
        val minute: Int = sharedPreferences.getString("minute", "0")!!.toInt()
        val calender = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY, hour)
        calender.set(Calendar.MINUTE, minute)
        calender.set(Calendar.SECOND, 0)

        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        intent.putExtra("notification", "Alarm time")
        intent.action = "de.felix.alarmmanager"
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC, calender.timeInMillis, pendingIntent);
    }

    fun cancelAlarm() {
        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val myIntent = Intent(context, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.cancel(pendingIntent)
    }
}