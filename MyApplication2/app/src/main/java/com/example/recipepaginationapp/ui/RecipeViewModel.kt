package com.example.recipepaginationapp.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.recipepaginationapp.model.Recipe
import com.example.recipepaginationapp.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class RecipeViewModel(repository: RecipeRepository) : ViewModel() {
    val recipes: Flow<PagingData<Recipe>> = repository.getPaginatedRecipes().cachedIn(viewModelScope)
}
