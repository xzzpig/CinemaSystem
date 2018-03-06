package com.xzzpig.cinemasystem.bean;

import com.sun.istack.internal.NotNull;

public class User {

	String phone;
	String password;

	public User(@NotNull String phone, String password) {
		this.phone = phone;
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "User [phone=" + phone + ", password=" + password + "]";
	}

}
