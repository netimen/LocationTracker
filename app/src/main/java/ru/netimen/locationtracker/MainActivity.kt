package ru.netimen.locationtracker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val uuidView by lazy { findViewById<TextView>(R.id.uuid) }
    private val timeoutView by lazy { findViewById<TextView>(R.id.timeout) }
    private val locationView by lazy { findViewById<TextView>(R.id.location) }

    private val settingsManager by lazy { SettingsManager(this.applicationContext) }
    private val networkManager by lazy { NetworkManager(settingsManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        uuidView.text = settingsManager.uuid
        timeoutView.text = settingsManager.timeoutS.toString()
        timeoutView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                settingsManager.timeoutS = Integer.valueOf(s.toString())
            }
        })
        LocationManager(this.applicationContext).startListen(10L) {
            locationView.text = it.toString()
            networkManager.sendLocation(it)
        }
    }
}

// CUR change timeout, autostart, service, store last success, fabric, battery

