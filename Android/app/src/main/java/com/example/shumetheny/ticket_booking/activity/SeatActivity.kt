package com.example.shumetheny.ticket_booking.activity

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Looper
import android.support.transition.Visibility
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.example.shumetheny.ticket_booking.R
import com.example.shumetheny.ticket_booking.bean.Seat
import com.example.shumetheny.ticket_booking.bean.TimeTable
import com.example.shumetheny.ticket_booking.bean.getItemView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_seat.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.util.*

class SeatActivity : AppCompatActivity() {

    lateinit var timetable: TimeTable
    private val userPerf by lazy { getSharedPreferences("user", Context.MODE_PRIVATE) }

    val gson by lazy { Gson() }
    val httpClient by lazy { OkHttpClient()}


    inner class LoadSeatArrangeTask : AsyncTask<Any, Any, Array<Seat>>() {
        override fun doInBackground(vararg p0: Any?): Array<Seat> {
            val result = httpClient.newCall(Request.Builder().url("http://192.168.105.57:8080/CinemaSystem/API?action=seatArrange&timetable=${timetable.id}&room=${timetable.room?.id}").build()).execute().body()!!.string()
            val seats = gson.fromJson(result, Array<com.example.shumetheny.ticket_booking.bean.Seat>::class.java)
            print(Arrays.toString(seats))
            return seats;
        }

        override fun onPostExecute(result: Array<Seat>) {
            seatList.clear()
            seatList.addAll(result)
            val max: Int = (seatList.map { seat -> seat.index }.max() ?: 0) + 1
            selected = Array(max, { i -> false })
            listview_seat.adapter = object : BaseAdapter() {
                override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View = if (p1 != null) p1 else seatList.getItemView(layoutInflater, p0, max, selected)

                override fun getItem(p0: Int) = seatList

                override fun getItemId(p0: Int) = 0L

                override fun getCount(): Int = if (max % 12 == 0) max / 12 else max / 12 + 1
            }

        }
    }


    inner class OrderSeatTask:AsyncTask<Set<Int>,Any,Int>() {

        private val ORDERSUCCESS=0
        private val NOLOGIN=1
        private val ORDERFAILED=2
        private val NOORDER=3

        override fun doInBackground(vararg p0: Set<Int>): Int {
            val seats = p0[0]
            val phone = userPerf.getString("phone","")
            val password = userPerf.getString("password","")
            val logined = userPerf.getBoolean("logined",false)
            if(!logined) return NOLOGIN
            if(seats.size==0)return NOORDER
            val result = httpClient.newCall(Request.Builder().url("http://192.168.105.57:8080/CinemaSystem/API?action=orderSeat&phone=${phone}&password=${password}&timetable=${timetable.id}&seats=${gson.toJson(seats)}").build()).execute().body()!!.string()
            if(result=="LOGINERROR")return NOLOGIN
            else if(result=="SQLERROR")return ORDERFAILED
            return ORDERSUCCESS;
        }

        override fun onPostExecute(result: Int) {
            button_seat_commit.visibility=View.VISIBLE
            when(result){
                NOLOGIN->{
                    userPerf.edit().putBoolean("logined",false).commit()
                    Toast.makeText(this@SeatActivity,"你还没登录,请先登录",Toast.LENGTH_SHORT).show()
                }
                ORDERFAILED->{
                    Toast.makeText(this@SeatActivity,"提交失败，请稍后再试",Toast.LENGTH_SHORT).show()
                    LoadSeatArrangeTask().execute()
                }
                ORDERSUCCESS->{
                    setResult(2)
                    Toast.makeText(this@SeatActivity,"提交成功",Toast.LENGTH_SHORT).show()
                    finish()
                }
                NOORDER->{
                    Toast.makeText(this@SeatActivity,"请先选择座位再提交",Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    var selected: Array<Boolean> = arrayOf<Boolean>()

    val seatList = mutableListOf<Seat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat)
        timetable = Gson().fromJson(intent.extras.getString("timetable"), TimeTable::class.java)
        textView_seat_movie.text = timetable.movie?.name
        textView_seat_movieInfo.text = "${SimpleDateFormat("MM月dd日 HH:mm").format(timetable.startTime)} 国语2D"
        LoadSeatArrangeTask().execute()
        button_seat_commit.setOnClickListener {
            button_seat_commit.visibility = View.GONE
            OrderSeatTask().execute((0..selected.size - 1).filter { i -> selected[i] }.toSet())
        }
    }
}
