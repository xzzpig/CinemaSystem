package com.xzzpig.cinemasystem.service;

import java.sql.SQLException;
import java.util.Date;

import com.sun.istack.internal.NotNull;
import com.xzzpig.cinemasystem.bean.Order;
import com.xzzpig.cinemasystem.bean.User;
import com.xzzpig.cinemasystem.dao.OrderDao;
import com.xzzpig.cinemasystem.service.UserService.LoginResult;

public class OrderService {

	public static enum OrderResult {
		SUCCESS, LOGINERROR, SQLERROR
	}

	public static @NotNull Order[] getUserOrders(User user) {
		try {
			Order[] orders = OrderDao.queryUserOrders(user.getPhone());
			return orders;
		} catch (SQLException e) {
			e.printStackTrace();
			return new Order[0];
		}
	}

	public static OrderResult order(Order[] orders) {
		if (UserService.login(orders[0].getUser()) != LoginResult.SUCCESS)
			return OrderResult.LOGINERROR;
		for (int i = 0; i < orders.length; i++)
			orders[i] = new Order(System.currentTimeMillis() + i, orders[i].getUser(),
					new Date(System.currentTimeMillis()), orders[i].getTimeTable(), orders[i].getSeat());
		try {
			OrderDao.insertOrders(orders);
		} catch (SQLException e) {
			e.printStackTrace();
			return OrderResult.SQLERROR;
		}
		return OrderResult.SUCCESS;
	}

	public static OrderResult remove(Order order) {
		if (UserService.login(order.getUser()) != LoginResult.SUCCESS)
			return OrderResult.LOGINERROR;
		try {
			OrderDao.deleteOrder(order.getId(), order.getUser().getPhone());
		} catch (SQLException e) {
			e.printStackTrace();
			return OrderResult.SQLERROR;
		}
		return OrderResult.SUCCESS;
	}
}
