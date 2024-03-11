package com.a.asm.sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.a.asm.databinding.ActivityAppMainBinding
import com.webuy.click.OnClickListenerImpl
import com.webuy.common.SecondActivity

class AsmMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("alvin", "onCreate")
        setViewOnclick()
        setFakeViewOnclick()

    }

    private fun setFakeViewOnclick() {
        var fakeView = FakeView()
        var listener = OnClickListenerImpl()
        fakeView.setOnClickListener(listener)
        val abcWrap = AbcWrap()
        abcWrap.init()
        abcWrap.doAbc()
    }

    private val longValue: Long = 1000L
    private fun setViewOnclick() {
        binding.btnVisit.setOnClickListener {
            println(longValue)
            binding.tvPrivacyResult.text = getPrivacyInfo()
        }

        binding.tvPrivacyResult.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                Log.d("alvin", "binding.tvAns onclick normal")
            }
        })
        binding.tvObject.setOnClickListener(OnClickListenerImpl("tvObject"))
        binding.tvWrapper.setOnClickListener(OnClickListenerImpl("tvWrapper"))
        binding.btnSecondActivity.setOnClickListener {
            startActivity(Intent(this@AsmMainActivity, SecondActivity::class.java))
        }
        PrivacyVisitor.getSharedPreferences(this)
    }

    private fun getPrivacyInfo(): String {
        return PrivacyVisitor.visitPrivacy(this.applicationContext)
    }

    override fun onResume() {
        super.onResume()
        Log.d("alvin", "PrivacyMainActivity onResume()")
    }

}


