package com.a.privacy_sample

import android.app.Application
import android.content.Context

class HApp: Application() {
    companion object{
     lateinit var  context :Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this@HApp
        Hello.a()
//        Fake.getProcessName()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }
}