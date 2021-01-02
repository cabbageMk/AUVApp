package com.zzz.auvapp.logic

import com.flyco.tablayout.listener.CustomTabEntity

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2020/12/31
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class TabEntity(private var title: String, private var selectedIcon: Int = 0, private var unSelectedIcon: Int = 0): CustomTabEntity {
    override fun getTabTitle() = title

    override fun getTabSelectedIcon() = selectedIcon

    override fun getTabUnselectedIcon() = unSelectedIcon
}