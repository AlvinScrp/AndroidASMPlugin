package com.webuy.common.base

import androidx.fragment.app.Fragment
import androidx.annotation.CallSuper

open class CBaseFragment : Fragment() {

    @CallSuper
    @Suppress("RedundantOverride")
    override fun onResume() {
        super.onResume()
    }
}