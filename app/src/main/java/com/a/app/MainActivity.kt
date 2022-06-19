package com.a.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.a.app.databinding.ActivityAppMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        test(100)
        sum(1, 2)
    }

    private fun test(time: Long) {
        Thread.sleep(time)
    }

    private fun sum(i: Int, j: Int): Int {
        return i + j
    }
}