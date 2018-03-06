package com.example.shumetheny.ticket_booking.fragment


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.example.shumetheny.ticket_booking.R
import com.example.shumetheny.ticket_booking.bean.*
import com.google.gson.Gson
import com.yydcdut.sdlv.Menu
import com.yydcdut.sdlv.MenuItem
import kotlinx.android.synthetic.main.fragment_blank.*
import kotlinx.android.synthetic.main.fragment_order.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Array.get
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class OrderFragment : Fragment() {

    val gson = Gson()
    val httpClient = OkHttpClient()

    private val userPerf by lazy { activity.getSharedPreferences("user", Context.MODE_PRIVATE) }

    val phone
        get() = userPerf.getString("phone", "") ?: ""
    val password
        get() = userPerf.getString("password", "")
    val logined:Boolean
        get() = userPerf.getBoolean("logined", false)


    object LoadLock {
    }

    inner class LoadOrderask : AsyncTask<Any, Any, Array<Order>>() {

        override fun doInBackground(vararg p0: Any?): Array<Order> {
            synchronized(LoadLock, {
                while (userPerf==null);
                if (!logined) return emptyArray()
                val result = httpClient.newCall(Request.Builder().url("http://192.168.105.57:8080/CinemaSystem/API?action=orderList&phone=${phone}&password=${password}").build()).execute().body()!!.string()
                val orders = gson.fromJson(result, Array<Order>::class.java)
//                for (order in orders) {
//                    order?.timeTable?.movie?.imgURL = R.drawable.fanghua;
//                }
                println(Arrays.toString(orders))
                return orders;
            })
        }

        override fun onPostExecute(result: Array<Order>) {
            if (orderList.size == result.size) return
            orderList.clear()
            orderList.addAll(result)
            slideListView_order.adapter = object : BaseAdapter() {
                override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View = if (p1 != null) p1 else orderList[p0].getItemView(layoutInflater)

                override fun getItem(p0: Int) = orderList[p0]

                override fun getItemId(p0: Int) = 0L

                override fun getCount(): Int = orderList.size
            }
        }
    }

    inner class DeleteOrderask(val order:Order) : AsyncTask<Any, Any, Boolean>() {

        override fun doInBackground(vararg p0: Any?): Boolean {
            synchronized(LoadLock, {
                val result = httpClient.newCall(Request.Builder().url("http://192.168.105.57:8080/CinemaSystem/API?action=removeOrder&phone=${phone}&password=${password}&order=${order.id}").build()).execute().body()!!.string()
                println(result)
                return result=="SUCCESS"
            })
        }

        override fun onPostExecute(result: Boolean) {
            if (!result) {
                orderList.add(order)
                Toast.makeText(activity,"取消失败",Toast.LENGTH_SHORT).show()
            }
            LoadOrderask().execute()
        }
    }

    val orderList = mutableListOf<Order>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slideListView_order.setMenu(Menu(false, Menu.ITEM_NOTHING).apply {
            addItem(MenuItem.Builder().apply {
                width = 200
                background = ColorDrawable(Color.RED)
                text = "取消订单"
                textSize = 16
                textColor = Color.WHITE
                direction = MenuItem.DIRECTION_RIGHT
            }.build())
        })
        slideListView_order.setOnMenuItemClickListener { v, itemPosition, buttonPosition, direction ->
            Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP.apply {
                Log.d("delete","order")
                DeleteOrderask(orderList.removeAt(itemPosition)).execute()
            }
        }
        LoadOrderask().execute()
    }

}// Required empty public constructor
