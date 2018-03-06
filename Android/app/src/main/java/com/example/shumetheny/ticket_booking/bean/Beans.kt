package com.example.shumetheny.ticket_booking.bean

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.shumetheny.ticket_booking.R
import com.example.shumetheny.ticket_booking.activity.SeatActivity
import com.example.shumetheny.ticket_booking.fragment.TimeTableFragment
import com.google.gson.Gson
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by shumetheny on 2018/1/8.
 */

data class Movie(val name: String?, var imgURL: String? = null, val type: String = "unknown", val score: Short = -1, val price: Int = -1, val minutes: Int = -1)

fun Movie.scoreString():String="" + (score.toFloat().div(10).let { if(it==0f)"暂无评分" else it })

fun Movie.getItemView(inflater: LayoutInflater): View {
    val view = inflater.inflate(R.layout.listview_item_cinema, null)
    view.findViewById<TextView>(R.id.textView_cinema_name).text = name;
    view.findViewById<TextView>(R.id.textView_cinema_type).text = type
    view.findViewById<TextView>(R.id.textView_cinema_score).text = scoreString()
    return view
}

data class Room(val id: Int = -1, val name: String? = null, val count: Int = -1)

data class TimeTable(val id: Int = -1, var movie: Movie? = null, val room: Room? = null, val startTime: Date? = null)

fun TimeTable.getItemView(inflater: LayoutInflater, timeTableFragment: TimeTableFragment): View {
    val timetable = this
    val view = inflater.inflate(R.layout.listview_item_timetable, null)
    Log.d("startTime", "" + startTime)
    view.findViewById<TextView>(R.id.textView_timetable_time).text = SimpleDateFormat("HH:mm").format(startTime)
    view.findViewById<TextView>(R.id.textView_timetable_price).text = "${movie?.price}" + "$"
    view.findViewById<Button>(R.id.button_timetable_buy).setOnClickListener {
        timeTableFragment.startActivityForResult(Intent().apply { setClass(view.context, SeatActivity::class.java);putExtra("timetable", Gson().toJson(timetable)) }, 1)
    }
    return view;
}

data class Seat(val room: Room? = null, val real: String? = null, val index: Int = -1, val timetable: Int? = null)

fun List<Seat>.getItemView(inflater: LayoutInflater, index: Int, max: Int, selected: Array<Boolean>): View {
    val view = inflater.inflate(R.layout.listview_item_seat, null)
    val start = index * 12
    val end = (index + 1) * 12 - 1
    for (seat in this) {
        if (seat.index !in start..end) continue
        if (seat.timetable == null) continue
        view.findViewWithTag<CheckBox>((seat.index % 12).toString()).isEnabled = false
    }
    for (i in 0..11) {
        val checkBox = view.findViewWithTag<CheckBox>(i.toString())
        val loc = (i + index * 12)
        checkBox.setOnClickListener { selected[loc] = checkBox.isChecked }
        if ((i + index * 12) >= max) checkBox.isEnabled = false
    }
    return view
}

data class Order(val id: Long = -1, val user: User? = null, val orderTime: Date? = null, val timeTable: TimeTable? = null, val seat: Seat? = null)

fun Order.getItemView(inflater: LayoutInflater): View {
    val view = inflater.inflate(R.layout.listview_item_order, null)
    val imgURL = timeTable?.movie?.imgURL
    if (imgURL != null)
        Glide.with(inflater.context).load(imgURL).into(view.findViewById(R.id.imageView_item_order_cinema))
    else
        view.findViewById<ImageView>(R.id.imageView_item_order_cinema).setImageResource(R.drawable.fanghua);
    view.findViewById<TextView>(R.id.textView_item_order_id).text = "订单编号：" + id
    view.findViewById<TextView>(R.id.textView_item_order_cinema).text = timeTable?.movie?.name
    view.findViewById<TextView>(R.id.textView_item_order_other).text = SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss").format(timeTable?.startTime) + "\n" + timeTable?.room?.name + " " + seat?.real
    return view
}

data class User(val phone: String, val password: String? = null)