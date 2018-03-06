package com.example.shumetheny.ticket_booking.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.shumetheny.ticket_booking.R
import com.example.shumetheny.ticket_booking.bean.Movie
import com.example.shumetheny.ticket_booking.bean.scoreString
import com.example.shumetheny.ticket_booking.fragment.TimeTableFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_time_table.*
import java.text.SimpleDateFormat
import java.util.*

class TimeTableActivity : AppCompatActivity() {

    lateinit var movie:Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table)
        movie = Gson().fromJson(intent.extras.getString("movie"),Movie::class.java)
        viewpager_timetable.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int)=TimeTableFragment(position).apply { arguments=Bundle().apply { putString("movie",intent.extras.getString("movie")) }}
            override fun getCount(): Int =5
        }
        if (movie.imgURL != null)
            Glide.with(this).load(movie.imgURL).into(imageView_timetable_movie)
        else
            imageView_timetable_movie.setImageResource(R.drawable.fanghua);
        val calendar = Calendar.getInstance().apply { time=Date(System.currentTimeMillis()) }
        val format = SimpleDateFormat("MM月dd日")
        for(i in 0..4){
            ll_timeTable.findViewWithTag<TextView>(i.toString())?.apply {
                setOnClickListener { view -> viewpager_timetable.setCurrentItem(i,true) }
                text=format.format(calendar.time)
                calendar.add(Calendar.DATE,1)
            }
        }
        textView_timetable_movie.text=movie.name
        textView_timetable_movieInfo.text="${movie.minutes}分钟 | ${movie.type} | ${movie.scoreString()}"
    }
}
