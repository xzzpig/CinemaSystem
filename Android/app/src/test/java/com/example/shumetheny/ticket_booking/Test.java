package com.example.shumetheny.ticket_booking;

import com.example.shumetheny.ticket_booking.bean.Movie;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by shumetheny on 2018/1/10.
 */

public class Test {
    @org.junit.Test
    public void test() throws IOException {
        Gson gson = new Gson();
        OkHttpClient httpClient = new OkHttpClient();
        String result = httpClient.newCall(new Request.Builder().url("http://192.168.105.57:8080/CinemaSystem/API?action=movieList").build()).execute().body().string();
        System.out.println(Arrays.toString(gson.fromJson(result, Movie[].class)));
    }
}
