package com.xzzpig.cinemasystem.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.sun.istack.internal.Nullable;
import com.xzzpig.cinemasystem.bean.Movie;

public class MovieDao {
	public static Movie[] queryMovies() throws SQLException {
		List<Movie> list = new LinkedList<>();
		DaoUtils.withConnection(connection -> {
			ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM MOVIE");
			while (resultSet.next()) {
				list.add(convert(resultSet));
			}
		});
		return list.toArray(new Movie[0]);
	}

	static Movie convert(ResultSet resultSet) throws SQLException {
		return new Movie(resultSet.getString("name"), resultSet.getInt("price"), resultSet.getInt("minutes"),
				resultSet.getString("type"), resultSet.getInt("score"));
	}

	static @Nullable Movie queryMovie(String name) throws SQLException {
		Movie[] movie = new Movie[1];
		DaoUtils.withConnection(connection -> {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM Movie WHERE name=?");
			statement.setString(1, name);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				movie[0] = convert(resultSet);
			}
		});
		return movie[0];
	}
}
