package com.example.recipepaginationapp.bansari

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bansi.myapplication.databinding.FragmentProductBinding

class ProductFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentProductBinding.inflate(inflater, container, false)
        val productId = arguments?.getInt("product_id") ?: -1
        binding.productId.text = "Product ID: $productId"
        return binding.root
    }

    companion object {
        fun newInstance(productId: Int): ProductFragment {
            return ProductFragment().apply {
                arguments = Bundle().apply { putInt("product_id", productId) }
            }
        }
    }
}