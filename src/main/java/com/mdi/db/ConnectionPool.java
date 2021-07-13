package com.mdi.db;

import java.sql.Connection;
import java.sql.DriverManager;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Record;

import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.google.gson.Gson;
import com.mdi.jooq.model.tables.Users;
import com.mdi.jooq.model.tables.records.UsersRecord;

public class ConnectionPool {

	//private Connection connection;
	private static String user = "root";
	private static String password = "";
	private static String url = "jdbc:mysql://localhost:3306/users";
	// String driver = System.getProperty("jdbc.driver");
	private static DSLContext dslContext = null;

	private ConnectionPool() {
		
	}

	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection(url, user, password);
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
		
	}
	public static DSLContext  getDSLContext() throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		dslContext = DSL.using(ConnectionPool.getConnection(), SQLDialect.MYSQL);
//			Result<Record> result = dslContext.select().from(Users.USERS_).fetch();
//
//			for (Record r : result) {
//				Integer id = r.getValue(Users.USERS_.ID);
//				String firstName = r.getValue(Users.USERS_.FIRST_NAME);
//				String lastName = r.getValue(Users.USERS_.LAST_NAME);
//
//				System.out.println("ID: " + id + " first name: " + firstName + " last name: " + lastName);
//			}

		return dslContext;
	}

	public static void main(String[] args)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			DSLContext context = ConnectionPool.getDSLContext();
//			UsersRecord usersRecord = context.newRecord(Users.USERS_);
//			usersRecord.setId(5);
//			usersRecord.setFirstName("aa");
//			usersRecord.setLastName("bb");
//			usersRecord.setEmail("cc");
//			usersRecord.store();
//			Result<Record> records=context.select().from(Users.USERS_).fetch();
//			System.out.println(new Gson().toJson(records.formatJSON()));
			int i=context.delete(Users.USERS_).where(Users.USERS_.ID.eq(10)).execute();
			System.out.println(i);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
//		ConnectionPool.getDSLContext();
	}
}
