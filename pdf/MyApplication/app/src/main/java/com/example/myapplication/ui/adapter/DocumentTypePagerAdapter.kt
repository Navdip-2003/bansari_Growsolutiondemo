package com.example.myapplication.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.ui.fragments.*

class DocumentTypePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = DOCUMENT_TYPES.size

    override fun createFragment(position: Int): Fragment {
        return when (DOCUMENT_TYPES[position]) {
            "ALL" -> AllDocumentsFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }

    companion object {
        val DOCUMENT_TYPES = listOf(
            "ALL",
        )
    }
} 