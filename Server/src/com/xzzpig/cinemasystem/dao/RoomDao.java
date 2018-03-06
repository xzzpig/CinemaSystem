package com.xzzpig.cinemasystem.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.xzzpig.cinemasystem.bean.Room;

public class RoomDao {
	public static Room getRoom(int id) throws SQLException {
		Room[] rooms = new Room[1];
		DaoUtils.withConnection(connection -> {
			PreparedStatement statement = connection.prepareStatement("" + "SELECT *" + "FROM Room" + "WHERE id=?");
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				rooms[0] = convert(resultSet);
		});
		return rooms[0];
	}

	static Room convert(ResultSet resultSet) throws SQLException {
		return new Room(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("count"));
	}
}
