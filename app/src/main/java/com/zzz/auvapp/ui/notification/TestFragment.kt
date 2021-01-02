package com.zzz.auvapp.ui.notification

import android.os.Bundle
import com.zzz.auvapp.R
import com.zzz.auvapp.databinding.FragmentTestBinding
import com.zzz.common.mvvm.BaseVMFragment

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2020/12/31
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class TestFragment: BaseVMFragment<FragmentTestBinding>(R.layout.fragment_test) {
    override fun openObserve() {

    }

    override fun initView() {
        val string = arguments?.getString("text")
        binding.tvTest.text = string
    }

    override fun initData() {

    }

    companion object {

        fun newInstance(text: String) : TestFragment {
            val bundle = Bundle().apply {
                putString("text", text)
            }

            return TestFragment().apply { arguments = bundle }
        }

    }
}