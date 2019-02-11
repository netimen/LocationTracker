package ru.netimen.locationtracker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val uuidView by lazy { findViewById<TextView>(R.id.uuid) }
    private val timeoutView by lazy { findViewById<TextView>(R.id.timeout) }
    private val locationView by lazy { findViewById<TextView>(R.id.location) }

    private val component by lazy { DaggerAppComponent.builder().context(applicationContext).build() }
    private val controller by lazy { component.controller() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        uuidView.text = controller.uuid
        timeoutView.onTextChanged {
            controller.changeTimeout(if (it.isNotBlank()) Integer.valueOf(it) else 1)
        }
        controller.onTimeoutChanged = { timeoutView.text = it.toString() }
        controller.onLastLocationChanged = { if (it != null) locationView.text = it.toString() }
    }

    override fun onStart() {
        super.onStart()
        controller.start()
    }
}

// CUR autostart,

