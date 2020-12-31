package com.zzz.auvapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zzz.common.mvvm.BaseViewModel

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2020/12/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class MainViewModel: BaseViewModel() {

    val currentIndex: LiveData<Int>
        get() = _currentIndex

    private var _currentIndex = MutableLiveData<Int>()

    init {
        _currentIndex.value = 0
    }

    fun setCurrentIndex(i: Int) {
        _currentIndex.value = i
    }
}