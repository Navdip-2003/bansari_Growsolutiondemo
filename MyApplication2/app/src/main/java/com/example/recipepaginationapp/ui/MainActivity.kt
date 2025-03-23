package com.example.recipepaginationapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bansi.myapplication.R
import com.example.recipepaginationapp.api.RecipeApiService
import com.example.recipepaginationapp.api.RetrofitClient
import com.example.recipepaginationapp.repository.RecipeRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RecipeAdapter
    private lateinit var viewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)

        val apiService = RetrofitClient.instance.create(RecipeApiService::class.java)
        val repository = RecipeRepository(apiService)
        viewModel = RecipeViewModel(repository)

        adapter = RecipeAdapter { recipe ->
            val intent = Intent(this, FullScreenActivity::class.java)
            intent.putExtra("recipeList", ArrayList(adapter.snapshot().items))
            intent.putExtra("selectedRecipeId", recipe.id)
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.recipes.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }
}

