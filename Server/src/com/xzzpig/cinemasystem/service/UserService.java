package com.xzzpig.cinemasystem.service;

import java.sql.SQLException;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.sun.istack.internal.NotNull;
import com.xzzpig.cinemasystem.bean.User;
import com.xzzpig.cinemasystem.dao.UserDao;

public class UserService {

	public static enum RegResult {
		SUCCESS, PHONEERROR, PASSWORDERROR, SQLERROR, REGEDERROR
	}

	public static enum LoginResult {
		SUCCESS, FAILED
	}

	public static @NotNull RegResult regist(@NotNull User user) {
		if (user.getPhone() == null || !user.getPhone().matches("(13[0-9]|15[7-9]|153|156|18[7-9])[0-9]{8}"))
			return RegResult.PHONEERROR;
		if (user.getPassword() == null || !user.getPassword().matches("[A-Za-z0-9]{6,10}"))
			return RegResult.PASSWORDERROR;
		try {
			UserDao.insertUser(user);
		} catch (SQLException e) {
			if (e instanceof MySQLIntegrityConstraintViolationException)
				return RegResult.REGEDERROR;
			e.printStackTrace();
			return RegResult.SQLERROR;
		}
		return RegResult.SUCCESS;
	}

	public static @NotNull LoginResult login(@NotNull User user) {
		try {
			UserDao.checkUser(user);
		} catch (SQLException e) {
			return LoginResult.FAILED;
		}
		return LoginResult.SUCCESS;
	}
}
