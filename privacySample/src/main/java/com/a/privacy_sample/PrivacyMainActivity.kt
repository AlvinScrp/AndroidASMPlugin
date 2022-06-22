package com.a.privacy_sample

import android.content.Context
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
//            var ans = PrivacyVisitor.visitPrivacy(this@PrivacyMainActivity)

            var simCountrySo = PrivacyVisitor.getSimCountryIso(this@PrivacyMainActivity)
            var androidId = PrivacyVisitor.getAndroidID(this@PrivacyMainActivity)
            var spValue = PrivacyVisitor.getSharedPreferences(this@PrivacyMainActivity)
                .getString("key", "ddd")
            var ans =
                "simCountryIso:${simCountrySo} \n androidId:${androidId} \n spValue:${spValue}"

            binding.tvAns.text = ans
        }

        ( this@PrivacyMainActivity as Context).getSharedPreferences("sd", MODE_MULTI_PROCESS)
    }


    private fun test(time: Long) {
        Thread.sleep(time)
    }

    private fun sum(i: Int, j: Int): Int {
        return i + j
    }
}