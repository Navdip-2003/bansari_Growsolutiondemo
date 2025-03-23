package com.example.recipepaginationapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bansi.myapplication.databinding.ItemFullscreenBinding
import com.bumptech.glide.Glide
import com.example.recipepaginationapp.model.Recipe

class FullscreenPagerAdapter(private val recipes: List<Recipe>) :
    RecyclerView.Adapter<FullscreenPagerAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemFullscreenBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.tvRecipeName.text = recipe.name
            Glide.with(binding.root.context).load(recipe.image).into(binding.ivRecipeImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFullscreenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size
}
