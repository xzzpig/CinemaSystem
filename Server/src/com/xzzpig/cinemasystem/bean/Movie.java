package com.xzzpig.cinemasystem.bean;

public class Movie {
	String name;
	int price;
	int minutes;
	String type;
	int score;

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public int getMinutes() {
		return minutes;
	}

	public String getType() {
		return type;
	}

	public int getScore() {
		return score;
	}

	@Override
	public String toString() {
		return "Movie [name=" + name + ", price=" + price + ", minutes=" + minutes + ", type=" + type + ", score="
				+ score + "]";
	}

	public Movie(String name, int price, int minutes, String type, int score) {
		super();
		this.name = name;
		this.price = price;
		this.minutes = minutes;
		this.type = type;
		this.score = score;
	};

	public Movie(String name) {
		this(name, -1, -1, null, -1);
	}

}
