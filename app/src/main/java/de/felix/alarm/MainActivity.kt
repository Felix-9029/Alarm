package de.felix.alarm

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import de.felix.alarm.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val binding : ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(toolbar)

        applySharedPreferenceSettings()

        buttonCancelAlarm.setOnClickListener {

        }

        seekBarSnoozeTime.progress = 4
        seekBarSnoozeTime.max = 14
        seekBarSnoozeTime.setOnSeekBarChangeListener(object: OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, currentValue: Int, fromUser: Boolean) {
                var currentValueInc = currentValue
                currentValueInc++
                val snoozeString = "$currentValueInc min"
                textViewSnoozeTime.text = snoozeString
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        var currentValueInc = seekBarSnoozeTime.progress
        currentValueInc++
        val snoozeString = "$currentValueInc min"
        textViewSnoozeTime.text = snoozeString
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

    private fun applySharedPreferenceSettings() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val textsizeString = sharedPreferences?.getString("fontsize", "19")
        if (textsizeString != null) {
            textView.textSize = textsizeString.toFloat()
            textViewSnoozeTimeString.textSize = textsizeString.toFloat()
            textViewSnoozeTime.textSize = textsizeString.toFloat()
            buttonCancelAlarm.textSize = textsizeString.toFloat()
            buttonSetAlarm.textSize = textsizeString.toFloat()
        }

        val darkmode = sharedPreferences?.getBoolean("darkmode", true)
        if (darkmode != null) {
            if (darkmode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}