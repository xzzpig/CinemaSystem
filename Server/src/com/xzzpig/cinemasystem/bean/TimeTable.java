package com.xzzpig.cinemasystem.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTable {
	int id;
	public Movie movie;
	Room room;
	Date startTime;

	public TimeTable(int id, Movie movie, Room room, Date startTime) {
		super();
		this.id = id;
		this.movie = movie;
		this.room = room;
		this.startTime = startTime;
	}

	public TimeTable(int id) {
		this(id, null, null, null);
	}

	public final int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "TimeTable [movie=" + movie + ", room=" + room + ", startTime="
				+ new SimpleDateFormat("MM-dd HH:mm").format(startTime) + "]";
	}

	public final Movie getMovie() {
		return movie;
	}

	public final Room getRoom() {
		return room;
	}

	public final Date getStartTime() {
		return startTime;
	}

	public void copy(TimeTable clone) {
		this.id = clone.id;
		this.movie = clone.movie;
		this.room = clone.room;
		this.startTime = clone.startTime;
	}

}
