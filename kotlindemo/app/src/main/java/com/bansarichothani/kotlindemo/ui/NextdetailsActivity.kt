package com.bansarichothani.kotlindemo.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bansarichothani.kotlindemo.Adepter.MyAdapter
import com.bansarichothani.kotlindemo.Adepter.CustomAdapter
import com.bansarichothani.kotlindemo.Utils.Mydata
import com.bansarichothani.kotlindemo.R
import com.bansarichothani.kotlindemo.Utils.Coursemodle
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.ArrayList
import java.util.HashSet

class NextdetailsActivity : AppCompatActivity(), MyAdapter.OnItemClickListener {
    private var selectedOwned = 0
    private var filteredList: MutableList<Coursemodle> = ArrayList()
    private var courseList: List<Coursemodle> = ArrayList()
    private lateinit var recyclerview: RecyclerView
//    private lateinit var courseAdapter: CourseAdapter
    private lateinit var myAdapter: MyAdapter
    private var title = ArrayList<String>()
    private lateinit var listView: ListView
    private lateinit var values: ArrayList<Int>
    private lateinit var back: ImageView
    private lateinit var cleareallfilter: LinearLayout
    private fun removeDuplicates(list: List<String>): List<String> {
        val set = HashSet<String>()
        for (s in list) {
            if (s.isNotEmpty()) {
                set.add(s)
            }
        }
        return ArrayList(set)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)
        courseList = intent.getSerializableExtra("LIST") as ArrayList<Coursemodle>
        recyclerview = findViewById(R.id.recyclerview)
        cleareallfilter = findViewById(R.id.dele)
        back = findViewById(R.id.back)
        titleset()
        val recyclerView1: RecyclerView = findViewById(R.id.container)
        recyclerView1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        myAdapter = MyAdapter(title, this, this)
        recyclerView1.adapter = myAdapter
        back.setOnClickListener { onBackPressed() }

        recyclerview.layoutManager = GridLayoutManager(this, 2)
//        courseAdapter = CourseAdapter(filteredList, this, false)
//        recyclerview.adapt/er = courseAdapter

        Mydata.getInstance().textViewList = ArrayList()
        Mydata.getInstance().integers = ArrayList()
        resetValues()
        filter("")
        values = ArrayList()
        cleareallfilter.setOnClickListener {
            resetValues()
            myAdapter.notifyDataSetChanged()
        }

        val searchView: EditText = findViewById(R.id.search_view)
        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun showFilterDialog(i: Int) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.filter_bottom_sheet, findViewById(R.id.bottom_sheet_container))
        var strings: List<String> = ArrayList()
        when (i) {
            1 -> strings = removeDuplicates(Mydata.getInstance().getSkiltaglList(courseList))
            2 -> strings = removeDuplicates(Mydata.getInstance().getCurrSkiltaglList(courseList))
            3 -> strings = removeDuplicates(Mydata.getInstance().getStyleSkiltaglList(courseList))
            4 -> strings =
                removeDuplicates(Mydata.getInstance().getEducatorSkiltaglList(courseList))
        }

        listView = bottomSheetView.findViewById(R.id.listView)
        val adapter = CustomAdapter(this, strings)
        listView.adapter = adapter

        val finalStrings = strings
        listView.setOnItemClickListener { _, _, position, _ ->
            if (finalStrings.size <= position) {
                Mydata.getInstance().integers.remove(title[i])
                when (i) {
                    1 -> {
                        Mydata.getInstance().selectedSkill = ""
                        title[i] = "Skill :"
                    }

                    2 -> {
                        Mydata.getInstance().selectedCurricule = ""
                        title[i] = "Curriculum :"
                    }

                    3 -> {
                        Mydata.getInstance().selectedStyle = ""
                        title[i] = "Style :"
                    }

                    4 -> {
                        Mydata.getInstance().selectedEducator = ""
                        title[i] = "Educator :"
                    }
                }
                myAdapter.notifyDataSetChanged()
                filter("")
            } else {
                val clickedItem = finalStrings[position]
                title[i] = clickedItem
                Mydata.getInstance().integers.add(clickedItem)
                myAdapter.notifyDataSetChanged()
                when (i) {
                    1 -> Mydata.getInstance().selectedSkill = clickedItem
                    2 -> Mydata.getInstance().selectedCurricule = clickedItem
                    3 -> Mydata.getInstance().selectedStyle = clickedItem
                    4 -> Mydata.getInstance().selectedEducator = clickedItem
                }
                filter("")
            }
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun incrementValue(index: Int) {
        if (index == 0) {
            if (selectedOwned == 1) {
                selectedOwned = 0
                Mydata.getInstance().integers.remove("Owned OFF")
                title[0] = "Owned Off :"
            } else {
                title[0] = "Owned ON :"
                Mydata.getInstance().integers.add("Owned ON")
                selectedOwned = 1
            }
            filter("")
            myAdapter.notifyDataSetChanged()
            return
        }
        showFilterDialog(index)
    }
    private fun titleset() {
        title.clear()
        title.add("Owned Off: ")
        title.add("Skill : ")
        title.add("Curriculum : ")
        title.add("Style : ")
        title.add("Educator : ")
    }

    private fun resetValues() {
        Mydata.getInstance().integers.clear()
        titleset()
        Mydata.getInstance().selectedSkill = ""
        Mydata.getInstance().selectedCurricule = ""
        Mydata.getInstance().selectedStyle = ""
        Mydata.getInstance().selectedEducator = ""
        selectedOwned = 0
        filter("")
        myAdapter.notifyDataSetChanged()
        cleareallfilter.visibility = View.GONE
    }

    private fun filter(text: String) {
        filteredList.clear()
        Log.e("h....", "filter: " + Mydata.getInstance().selectedCurricule)
        Log.e("h....", "filter:selectedSkill " + Mydata.getInstance().selectedSkill)
        if (Mydata.getInstance().integers.isNotEmpty()) {
            cleareallfilter.visibility = View.VISIBLE
        } else {
            cleareallfilter.visibility = View.GONE
        }
        for (course in courseList) {
            val matches = (text.isEmpty() || course.title?.toLowerCase()
                ?.contains(text.toLowerCase()) == true) &&
                    (Mydata.getInstance().selectedSkill.isEmpty() || course.skillTags?.contains(
                        Mydata.getInstance().selectedSkill
                    ) ?: true) &&
                    (Mydata.getInstance().selectedEducator.isEmpty() || course.educator.equals(
                        Mydata.getInstance().selectedEducator,
                        ignoreCase = true
                    )) &&
                    (Mydata.getInstance().selectedStyle.isEmpty() || course.educator.equals(
                        Mydata.getInstance().selectedStyle,
                        ignoreCase = true
                    )) &&
                    (Mydata.getInstance().selectedCurricule.isEmpty() || course.educator.equals(
                        Mydata.getInstance().selectedCurricule,
                        ignoreCase = true
                    )) &&
                    (course.owned == selectedOwned)

            if (matches) {
                filteredList.add(course)
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No courses found for given search", Toast.LENGTH_SHORT).show()
        }
//        courseAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {
        if (position == title.size + 1) {
            resetValues()
        } else {
            incrementValue(position)
        }
    }
}
