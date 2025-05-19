package com.bansarichothani.kotlindemo.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
//import com.androidnetworking.AndroidNetworking
//import com.androidnetworking.common.Priority
//import com.androidnetworking.error.ANError
//import com.androidnetworking.interfaces.StringRequestListener
import com.bansarichothani.kotlindemo.Utils.Mydata
import com.bansarichothani.kotlindemo.R
import com.bansarichothani.kotlindemo.Utils.Coursemodle
import com.bansarichothani.kotlindemo.Utils.Modelfilter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.io.Serializable
import java.lang.reflect.Type
import java.util.*

class MainActivity : AppCompatActivity() {
//    private lateinit var courseAdapter: CourseAdapter
    private var courseList: MutableList<Coursemodle> = mutableListOf()
    private var OthercatList: MutableList<Modelfilter> = mutableListOf()
    private lateinit var mainLayout: LinearLayout
    private lateinit var animation_view: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainLayout = findViewById(R.id.main_layout)
        animation_view = findViewById(R.id.animation_view)
        courseList = mutableListOf()
        if (isOnline()) {
            CallMain()
        } else {
            animation_view.visibility = View.GONE
            try {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Internet not available, Cross check your internet connectivity and try again later...")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setNeutralButton("Try Again") { _, _ ->
                        animation_view.visibility = View.VISIBLE

                        if (isOnline()) {
                            CallMain()
                        } else {
                            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show()
                        }
                    }.show()
            } catch (e: Exception) {
                e.message
            }
        }
    }

    fun isOnline(): Boolean {
        val conMgr = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conMgr.activeNetworkInfo

        return netInfo != null && netInfo.isConnected && netInfo.isAvailable
    }

    fun CallMain() {
        animation_view.visibility = View.VISIBLE
        Single.fromCallable {
//            try {
//                val url = "https://bit.ly/3u3sDEM"
//                AndroidNetworking.get(url)
//                    .setPriority(Priority.HIGH)
//                    .setTag("VipStore")
//                    .build()
//                    .getAsString(object : StringRequestListener {
//                        override fun onResponse(response: String) {
//                            Log.e("hhh....  ", "onResponse: $response")
//                            try {
//                                val jsonObject = JSONObject(response)
//                                val indexArray = jsonObject.getJSONObject("result").getJSONArray("index")
//                                val indexArrayStr = indexArray.toString()
//                                val gson = Gson()
//                                val listType: Type = object : TypeToken<ArrayList<Coursemodle>>() {}.type
//                                courseList = gson.fromJson(indexArrayStr, listType)
//                                val indexArray1 = jsonObject.getJSONObject("result")
//                                val collections = indexArray1.getJSONObject("collections")
//                                val smartCollections = collections.getJSONArray("smart")
//                                val indexArrayStr1 = smartCollections.toString()
//                                SetrecyclerData(indexArrayStr1, "owned", "owned")
//                                SetrecyclerData(indexArrayStr1, "recently_watched", "Recently Viewed")
//                                SetrecyclerData(indexArrayStr1, "favorites", "Favorites")
//                                SetrecyclerData(indexArrayStr1, "wishlist", "Wishlist")
//                                SetrecyclerData(indexArrayStr1, "in_progress", "In Progress")
//                            } catch (e: Exception) {
//                                e.message
//                            }
//                        }
//
//                        override fun onError(anError: ANError) {
//                            anError.printStackTrace()
//                        }
//                    })
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
            ""
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        mainLayout.setOnClickListener {
            val intent = Intent(this@MainActivity, NextdetailsActivity::class.java)
            intent.putExtra("LIST", courseList as Serializable)
            startActivity(intent)
        }
    }

    fun SetrecyclerData(response: String, ID: String, view: String) {
        mainLayout.addView(Mydata.getInstance().GenrateMain_CustomTextView(this, true, view))
        val recyclerView = RecyclerView(this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mainLayout.addView(recyclerView)
        try {
            animation_view.visibility = View.GONE
            val gson = Gson()
            val listType: Type = object : TypeToken<ArrayList<Modelfilter>>() {}.type
            OthercatList = gson.fromJson(response, listType)
            val Owendlist = mutableListOf<Int>()
            val Owendlistdata = mutableListOf<Coursemodle>()
            for (Model2 in OthercatList) {
                if (Model2.id == ID) Owendlist.addAll(Model2.courses!!)
            }
            for (id in Owendlist) {
                for (item in courseList) {
                    if (id == item.id) {
                        Owendlistdata.add(item)
                        break
                    }
                }
            }
            Collections.shuffle(OthercatList)
//            courseAdapter = CourseAdapter(Owendlistdata, this, view == "owned")
//            recyclerView.adapter = courseAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
