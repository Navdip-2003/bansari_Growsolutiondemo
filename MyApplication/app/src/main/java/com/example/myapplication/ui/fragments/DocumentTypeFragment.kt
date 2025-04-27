package com.example.myapplication.ui.fragments

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.Document
import com.example.myapplication.ui.adapter.DocumentAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DocumentTypeFragment : BaseDocumentFragment() {
    private var documentType: String? = null
    private var onDocumentClick: ((Long) -> Unit)? = null
    private var onFavoriteClick: ((Long, Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            documentType = it.getString(ARG_DOCUMENT_TYPE)
            onDocumentClick = it.getSerializable(ARG_ON_CLICK) as? (Long) -> Unit
            onFavoriteClick = it.getSerializable(ARG_ON_FAVORITE) as? (Long, Boolean) -> Unit
        }
    }

    override fun setupAdapter() {
        adapter = DocumentAdapter(
            onItemClick = { document ->
                onDocumentClick?.invoke(document.id)
            },
            onFavoriteClick = { document ->
                onFavoriteClick?.invoke(document.id, !document.isFavorite)
            },
            onRecentClick = { document ->
                viewModel.updateLastAccessed(document.id)
            }
        )
        binding.recyclerView.adapter = adapter
    }

    override fun observeDocuments() {
        viewLifecycleOwner.lifecycleScope.launch {
            documentType?.let { type ->
                viewModel.getDocumentsByType(type).collectLatest { documents ->
                    adapter.submitList(documents)
                }
            }
        }
    }

    override fun showAllFiles() {
        // For document type fragment, we don't need to implement this
        // as it's specific to the document type view
    }

    override fun showRecentFiles() {
        // For document type fragment, we don't need to implement this
        // as it's specific to the document type view
    }

    override fun showFavoriteFiles() {
        // For document type fragment, we don't need to implement this
        // as it's specific to the document type view
    }

    companion object {
        private const val ARG_DOCUMENT_TYPE = "document_type"
        private const val ARG_ON_CLICK = "on_click"
        private const val ARG_ON_FAVORITE = "on_favorite"

        fun newInstance(
            documentType: String,
            onDocumentClick: (Long) -> Unit,
            onFavoriteClick: (Long, Boolean) -> Unit
        ): DocumentTypeFragment {
            return DocumentTypeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DOCUMENT_TYPE, documentType)
                    putSerializable(ARG_ON_CLICK, onDocumentClick as java.io.Serializable)
                    putSerializable(ARG_ON_FAVORITE, onFavoriteClick as java.io.Serializable)
                }
            }
        }
    }
} 