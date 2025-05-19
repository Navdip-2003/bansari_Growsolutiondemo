package com.bansarichothani.kotlindemo.Adepter

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bansarichothani.kotlindemo.Utils.Mydata

class CustomAdapter(private val context: Context, private val items: List<String>) : BaseAdapter() {

    private var selectedIndex = -1

    override fun getCount(): Int {
        return items.size + 1
    }

    override fun getItem(position: Int): Any {
        return items[position] + 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
            holder = ViewHolder()
            holder.textView = convertView.findViewById(android.R.id.text1)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        if (items.size > position) {
            holder.textView.text = items[position]
            if ((Mydata.getInstance().selectedSkill).contains(items[position]) || (Mydata.getInstance().selectedStyle).contains(items[position])
                ||(Mydata.getInstance().selectedEducator).contains(items[position]) ||(Mydata.getInstance().selectedCurricule).contains(items[position])) {
                holder.textView.setTextColor(Color.BLUE)
            } else {
                holder.textView.setTextColor(Color.BLACK)
            }

        } else {
            holder.textView.setTextColor(Color.RED)
            holder.textView.gravity = Gravity.CENTER
            holder.textView.text = "Cancel"
        }

        return convertView!!
    }

    private class ViewHolder {
        lateinit var textView: TextView
    }
}
