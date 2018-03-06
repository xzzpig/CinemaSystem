package com.xzzpig.cinemasystem.bean;

import java.util.Date;

public class Order {
	long id;
	User user;
	Date orderTime;
	TimeTable timeTable;
	Seat seat;

	public final long getId() {
		return id;
	}

	public final User getUser() {
		return user;
	}

	public final Date getOrderTime() {
		return orderTime;
	}

	

	public final Seat getSeat() {
		return seat;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", user=" + user + ", orderTime=" + orderTime + ", timeTable=" + timeTable
				+ ", seat=" + seat + "]";
	}

	public final TimeTable getTimeTable() {
		return timeTable;
	}

	public Order(long id, User user, Date orderTime, TimeTable timeTable, Seat seat) {
		super();
		this.id = id;
		this.user = user;
		this.orderTime = orderTime;
		this.timeTable = timeTable;
		this.seat = seat;
	}

}
