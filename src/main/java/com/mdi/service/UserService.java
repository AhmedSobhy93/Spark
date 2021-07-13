package com.mdi.service;

import java.util.Collection;

import org.jooq.Record;
import org.jooq.Result;

import com.mdi.error.UserException;
import com.mdi.jooq.model.tables.records.UsersRecord;
import com.mdi.model.User;

public interface UserService {

	public void addUser(User user);

	public Result<Record> getUsers();

	public UsersRecord getUser(Integer id);

	public UsersRecord editUser(User user) throws UserException;

	public int deleteUser(String id);

	public boolean userExist(String id);
}
