package com.example.shumetheny.ticket_booking

import com.example.shumetheny.ticket_booking.bean.Movie
import com.example.shumetheny.ticket_booking.bean.Order
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Test
import java.util.*

class KotlinTest{
    @Test
    public fun testMovie(){
        val gson = Gson()
        val httpClient = OkHttpClient()
        val result = httpClient.newCall(Request.Builder().url("http://192.168.105.57:8080/CinemaSystem/API?action=movieList").build()).execute().body()!!.string()
        val movies = gson.fromJson(result, Array<Movie>::class.java)
        println(Arrays.toString(movies))
    }

    @Test
    public fun testOrderList(){
        val gson = Gson()
        val httpClient = OkHttpClient()
        val result = httpClient.newCall(Request.Builder().url("http://192.168.105.57:8080/CinemaSystem/API?action=orderList&phone=13917704827&password=qwer1234").build()).execute().body()!!.string()
        val orders = gson.fromJson(result, Array<Order>::class.java)
        println(Arrays.toString(orders))
    }

    @Test
    public fun testTimeTable(){
        val gson = Gson()
        val httpClient = OkHttpClient()
        val result = httpClient.newCall(Request.Builder().url("http://192.168.105.57:8080/CinemaSystem/API?action=timeTableList&movie=movie1&date=${System.currentTimeMillis()}").build()).execute().body()!!.string()
        println(result)
        val timetables = gson.fromJson(result, Array<com.example.shumetheny.ticket_booking.bean.TimeTable>::class.java)
        print(Arrays.toString(timetables))
    }

    @Test
    public fun testSeatArrange(){
        val gson = Gson()
        val httpClient = OkHttpClient()
        val result = httpClient.newCall(Request.Builder().url("http://192.168.105.57:8080/CinemaSystem/API?action=seatArrange&timetable=0&room=0").build()).execute().body()!!.string()
        println(result)
        val seats = gson.fromJson(result, Array<com.example.shumetheny.ticket_booking.bean.Seat>::class.java)
        print(Arrays.toString(seats))
    }
}