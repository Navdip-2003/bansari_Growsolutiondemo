package com.example.recipepaginationapp.bansari

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.recipepaginationapp.bansari.Product
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductRepository(val apiService: ApiService) {

    fun getProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,  // Number of items per page
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductPagingSource(apiService) }
        ).flow
    }
}