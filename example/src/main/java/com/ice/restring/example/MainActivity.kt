package com.ice.restring.example

import android.os.Bundle
import android.widget.TextView

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (findViewById<TextView>(R.id.text_view2)).setText(R.string.subtitle)
    }
}
