package com.example.shumetheny.ticket_booking.fragment


import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import com.example.shumetheny.ticket_booking.R
import com.example.shumetheny.ticket_booking.bean.Movie
import com.example.shumetheny.ticket_booking.bean.TimeTable
import com.example.shumetheny.ticket_booking.bean.getItemView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_time_table.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*


@SuppressLint("ValidFragment")
/**
 * A simple [Fragment] subclass.
 */
class TimeTableFragment(val index: Int) : Fragment() {

    val timetableList = mutableListOf<TimeTable>()
    lateinit var movie: Movie
    val date: Date by lazy {
        val calendar = Calendar.getInstance()
        calendar.time = Date(System.currentTimeMillis())
        calendar.add(Calendar.DATE, index)
        calendar.time
    }


    inner class LoadTimeTableTask : AsyncTask<Any, Any, Array<TimeTable>>() {
        override fun doInBackground(vararg p0: Any?): Array<TimeTable> {
            val gson = Gson()
            val httpClient = OkHttpClient()
            val result = httpClient.newCall(Request.Builder().url("http://192.168.105.57:8080/CinemaSystem/API?action=timeTableList&movie=${movie.name}&date=${date.time}").build()).execute().body()!!.string()
            val timetables = gson.fromJson(result, Array<com.example.shumetheny.ticket_booking.bean.TimeTable>::class.java)
            timetables.forEach { timeTable -> timeTable.movie = movie }
            print(timetables)
            return timetables;
        }

        override fun onPostExecute(result: Array<TimeTable>) {
            timetableList.clear()
            timetableList.addAll(result)
            listView_timetable.adapter = object : BaseAdapter() {
                override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View = if (p1 != null) p1 else timetableList[p0].getItemView(layoutInflater,this@TimeTableFragment)

                override fun getItem(p0: Int) = timetableList[p0]

                override fun getItemId(p0: Int) = 0L

                override fun getCount(): Int = timetableList.size
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_time_table, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView_timetable.adapter = object : BaseAdapter() {
            override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View = if (p1 != null) p1 else timetableList[p0].getItemView(layoutInflater, this@TimeTableFragment)

            override fun getItem(p0: Int) = timetableList[p0]

            override fun getItemId(p0: Int) = 0L

            override fun getCount(): Int = timetableList.size
        }
        movie = Gson().fromJson(arguments.getString("movie"), Movie::class.java)
        LoadTimeTableTask().execute()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1){
            if(resultCode==2){
                activity.setResult(3)
                activity.finish()
            }
        }
    }
}// Required empty public constructor
