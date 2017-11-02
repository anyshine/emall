package cn.tmall.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	static String ip = "localhost";
	static int port = 3306;
	static String database = "tmall";
	static String charset = "UTF-8";
	static String username = "root";
	static String password = "123";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		String url = String.format(
				"jdbc:mysql://%s:%d/%s?characterEncoding=%s", ip, port,
				database, charset);
		return DriverManager.getConnection(url, username, password);
	}
	public static void main(String[] args) throws SQLException {
		System.out.println(getConnection());
	}
}
