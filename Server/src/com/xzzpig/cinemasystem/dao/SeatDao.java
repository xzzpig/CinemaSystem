package com.xzzpig.cinemasystem.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.xzzpig.cinemasystem.bean.Room;
import com.xzzpig.cinemasystem.bean.Seat;

public class SeatDao {
	public static Seat getSeat(int roomID, int index) throws SQLException {
		Seat[] seats = new Seat[1];
		DaoUtils.withConnection(connection -> {
			PreparedStatement statement = connection
					.prepareStatement("" + "SELECT * FROM SeatMap WHERE roomID=? AND index=?");
			statement.setInt(1, roomID);
			statement.setInt(2, index);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				seats[0] = convert(resultSet);
		});

		return seats[0];
	}

	public static Seat[] getSeatArrange(int room, int timetable) throws SQLException {
		List<Seat> seats = new LinkedList<>();
		DaoUtils.withConnection(connection -> {
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM SeatArrange WHERE roomID=? AND (timetable=? OR timetable is null)");
			statement.setInt(1, room);
			statement.setInt(2, timetable);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Seat seat = convert(resultSet);
				seat.timetable = resultSet.getInt("timetable");
				if (resultSet.wasNull())
					seat.timetable = null;
				seats.add(seat);
			}
		});
		return seats.toArray(new Seat[0]);
	}

	static Seat convert(ResultSet resultSet) throws SQLException {
		return new Seat(new Room(resultSet.getInt("roomID")), resultSet.getString("real"), resultSet.getInt("index"));
	}
}
