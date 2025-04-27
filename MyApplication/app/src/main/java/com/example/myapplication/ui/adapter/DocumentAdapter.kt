package com.example.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bansi.myapplication.R
import com.bansi.myapplication.databinding.ItemDocumentBinding
import com.example.myapplication.data.Document
import java.text.SimpleDateFormat
import java.util.Locale

class DocumentAdapter(
    private val onItemClick: (Document) -> Unit,
    private val onFavoriteClick: (Document) -> Unit,
    private val onRecentClick: (Document) -> Unit
) : ListAdapter<Document, DocumentAdapter.DocumentViewHolder>(DocumentDiffCallback()) {

    private val documents = mutableListOf<Document>()
    private val documentIds = mutableSetOf<Long>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val binding = ItemDocumentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DocumentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        holder.bind(documents[position])
    }

    override fun getItemCount(): Int = documents.size

    fun updateData(newDocuments: List<Document>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = documents.size
            override fun getNewListSize(): Int = newDocuments.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return documents[oldItemPosition].id == newDocuments[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return documents[oldItemPosition] == newDocuments[newItemPosition]
            }
        }

        // Clear existing data
        documents.clear()
        documentIds.clear()
        documents.addAll(newDocuments)
        notifyDataSetChanged()

        // Add only unique documents
//        newDocuments.forEach { document ->
//            if (document.id !in documentIds) {
//                documents.add(document)
//                documentIds.add(document.id)
//            }
//        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class DocumentViewHolder(
        private val binding: ItemDocumentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        private val clickAnimation = AnimationUtils.loadAnimation(binding.root.context, R.anim.item_click_animation)

        init {
          /*  binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    binding.root.startAnimation(clickAnimation)
                    onItemClick(documents[position])
                }
            }*/

            binding.favoriteButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    binding.favoriteButton.startAnimation(clickAnimation)
                    onFavoriteClick(documents[position])
                }
            }

            binding.recentButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    binding.recentButton.startAnimation(clickAnimation)
                    onRecentClick(documents[position])
                }
            }
        }

        fun bind(document: Document) {
            binding.apply {
                fileName.text = document.name
                fileType.text = document.type
                fileSize.text = formatFileSize(document.size)
                lastModified.text = dateFormat.format(document.lastModified)
                
                // Update favorite button state with appropriate drawables
                favoriteButton.isSelected = document.isFavorite
                favoriteButton.setImageResource(
                    if (document.isFavorite) R.drawable.ic_favorite
                    else R.drawable.ic_unfavorite
                )
                
                recentButton.isSelected = document.isRecent
            }
        }

        private fun formatFileSize(size: Long): String {
            val units = arrayOf("B", "KB", "MB", "GB")
            var fileSize = size.toDouble()
            var unitIndex = 0

            while (fileSize > 1024 && unitIndex < units.size - 1) {
                fileSize /= 1024
                unitIndex++
            }

            return String.format("%.1f %s", fileSize, units[unitIndex])
        }
    }

    private class DocumentDiffCallback : DiffUtil.ItemCallback<Document>() {
        override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem == newItem
        }
    }
} 