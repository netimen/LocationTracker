package ru.netimen.locationtracker

import android.view.KeyEvent
import android.widget.TextView

//fun TextView.onTextChanged(runnable: (String) -> Unit) = addTextChangedListener(object : TextWatcher {
//        override fun afterTextChanged(s: Editable?) = Unit
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
//        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = runnable(s.toString())
//    })


fun TextView.onTextChanged(runnable: (String) -> Unit) =
    setOnEditorActionListener(object : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            runnable(text.toString())
            return true
        }
    })
