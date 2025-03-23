package com.example.recipepaginationapp.bansari

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bansi.myapplication.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productId = intent.getIntExtra("product_id", -1)
        if (productId != -1) {
            setupViewPager(productId)
        }
    }

    private fun setupViewPager(productId: Int) {
        val pagerAdapter = ProductPagerAdapter(this, productId)
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = "Page ${position + 1}"
        }.attach()
    }
}