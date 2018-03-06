package com.xzzpig.cinemasystem.bean;

public class Seat {
	public Room room;
	String real;
	int index;
	
	public Integer timetable=-1;

	@Override
	public String toString() {
		return "Seat [room=" + room + ", real=" + real + ", index=" + index + "]";
	}

	public Seat(Room room, String real, int index) {
		super();
		this.room = room;
		this.real = real;
		this.index = index;
	}

	public final Room getRoom() {
		return room;
	}

	public final String getReal() {
		return real;
	}

	public final int getIndex() {
		return index;
	}

	public void copy(Seat clone) {
		this.room = clone.room;
		this.real = clone.real;
		this.index = clone.index;
	}
}
