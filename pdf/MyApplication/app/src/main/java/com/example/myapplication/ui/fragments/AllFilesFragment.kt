package com.example.myapplication.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bansi.myapplication.R
import com.bansi.myapplication.databinding.FragmentAllFilesBinding
import com.example.myapplication.ui.adapter.DocumentTypePagerAdapter
import com.example.myapplication.viewmodel.DocumentViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AllFilesFragment : Fragment() {
    private var _binding: FragmentAllFilesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DocumentViewModel by viewModels()
    private lateinit var documentsFragment: AllDocumentsFragment
    private lateinit var pdfFragment: PdfFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            setupFragments()
        } else {
            documentsFragment = childFragmentManager.findFragmentById(R.id.fragmentContainer) as AllDocumentsFragment
        }
        observeLoadingState()
    }

    private fun setupFragments() {
        documentsFragment = AllDocumentsFragment()
        pdfFragment = PdfFragment()

        // Initially show AllDocumentsFragment
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, documentsFragment)
            .commitNow()
    }

    private fun observeLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    fun showAllFiles() {
        if (::documentsFragment.isInitialized) {
            documentsFragment.showAllFiles()
        }
    }

    fun showRecentFiles() {
        if (::documentsFragment.isInitialized) {
            documentsFragment.showRecentFiles()
        }
    }

    fun showFavoriteFiles() {
        if (::documentsFragment.isInitialized) {
            documentsFragment.showFavoriteFiles()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 