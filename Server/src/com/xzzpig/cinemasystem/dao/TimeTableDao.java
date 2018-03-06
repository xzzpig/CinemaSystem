package com.xzzpig.cinemasystem.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.xzzpig.cinemasystem.bean.Movie;
import com.xzzpig.cinemasystem.bean.Room;
import com.xzzpig.cinemasystem.bean.TimeTable;

public class TimeTableDao {
	public static TimeTable[] queryTimeTables(@NotNull String movie, @Nullable Date date) throws SQLException {
		if (date == null)
			date = new Date(System.currentTimeMillis());
		List<TimeTable> list = new LinkedList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date start = new Date(calendar.getTime().getTime());
		calendar.add(Calendar.DATE, 1);
		Date end = new Date(calendar.getTime().getTime());
		DaoUtils.withConnection(connection -> {
			PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM TimeTable WHERE movie=? AND startTime>=? AND startTime<?");
			statement.setString(1, movie);
			statement.setDate(2, new java.sql.Date(start.getTime()));
			statement.setDate(3, new java.sql.Date(end.getTime()));
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				list.add(convert(resultSet));
			}
		});
		return list.toArray(new TimeTable[0]);
	}

	static TimeTable convert(ResultSet resultSet) throws SQLException {
		return new TimeTable(resultSet.getInt("id"), new Movie(resultSet.getString("movie")),
				new Room(resultSet.getInt("room")), resultSet.getTimestamp("startTime"));
	}

	public static void fill(TimeTable timeTable) throws SQLException {
		DaoUtils.withConnection(connection -> {
			PreparedStatement statement = connection
					.prepareStatement("" + "SELECT *" + "FROM TimeTable" + "WHERE id=?");
			statement.setInt(1, timeTable.getId());
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				timeTable.copy(convert(resultSet));
			}
		});
	}
}
