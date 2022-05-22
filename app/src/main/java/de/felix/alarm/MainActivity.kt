package de.felix.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import de.felix.alarm.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.util.*

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: de.felix.todo.Activity</p>
 * <p>Datei: MainActivity.kt</p>
 * <p>Datum: 02.05.2022</p>
 * <p>Version: 1</p>
 *
 * This Project is inspired by https://developer.android.com/codelabs/advanced-android-kotlin-training-notifications
 */

class MainActivity : AppCompatActivity() {

    var _hour: Int = LocalDateTime.now().hour
    var _minute: Int = LocalDateTime.now().minute

    companion object {
        lateinit var mainActivity: MainActivity
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = this
        setContentView(R.layout.activity_main)


        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(toolbar)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        applySharedPreferenceSettings()
        setAlarmTime(_hour, _minute)
        val snoozeTime = sharedPreferences?.getString("snoozeTime", "4")
        seekBarSnoozeTime.max = 14
        seekBarSnoozeTime.progress = snoozeTime!!.toInt()
        setSnoozeTimeString(seekBarSnoozeTime.progress)

        seekBarSnoozeTime.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, currentValue: Int, fromUser: Boolean) {
                setSnoozeTimeString(currentValue)
                setSharedPreference("snoozeTime", currentValue.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        buttonAlarmTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                _hour = hour
                _minute = minute
                setSharedPreference("hour", _hour.toString())
                setSharedPreference("minute", _minute.toString())
                setAlarmTime(_hour, _minute)
            }
            TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }


        val saveData = SaveData(applicationContext)
        createChannel(getString(R.string.AlarmNotificationChannelID), getString(R.string.AlarmNotificationChannelName))

        buttonSetAlarm.setOnClickListener {
            saveData.setAlarm()
        }

        buttonCancelAlarm.setOnClickListener {
            saveData.cancelAlarm()
        }
    }

    fun createChannel(channelID: String, channelName: String) {
        val notificationChannel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
            .apply {
                setShowBadge(true)
            }

        notificationChannel.description = getString(R.string.AlarmNotificationChannelName)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        val longArray = LongArray(9)
        notificationChannel.vibrationPattern = longArray.apply {
            arrayOf<Long>(100, 200, 300, 400, 500, 400, 300, 200, 400)
        }

        val notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun onResume() {
        super.onResume()
        applySharedPreferenceSettings()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent Activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setAlarmTime(hour: Int, min: Int) {
        val time = "$hour:$min"
        buttonAlarmTime.text = time
    }

    private fun setSnoozeTimeString(time: Int) {
        val currentValueInc = time + 1
        val snoozeString = "$currentValueInc min"
        textViewSnoozeTime.text = snoozeString
    }

    private fun setSharedPreference(key: String, value: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.edit {
            putString(key, value)
        }
    }

    private fun applySharedPreferenceSettings() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val textSize = sharedPreferences.getString("fontsize", "19")!!.toFloat()
        buttonAlarmTime.textSize = textSize
        textViewSnoozeTimeString.textSize = textSize
        textViewSnoozeTime.textSize = textSize
        buttonCancelAlarm.textSize = textSize
        buttonSetAlarm.textSize = textSize

        val darkmode = sharedPreferences.getBoolean("darkmode", true)
        if (darkmode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}