package com.a.asm.sample

import android.util.Log

class AbcWrap {

    private var abc: Abc? = null
    fun init() {
        setAbc {
//            AutoTrackUtil.autoTrackClickString2(this,it)
            Log.d("alvin", "doSome in abc.def:${it}")
        }

    }

    private fun setAbc(abc: Abc) {
        this.abc = abc
    }

    fun doAbc() {
        abc?.def("abc.def")
    }

    fun doSomething(intValue:Int,longValue:Long,str:String){
        Log.d("alvin","doSomething:${intValue}")
        Log.d("alvin","doSomething:${longValue}")
        Log.d("alvin","doSomething:${str}")
    }
}