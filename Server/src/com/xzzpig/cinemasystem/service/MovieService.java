package com.xzzpig.cinemasystem.service;

import java.sql.SQLException;

import com.sun.istack.internal.NotNull;
import com.xzzpig.cinemasystem.bean.Movie;
import com.xzzpig.cinemasystem.dao.MovieDao;

public class MovieService {
	public static @NotNull Movie[] getMovies() {
		try {
			return MovieDao.queryMovies();
		} catch (SQLException e) {
			e.printStackTrace();
			return new Movie[0];
		}
	}

	// @Test
	// public void test() {
	// System.out.println(Arrays.toString(getMovies()));
	// }
}
