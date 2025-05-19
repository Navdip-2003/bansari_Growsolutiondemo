package com.bansarichothani.kotlindemo.Adepter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bansarichothani.kotlindemo.Utils.Mydata
import com.bansarichothani.kotlindemo.R

class MyAdapter(
    private val itemList: List<String>,
    private val context: Context,
    private val onItemClickListener: OnItemClickListener?) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        if (Mydata.getInstance().integers.contains(item) && itemList[position].equals(item, ignoreCase = true)) {
            holder.textView.setTextColor(Color.BLACK)
            holder.b1.visibility = View.GONE
            holder.textView.background = context.resources.getDrawable(R.drawable.selected_tag_background)
        } else {
            holder.b1.visibility = View.VISIBLE
            holder.textView.setTextColor(Color.WHITE)
            holder.b1.setImageDrawable(context.resources.getDrawable(R.drawable.bottom))
            holder.textView.background = context.resources.getDrawable(R.drawable.unselected_tag_background)
        }
        holder.textView.text = item

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val b1: ImageView = itemView.findViewById(R.id.b1)
        val l1: LinearLayout = itemView.findViewById(R.id.l1)
    }
}
