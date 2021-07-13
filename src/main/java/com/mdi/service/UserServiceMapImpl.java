package com.mdi.service;

import java.util.Collection;
import java.util.HashMap;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import com.mdi.db.ConnectionPool;
import com.mdi.error.UserException;

import com.mdi.jooq.model.tables.records.UsersRecord;
import com.mdi.model.User;
import com.mdi.jooq.model.tables.Users;

public class UserServiceMapImpl implements UserService {

	private HashMap<String, User> userMap;
	DSLContext context;

	public UserServiceMapImpl() {
		userMap = new HashMap<>();
		try {
			context = ConnectionPool.getDSLContext();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addUser(User user) {
		// userMap.put(user.getId(), user);

		UsersRecord usersRecord = context.newRecord(Users.USERS_);

		usersRecord.setFirstName(user.getFirstName());
		usersRecord.setLastName(user.getLastName());
		usersRecord.setEmail(user.getEmail());
		usersRecord.store();

	}

	@Override
	public Result<Record> getUsers() {
		// return userMap.values();

		Result<Record> records = context.select().from(Users.USERS_).fetch();

		return records;

	}

	@Override
	public UsersRecord getUser(Integer id) {
		// return userMap.get(id);

		UsersRecord usersRecord = context.fetchOne(Users.USERS_, Users.USERS_.ID.eq(id));

		return usersRecord;

	}

	@Override
	public UsersRecord editUser(User forEdit) throws UserException {
		try {
			if (forEdit.getId() == null)
				throw new UserException("ID cannot be blank");

			UsersRecord toEdit = context.fetchOne(Users.USERS_, Users.USERS_.ID.eq(forEdit.getId()));
			// User toEdit = userMap.get(forEdit.getId());

			if (toEdit == null)
				throw new UserException("User not found");

//			if (forEdit.getEmail() != null) {
//				toEdit.setEmail(forEdit.getEmail());
//			}
//			if (forEdit.getFirstName() != null) {
//				toEdit.setFirstName(forEdit.getFirstName());
//			}
//			if (forEdit.getLastName() != null) {
//				toEdit.setLastName(forEdit.getLastName());
//			}
//			if (forEdit.getId() != null) {
//				toEdit.setId(forEdit.getId());
//			}

			context.update(Users.USERS_).set(Users.USERS_.FIRST_NAME, forEdit.getFirstName())
					.set(Users.USERS_.LAST_NAME, forEdit.getLastName()).set(Users.USERS_.EMAIL, forEdit.getEmail())
					.where(Users.USERS_.ID.eq(forEdit.getId())).execute();

			return toEdit;
		} catch (Exception ex) {
			throw new UserException(ex.getMessage());
		}
	}

	@Override
	public int deleteUser(String id) {
		// userMap.remove(id);

		int deletedRows = 0;
		try {
			context = ConnectionPool.getDSLContext();

			deletedRows = context.delete(Users.USERS_).where(Users.USERS_.ID.eq(Integer.parseInt(id))).execute();

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {

			e.printStackTrace();
		}
		return deletedRows;
	}

	@Override
	public boolean userExist(String id) {
		// return userMap.containsKey(id);
		boolean isExist = context.fetchExists(Users.USERS_, Users.USERS_.ID.eq(Integer.parseInt(id)));
		return isExist;
	}

}
