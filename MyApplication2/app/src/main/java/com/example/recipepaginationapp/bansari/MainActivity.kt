package com.example.recipepaginationapp.bansari

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bansi.myapplication.databinding.ActivityMainBinding
import com.example.recipepaginationapp.bansari.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProductAdapter

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(ProductRepository(ApiService.create()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeProducts()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter { product ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("product_id", product.id)
            }
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeProducts() {
        viewModel.products.observe(this) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }
    }
}
