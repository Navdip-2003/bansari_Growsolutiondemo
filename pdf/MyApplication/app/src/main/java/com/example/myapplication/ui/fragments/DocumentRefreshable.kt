package com.example.myapplication.ui.fragments

interface DocumentRefreshable {
    fun showAllFiles()
    fun showRecentFiles()
    fun showFavoriteFiles()
    fun refresh()
} 