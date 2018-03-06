package com.xzzpig.cinemasystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import com.xzzpig.cinemasystem.bean.Movie;
import com.xzzpig.cinemasystem.bean.Order;
import com.xzzpig.cinemasystem.bean.Room;
import com.xzzpig.cinemasystem.bean.Seat;
import com.xzzpig.cinemasystem.bean.TimeTable;
import com.xzzpig.cinemasystem.bean.User;

public class OrderDao {
	public static Order[] queryUserOrders(String user) throws SQLException {
		List<Order> list = new LinkedList<>();
		DaoUtils.withConnection(connection -> {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM UserOrder WHERE user=? ORDER BY id DESC");
			statement.setString(1, user);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				list.add(convertUserOrder(resultSet));
			}
		});
		return list.toArray(new Order[0]);
	}

	static Order convertUserOrder(ResultSet resultSet) throws SQLException {

		Movie movie = new Movie(resultSet.getString("movie"));
		Room room = new Room(-1, resultSet.getString("room"), -1);
		Seat seat = new Seat(room, resultSet.getString("seat"), -1);
		TimeTable timeTable = new TimeTable(-1, movie, room, resultSet.getTimestamp("startTime"));
		User user = new User(resultSet.getString("user"), null);
		Order order = new Order(resultSet.getLong("id"), user, null, timeTable, seat);
		// Order order = new Order(resultSet.getInt("id"), new
		// User(resultSet.getString("user"), null),
		// resultSet.getTimestamp("orderTime"), new TimeTable(resultSet.getInt("id")),
		// new Seat(null, null, resultSet.getInt("seat")));
		return order;
	}

	public static void insertOrders(Order[] orders) throws SQLException {
		DaoUtils.withConnection(conn->{
			for(Order order:orders)insertOrder(order, conn);
		});
	}
	
	public static void deleteOrder(long id,String user) throws SQLException {
		DaoUtils.withConnection(connection->{
			PreparedStatement statement = connection.prepareStatement("DELETE FROM `order` WHERE `user`=? AND id=?");
			statement.setString(1, user);
			statement.setLong(2, id);
			if(statement.executeUpdate()==0)throw new SQLException("error user and id in order delete");
		});
	}

	static void insertOrder(Order order, Connection connection) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("INSERT INTO `Order` VALUES(?,?,?,?,?)");
		statement.setLong(1, order.getId());
		statement.setString(2, order.getUser().getPhone());
		statement.setTimestamp(3, new Timestamp(order.getOrderTime().getTime()));
		// statement.setDate(3, new Date(order.getOrderTime().getTime()));
		statement.setInt(4, order.getTimeTable().getId());
		statement.setInt(5, order.getSeat().getIndex());
		statement.execute();
	}

}
