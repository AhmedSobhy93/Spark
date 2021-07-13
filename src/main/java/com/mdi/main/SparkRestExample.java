package com.mdi.main;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.mdi.jooq.model.tables.records.UsersRecord;
import com.mdi.model.User;
import com.mdi.response.StandardResponse;
import com.mdi.response.StatusResponse;
import com.mdi.service.UserService;
import com.mdi.service.UserServiceMapImpl;

public class SparkRestExample {
	public static void main(String[] args) {

		final UserService userService = new UserServiceMapImpl();
		post("/users", (request, response) -> {
			response.type("application/json");

			User user = new Gson().fromJson(request.body(), User.class);
			userService.addUser(user);

			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));

		});

		get("/users", (request, response) -> {
			response.type("application/json");

			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
					new Gson().toJsonTree(userService.getUsers().formatJSON())));
		});

		get("/users/:id", (request, response) -> {
			response.type("application/json");

			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
					new Gson().toJsonTree(userService.getUser(Integer.parseInt(request.params(":id"))).formatJSON())));
		});

		put("/users", (request, response) -> {
			response.type("application/json");

			User toEdit = new Gson().fromJson(request.body(), User.class);
			UsersRecord editedUser = userService.editUser(toEdit);

			if (editedUser != null) {
				return new Gson().toJson(
						new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(editedUser.formatJSON())));
			} else {
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,
						new Gson().toJson("User not found or error in edit")));
			}
		});

		delete("/users/:id", (request, response) -> {
			response.type("application/json");

			int deletedRows = userService.deleteUser(request.params(":id"));
			if (deletedRows != 0)
				return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "user deleted"));
			else
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "user not found!"));

		});

		options("/users/:id", (request, response) -> {
			response.type("application/json");

			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
					(userService.userExist(request.params(":id"))) ? "User exists" : "User does not exists"));
		});
	}
}
