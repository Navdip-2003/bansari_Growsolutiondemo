package com.example.recipepaginationapp.bansari

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class MainViewModel(private val repository: ProductRepository) : ViewModel() {

    val products = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = { ProductPagingSource(repository.apiService) }
    ).flow.cachedIn(viewModelScope).asLiveData()
}
