package com.webuy.click

import android.util.Log
import android.view.View

class OnClickListenerImpl(private val tag: String? = null) : View.OnClickListener {
    override fun onClick(v: View?) {
        Log.d("alvin", "Impl onclick ${tag}")
    }

}