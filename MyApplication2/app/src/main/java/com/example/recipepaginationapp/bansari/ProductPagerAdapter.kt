package com.example.recipepaginationapp.bansari

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProductPagerAdapter(fragmentActivity: FragmentActivity, private val productId: Int) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 10
    override fun createFragment(position: Int): Fragment {
        return ProductFragment.newInstance(productId + position)
    }
}