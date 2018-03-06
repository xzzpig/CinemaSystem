package com.xzzpig.cinemasystem.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.xzzpig.cinemasystem.bean.Movie;
import com.xzzpig.cinemasystem.bean.TimeTable;
import com.xzzpig.cinemasystem.dao.TimeTableDao;

public class TimeTableService {
	public static TimeTable[] getTimeTable(Movie movie, Date date) {
		try {
			TimeTable[] tables = TimeTableDao.queryTimeTables(movie.getName(), date);
			for (TimeTable table : tables)
				table.movie = movie;
			return tables;
		} catch (SQLException e) {
			e.printStackTrace();
			return new TimeTable[0];
		}
	}

	@Test
	public void test() {
		// System.out.println(System.currentTimeMillis());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(Calendar.DATE, 1);
		System.out.println(calendar.getTime());
		System.out.println(Arrays.toString(getTimeTable(new Movie("movie1"), calendar.getTime())));
	}
}
