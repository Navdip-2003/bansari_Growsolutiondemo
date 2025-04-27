package com.example.myapplication.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.Document
import com.example.myapplication.ui.adapter.DocumentAdapter
import com.example.myapplication.viewmodel.DocumentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AllDocumentsFragment : BaseDocumentFragment() {
    private var currentFilter: String = "ALL"
    private var showRecentOnly: Boolean = false
    private var showFavoritesOnly: Boolean = false
    private var isViewCreated: Boolean = false
    private var allDocuments: List<Document> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        setupRefreshButton()
        setupRecyclerView()
        observeDocuments()
    }

    private fun setupRefreshButton() {
        binding.refreshButton.setOnClickListener {
            viewModel.refreshDocuments()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AllDocumentsFragment.adapter
        }
    }

    override fun observeDocuments() {
        if (!isViewCreated) return

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allDocuments.collectLatest { documents ->
                allDocuments = documents
                applyFilters()
            }
        }
    }

    private fun applyFilters() {
        if (!isViewCreated) return

        val filteredDocs = when (currentFilter) {
            "ALL" -> allDocuments
            "PDF" -> allDocuments.filter { it.type.startsWith("application/pdf") }
            "WORD" -> allDocuments.filter { 
                it.type.startsWith("application/msword") || 
                it.type.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml")
            }
            "HTML" -> allDocuments.filter { it.type.startsWith("text/html") }
            "TXT" -> allDocuments.filter { it.type.startsWith("text/plain") }
            "OTHER" -> allDocuments.filter { doc ->
                !doc.type.startsWith("application/pdf") &&
                !doc.type.startsWith("application/msword") &&
                !doc.type.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml") &&
                !doc.type.startsWith("text/html") &&
                !doc.type.startsWith("text/plain")
            }
            else -> allDocuments
        }

        val finalDocs = when {
            showRecentOnly -> filteredDocs.filter { it.isRecent }.sortedByDescending { it.lastAccessed }
            showFavoritesOnly -> filteredDocs.filter { it.isFavorite }
            else -> filteredDocs
        }

        adapter.updateData(finalDocs)
    }

    fun setFilter(filter: String) {
        currentFilter = filter
        if (isViewCreated) {
            applyFilters()
        }
    }

    override fun showRecentFiles() {
        showRecentOnly = true
        showFavoritesOnly = false
        if (isViewCreated) {
            applyFilters()
        }
    }

    override fun showFavoriteFiles() {
        showRecentOnly = false
        showFavoritesOnly = true
        if (isViewCreated) {
            applyFilters()
        }
    }

    override fun showAllFiles() {
        showRecentOnly = false
        showFavoritesOnly = false
        if (isViewCreated) {
            applyFilters()
        }
    }

    fun showPdfFiles() {
        currentFilter = "PDF"
        showRecentOnly = false
        showFavoritesOnly = false
        if (isViewCreated) {
            applyFilters()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewCreated = false
    }
} 