package com.a.privacy_sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.a.privacy_sample.databinding.ActivityAppMainBinding

class PrivacyMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        var str = Hello.a()
//        Toast.makeText(this@MainActivity,str,Toast.LENGTH_LONG).show()


        binding.btnVisit.setOnClickListener {
            var ans = PrivacyVisitor.visitPrivacy(this@PrivacyMainActivity)
            binding.tvAns.text = ans
        }
    }


    private fun test(time: Long) {
        Thread.sleep(time)
    }

    private fun sum(i: Int, j: Int): Int {
        return i + j
    }
}