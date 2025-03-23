package com.example.recipepaginationapp.api

import com.example.recipepaginationapp.model.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApiService {
    @GET("recipes")
    suspend fun getRecipes(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): RecipeResponse
}
