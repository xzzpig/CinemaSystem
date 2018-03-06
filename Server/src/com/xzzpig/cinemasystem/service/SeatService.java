package com.xzzpig.cinemasystem.service;

import java.sql.SQLException;
import java.util.Arrays;

import org.junit.Test;

import com.xzzpig.cinemasystem.bean.Room;
import com.xzzpig.cinemasystem.bean.Seat;
import com.xzzpig.cinemasystem.bean.TimeTable;
import com.xzzpig.cinemasystem.dao.SeatDao;

public class SeatService {
	public static Seat[] getSeatArrange(TimeTable timeTable) {
		try {
			return SeatDao.getSeatArrange(timeTable.getRoom().getId(), timeTable.getId());
		} catch (SQLException e) {
			return new Seat[0];
		}
	}

	@Test
	public void test() {
		System.out.println(Arrays.toString(getSeatArrange(new TimeTable(0, null, new Room(0), null))));
	}
}