package com.example.recipepaginationapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.recipepaginationapp.api.RecipeApiService
import com.example.recipepaginationapp.model.Recipe
import com.example.recipepaginationapp.paging.RecipePagingSource
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val apiService: RecipeApiService) {

    fun getPaginatedRecipes(): Flow<PagingData<Recipe>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RecipePagingSource(apiService) }
        ).flow
    }
}
