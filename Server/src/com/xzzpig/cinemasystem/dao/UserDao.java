package com.xzzpig.cinemasystem.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.xzzpig.cinemasystem.bean.User;

public class UserDao {
	public static void insertUser(User user) throws SQLException {
		DaoUtils.withConnection((connnection) -> {
			PreparedStatement statement = connnection.prepareStatement("INSERT INTO User VALUES (?,?)");
			statement.setString(1, user.getPhone());
			statement.setString(2, user.getPassword());
			statement.executeUpdate();
		});
	}

	public static void checkUser(User user) throws SQLException {
		DaoUtils.withConnection(connnection -> {
			PreparedStatement statement = connnection
					.prepareStatement("SELECT * FROM USER WHERE phone=? AND password=?");
			statement.setString(1, user.getPhone());
			statement.setString(2, user.getPassword());
			if (!statement.executeQuery().next()) {
				throw new SQLException("check failed");
			}
		});
	}
}
