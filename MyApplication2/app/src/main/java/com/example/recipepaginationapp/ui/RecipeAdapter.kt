package com.example.recipepaginationapp.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.paging.PagingDataAdapter
import com.bansi.myapplication.databinding.ItemRecipeBinding
import com.bumptech.glide.Glide
import com.example.recipepaginationapp.model.Recipe

class RecipeAdapter(private val onItemClick: (Recipe) -> Unit) :
    PagingDataAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(COMPARATOR) {

    class RecipeViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe, onItemClick: (Recipe) -> Unit) {
            Glide.with(binding.root.context).load(recipe.image).into(binding.ivRecipeImage)
            binding.root.setOnClickListener { onItemClick(recipe) } // Click Listener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, onItemClick) }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean = oldItem == newItem
        }
    }
}

