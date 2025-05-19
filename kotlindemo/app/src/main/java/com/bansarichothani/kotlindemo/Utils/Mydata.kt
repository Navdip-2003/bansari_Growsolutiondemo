package com.bansarichothani.kotlindemo.Utils

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class Mydata {
    var SkiltaglList: MutableList<String> = ArrayList()
    var selectedSkill: String = ""
    var selectedEducator: String = ""
    var selectedCurricule: String = ""
    var selectedStyle: String = ""
    var textViewList: ArrayList<TextView> = ArrayList()
    var integers: ArrayList<String> = ArrayList()
    companion object {
        private var instance: Mydata? = null
        fun getInstance(): Mydata {
            if (instance == null) {
                instance = Mydata()
            }
            return instance!!
        }
    }

    fun getSkiltaglList(examples: List<Coursemodle>): List<String> {
        SkiltaglList = ArrayList()
        for (example in examples) {
            println(example.skillTags?.get(0))
            example.skillTags?.let { SkiltaglList.addAll(it) }
        }
        return SkiltaglList
    }

    fun getCurrSkiltaglList(examples: List<Coursemodle>): List<String> {
        SkiltaglList = ArrayList()
        for (example in examples) {
            example.curriculumTags?.let { (SkiltaglList as ArrayList<String>).addAll(it) }
        }
        return SkiltaglList
    }
    fun getEducatorSkiltaglList(examples: List<Coursemodle>): List<String> {
        SkiltaglList = java.util.ArrayList()

        for ((_, _, _, _, _, _, _, _, _, _, _, _, educator) in examples) {
            SkiltaglList.add(educator!!)
        }
        return SkiltaglList
    }
    fun getStyleSkiltaglList(examples: List<Coursemodle>): List<String> {
        SkiltaglList = ArrayList()
        for (example in examples) {
            println(example.styleTags?.get(0))
            example.styleTags?.let { (SkiltaglList as ArrayList<String>).addAll(it) }
        }
        return SkiltaglList
    }

    fun GenrateMain_CustomTextView(activity: Activity?, Gravity1: Boolean, s: String?): ConstraintLayout {
        val linearLayout = ConstraintLayout(activity!!)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val textView = TextView(activity)
        textView.text = s
        textView.setPadding(25, 16, 0, 4)
        textView.textSize = 14f
        textView.gravity = if (Gravity1) Gravity.START else Gravity.END
        textView.setTextColor(Color.WHITE)
        textView.layoutParams = layoutParams

        val textView1 = TextView(activity)
        textView1.text = "View All >>"
        textView1.setPadding(25, 16, 0, 4)
        textView1.textSize = 14f
        textView1.gravity = Gravity.END
        textView1.setTextColor(Color.WHITE)
        textView1.layoutParams = layoutParams

        linearLayout.addView(textView)
        linearLayout.addView(textView1)
        return linearLayout
    }


}
