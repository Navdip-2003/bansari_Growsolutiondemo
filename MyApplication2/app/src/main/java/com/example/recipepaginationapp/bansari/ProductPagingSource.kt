package com.example.recipepaginationapp.bansari

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.recipepaginationapp.bansari.Product


class ProductPagingSource(private val apiService: ApiService) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val closestPage = state.closestPageToPosition(anchorPosition)
            closestPage?.prevKey?.plus(10) ?: closestPage?.nextKey?.minus(10)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val currentPage = params.key ?: 0
            val response = apiService.getProducts(skip = currentPage, limit = params.loadSize)

            LoadResult.Page(
                data = response.products,
                prevKey = if (currentPage == 0) null else currentPage - 10,
                nextKey = if (response.products.isEmpty()) null else currentPage + 10
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
