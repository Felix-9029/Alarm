package de.felix.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private val notificationID = 9029
private val requestCode = 0
private val flags = 0

/**
 * Builds the notification and sends it afterwards.
 *
 * @param context, activity context.
 */

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(applicationContext, notificationID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    val snoozeIntent = Intent(applicationContext, AlarmSnoozeReceiver::class.java)
    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(applicationContext, requestCode, snoozeIntent, flags)

    // Build the notification
    val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.AlarmNotificationChannelID))
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(applicationContext.getString(R.string.AlarmNotificationTitle))
        .setContentText(messageBody)

        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        .addAction(R.drawable.ic_launcher_foreground, applicationContext.getString(R.string.snooze), snoozePendingIntent)

        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(notificationID, builder.build())
}

/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}
