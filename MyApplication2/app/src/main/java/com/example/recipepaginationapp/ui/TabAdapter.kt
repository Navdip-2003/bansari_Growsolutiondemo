package com.example.recipepaginationapp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.recipepaginationapp.model.Recipe

class TabAdapter(
    fragmentActivity: FragmentActivity,
    private var items: List<Recipe>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment {
        return ViewPagerFragment.newInstance(items[position])
    }
    fun updateData(newItems: List<Recipe>) {
        items = newItems
        notifyDataSetChanged()
    }
}
