package com.webuy.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.a.asm.R

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SecondFragment.newInstance("sdsdsd"))
                .commitNow()
        }
    }
}