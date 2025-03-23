package com.example.recipepaginationapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.recipepaginationapp.api.RecipeApiService
import com.example.recipepaginationapp.model.Recipe
import retrofit2.HttpException
import java.io.IOException

class RecipePagingSource(private val apiService: RecipeApiService) : PagingSource<Int, Recipe>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> {
        val page = params.key ?: 0 // Default page start from 0
        val pageSize = params.loadSize

        return try {
            val response = apiService.getRecipes(limit = pageSize, skip = page)
            LoadResult.Page(
                data = response.recipes,
                prevKey = if (page == 0) null else page - pageSize,
                nextKey = if (response.recipes.isEmpty()) null else page + pageSize
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
