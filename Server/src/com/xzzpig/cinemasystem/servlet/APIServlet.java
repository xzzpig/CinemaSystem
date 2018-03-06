package com.xzzpig.cinemasystem.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.xzzpig.cinemasystem.bean.Movie;
import com.xzzpig.cinemasystem.bean.Order;
import com.xzzpig.cinemasystem.bean.Room;
import com.xzzpig.cinemasystem.bean.Seat;
import com.xzzpig.cinemasystem.bean.TimeTable;
import com.xzzpig.cinemasystem.bean.User;
import com.xzzpig.cinemasystem.service.MovieService;
import com.xzzpig.cinemasystem.service.OrderService;
import com.xzzpig.cinemasystem.service.OrderService.OrderResult;
import com.xzzpig.cinemasystem.service.SeatService;
import com.xzzpig.cinemasystem.service.TimeTableService;
import com.xzzpig.cinemasystem.service.UserService;
import com.xzzpig.cinemasystem.service.UserService.LoginResult;
import com.xzzpig.cinemasystem.service.UserService.RegResult;

/**
 * Servlet implementation class APIServlet
 */
@WebServlet(name = "API", urlPatterns = { "/API" })
public class APIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public APIServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	private Gson gson = new Gson();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("content-type","text/html;charset=UTF-8");
		try {
			String action = request.getParameter("action");
			switch (action) {
			case "login":
				User user = new User(request.getParameter("phone"), request.getParameter("password"));
				LoginResult result = UserService.login(user);
				System.out.println("LOGIN:" + user);
				response.getWriter().write(result.name());
				return;
			case "regist":
				User user2 = new User(request.getParameter("phone"), request.getParameter("password"));
				System.out.println("REG:" + user2);
				RegResult result2 = UserService.regist(user2);
				response.getWriter().write(result2.name());
				return;
			case "movieList":
				Movie[] movies = MovieService.getMovies();
				response.getWriter().append(gson.toJson(movies));
				return;
			case "orderList":
				Order[] orders = OrderService
						.getUserOrders(new User(request.getParameter("phone"), request.getParameter("password")));
				response.getWriter().append(gson.toJson(orders));
				return;
			case "timeTableList":
				TimeTable[] timeTables = TimeTableService.getTimeTable(new Movie(request.getParameter("movie")),
						new Date(Long.parseLong(request.getParameter("date"))));
				response.getWriter().append(gson.toJson(timeTables));
				return;
			case "seatArrange":
				Seat[] seats = SeatService
						.getSeatArrange(new TimeTable(Integer.parseInt(request.getParameter("timetable")), null,
								new Room(Integer.parseInt(request.getParameter("room"))), null));
				response.getWriter().append(gson.toJson(seats));
				return;
			case "orderSeat":
				String phone = request.getParameter("phone");
				String password = request.getParameter("password");
				int timetable = Integer.parseInt(request.getParameter("timetable"));
				int[] seatarr = gson.fromJson(request.getParameter("seats"), int[].class);
				User orderUser = new User(phone, password);
				TimeTable orderTimetable = new TimeTable(timetable);
				Order[] orders2 = new Order[seatarr.length];
				for (int i = 0; i < orders2.length; i++)
					orders2[i] = new Order(-1L, orderUser, null, orderTimetable, new Seat(null, null, seatarr[i]));
				OrderResult orderResult = OrderService.order(orders2);
				response.getWriter().append(orderResult.name());
				return;
			case "removeOrder":
				Order order = new Order(Long.parseLong(request.getParameter("order")),
						new User(request.getParameter("phone"), request.getParameter("password")), null, null, null);
				response.getWriter().append(OrderService.remove(order).name());
				return;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().write("REQUESTERROR");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
