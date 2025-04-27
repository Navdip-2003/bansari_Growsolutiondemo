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

class PdfFragment : BaseDocumentFragment() {
    private var isViewCreated: Boolean = false
    private var allDocuments: List<Document> = emptyList()
    private var showFavoritesOnly: Boolean = false
    private var showRecentOnly: Boolean = false

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
            adapter = this@PdfFragment.adapter
        }
    }

    override fun observeDocuments() {
        if (!isViewCreated) return

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allDocuments.collectLatest { documents ->
                allDocuments = documents
                filterPdfDocuments()
            }
        }
    }

    private fun filterPdfDocuments() {
        if (!isViewCreated) return
        val pdfDocuments = allDocuments.filter { it.type.startsWith("application/pdf") }
        val finalDocs = when {
            showRecentOnly -> pdfDocuments.filter { it.isRecent }.sortedByDescending { it.lastAccessed }
            showFavoritesOnly -> pdfDocuments.filter { it.isFavorite }
            else -> pdfDocuments
        }
        adapter.updateData(pdfDocuments)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewCreated = false
    }

    override fun showRecentFiles() {
        showRecentOnly = true
        showFavoritesOnly = false
        if (isViewCreated) {
            filterPdfDocuments()
        }
    }

    override fun showFavoriteFiles() {
        showRecentOnly = false
        showFavoritesOnly = true
        if (isViewCreated) {
            filterPdfDocuments()
        }
    }

    override fun showAllFiles() {
        showRecentOnly = false
        showFavoritesOnly = false
        if (isViewCreated) {
            filterPdfDocuments()
        }
    }

}