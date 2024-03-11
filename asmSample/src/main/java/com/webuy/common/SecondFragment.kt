package com.webuy.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a.asm.databinding.FragmentSecondBinding
import com.webuy.click.OnClickListenerImpl
import com.webuy.common.base.CBaseFragment

class SecondFragment : CBaseFragment() {
    private var param1: String? = null

    private lateinit var binding: FragmentSecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("p1")
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("alvin", "SecondFragment onResume")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvAns.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("alvin", "binding.tvAns onclick normal")
            }
        })

        val sskk2: View.OnClickListener = OnClickListenerImpl("tvObject")
        binding.tvObject.setOnClickListener(sskk2)
        val sskk = OnClickListenerImpl("tvWrapper")
        binding.tvWrapper.setOnClickListener(sskk)

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            SecondFragment().apply {
                arguments = Bundle().apply {
                    putString("p1", param1)
                }
            }
    }
}