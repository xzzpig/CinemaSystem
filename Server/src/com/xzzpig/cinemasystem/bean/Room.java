package com.xzzpig.cinemasystem.bean;

public class Room {
	int id;
	String name;
	int count;

	public Room(int id, String name, int count) {
		super();
		this.id = id;
		this.name = name;
		this.count = count;
	}

	public Room(int id) {
		this(id, null, -1);
	}

	@Override
	public String toString() {
		return "Room [id=" + id + ", name=" + name + ", count=" + count + "]";
	}

	public final int getId() {
		return id;
	}

	public final String getName() {
		return name;
	}

	public final int getCount() {
		return count;
	}

	public void copy(Room clone) {
		this.id = clone.id;
		this.name = clone.name;
		this.count = clone.count;
	}

}
