package com.example.myapplication.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bansi.myapplication.databinding.FragmentAllDocumentsBinding
import com.example.myapplication.ui.adapter.DocumentAdapter
import com.example.myapplication.ui.viewmodel.DocumentViewModel

abstract class BaseDocumentFragment : Fragment(), DocumentRefreshable {
    protected lateinit var binding: FragmentAllDocumentsBinding
    protected lateinit var viewModel: DocumentViewModel
    protected lateinit var adapter: DocumentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllDocumentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DocumentViewModel::class.java]
        setupAdapter()
        observeDocuments()
    }

    protected open fun setupAdapter() {
        adapter = DocumentAdapter(
            onItemClick = { document ->
                viewModel.openDocument(document.id)
            },
            onFavoriteClick = { document ->
                viewModel.toggleFavorite(document.id)
            },
            onRecentClick = { document ->
                viewModel.updateLastAccessed(document.id)
            }
        )
        binding.recyclerView.adapter = adapter
    }

    protected abstract fun observeDocuments()

    override fun refresh() {
        observeDocuments()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
} 