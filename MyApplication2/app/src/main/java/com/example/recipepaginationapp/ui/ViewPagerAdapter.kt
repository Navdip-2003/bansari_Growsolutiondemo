package com.example.recipepaginationapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bansi.myapplication.databinding.ItemViewPagerBinding
import com.bumptech.glide.Glide
import com.example.recipepaginationapp.model.Recipe

class ViewPagerFragment : Fragment() {

    private lateinit var binding: ItemViewPagerBinding

    companion object {
        private const val ARG_ITEM = "item"

        fun newInstance(item: Recipe): ViewPagerFragment {
            val fragment = ViewPagerFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_ITEM, item)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = arguments?.getParcelable<Recipe>(ARG_ITEM)
        item?.let {
            Glide.with(binding.root.context).load(item.image).into(binding.imageView)

        }
    }
}
