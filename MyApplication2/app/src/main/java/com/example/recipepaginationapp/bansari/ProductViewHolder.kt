package com.example.recipepaginationapp.bansari

import androidx.recyclerview.widget.RecyclerView
import com.bansi.myapplication.databinding.ItemProductBinding
import com.bumptech.glide.Glide
import com.example.recipepaginationapp.bansari.Product

class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product, onItemClick: (Product) -> Unit) {
        binding.productName.text = product.title
        Glide.with(binding.root).load(product.thumbnail).into(binding.productImage)
        binding.root.setOnClickListener { onItemClick(product) }
    }
}