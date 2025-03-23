package com.example.recipepaginationapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bansi.myapplication.R
import com.bumptech.glide.Glide
import com.example.recipepaginationapp.model.Recipe
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FullScreenActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var tabAdapter: TabAdapter
    private var items: List<Recipe> = listOf()
    private var currentPosition: Int = 0
    private var selectedRecipeId: Int = 0
    private var isLoading = false  // Prevent multiple loads

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        // Retrieve data from intent
        items = intent.getSerializableExtra("recipeList") as List<Recipe>
        selectedRecipeId = intent.getIntExtra("selectedRecipeId", 0)


        // Set current item based on clicked recipe
        val currentPosition = items.indexOfFirst { it.id == selectedRecipeId }

        tabAdapter = TabAdapter(this, items)
        viewPager.adapter = tabAdapter
        if (currentPosition != -1) {
            viewPager.setCurrentItem(currentPosition, false)
        }
        viewPager.setCurrentItem(currentPosition, false)

        // Set up TabLayout with custom adapter
        setupCustomTabs()

        // Sync ViewPager2 with TabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.customView = createTabView(items.get(position).image, position == currentPosition)
        }.attach()

        // Handle Tab Clicks
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { viewPager.currentItem = it.position
                    if (it.position < 3 && !isLoading) {
                        loadMoreData()
                    }

                }
                updateTabBackground(viewPager.currentItem)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}


            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    private fun loadMoreData() {
        isLoading = true  // Prevent multiple requests

        // Simulate fetching data (Replace this with actual API call or database fetch)
        Thread {
            try {
                Thread.sleep(2000) // Simulating network delay

                // Fetch new data (You should replace this with your actual data source)
                val newItems = items

                runOnUiThread {
                    items = items + newItems
                    tabAdapter.updateData(items)
                    setupCustomTabs()
                    isLoading = false  // Reset after loading completes
                }
            } catch (e: Exception) {
                isLoading = false // Reset on failure
            }
        }.start()
    }
    private fun setupCustomTabs() {
        items.forEachIndexed { index, item ->

            val tab = tabLayout.newTab()
            tab.customView = createTabView(item.image, index == currentPosition)
            tabLayout.addTab(tab)
        }
    }

    private fun createTabView(iconRes: String, isSelected: Boolean): View {
        val view = LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null)
        val imageView = view.findViewById<ImageView>(R.id.tabIcon)
        val layout = view.findViewById<FrameLayout>(R.id.tabLayoutBg)
        Glide.with(this).load(iconRes).into(imageView)

        layout.setBackgroundResource(if (isSelected) R.drawable.se else R.drawable.un)

        return view
    }

    private fun updateTabBackground(selectedPosition: Int) {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            val view = tab?.customView
            val layout = view?.findViewById<FrameLayout>(R.id.tabLayoutBg)
            layout?.setBackgroundResource(if (i == selectedPosition) R.drawable.se else R.drawable.un)
        }
    }
}

