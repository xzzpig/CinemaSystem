package com.xzzpig.cinemasystem.dao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DaoUtils {
	private static ComboPooledDataSource dataSource;

	public static interface ConnectionConsumer {
		void consume(Connection connection) throws SQLException;
	}

	static {
		dataSource = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass("com.mysql.jdbc.Driver");
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		} // loads the jdbc driver
		dataSource.setJdbcUrl(
				"jdbc:mysql://localhost:3306/ticket?user=root&password=mayxh1101&useUnicode=true&characterEncoding=UTF8");
		// the settings below are optional -- c3p0 can work with defaults
		dataSource.setMinPoolSize(5);
		dataSource.setAcquireIncrement(5);
		dataSource.setMaxPoolSize(20);
	}

	static DataSource getInstance() {
		return dataSource;
	}

	static void withConnection(ConnectionConsumer consumer) throws SQLException {
		Connection connection = null;
		try {
			connection = getInstance().getConnection();
			connection.setAutoCommit(false);
			consumer.consume(connection);
			connection.commit();
		} catch (SQLException e) {
			if(connection!=null)
				connection.rollback();
			throw e;
		} finally {
			if (connection != null)
				connection.close();
		}
	}
}
