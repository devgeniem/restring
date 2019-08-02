package com.ice.restring.example

import android.app.Application

import com.ice.restring.Restring
import com.ice.restring.RestringConfig

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Restring.init(this,
            RestringConfig.Builder()
                .persist(true)
                .stringsLoader(SampleStringsLoader())
                .build()
        )
    }
}
