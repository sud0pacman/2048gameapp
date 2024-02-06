package com.example.a2048.presenter.screen.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.a2048.presenter.screen.info.pages.InfoPageOne
import com.example.a2048.presenter.screen.info.pages.InfoPageThree
import com.example.a2048.presenter.screen.info.pages.InfoPageTwo

class InfoAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> InfoPageOne()
        1 -> InfoPageTwo()
        else -> InfoPageThree()
    }


}