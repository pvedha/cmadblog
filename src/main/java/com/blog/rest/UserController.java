package com.blog.rest;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.blog.api.BlogUser;
import com.blog.api.Exceptions.DuplicateUserException;
import com.blog.api.Exceptions.InvalidUserException;
import com.blog.biz.Blog;
import com.blog.dto.AuthenticationDto;
import com.blog.dto.UserDto;

@Path("/user")
public class UserController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/all")
	public Response readAllUsers() {
		Blog blog = new Blog();
		ArrayList<BlogUser> blogUsers = blog.readAllUsers();
		return Response.ok().entity(blogUsers).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ids")
	public Response readUserIds() {
		Blog blog = new Blog();
		ArrayList<String> userNames = blog.readUserIds();
		return Response.ok().entity(userNames).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/view/{userId}")
	public Response getUser(@PathParam("userId") String userId) {
		Blog blog = new Blog();
		UserDto user = blog.getUser(userId);
		return Response.ok().entity(user).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/update")
	public Response updateUser(UserDto user) {
		Blog blog = new Blog();
		boolean result = blog.updateUser(user);
		return Response.ok().entity(Boolean.toString(result)).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{userid}/{password}")
	public Response login(@PathParam("userid") String userId, @PathParam("password") String password) {
		Blog blog = new Blog();
		AuthenticationDto token = blog.validateLogin(userId, password);
		if (token != null) {
			return Response.ok().entity(token).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/addUser")
	public Response addUser(BlogUser user) throws InvalidUserException, DuplicateUserException {
		Blog blog = new Blog();
		try {
			int number = blog.createUser(user);
			return Response.ok().entity(Integer.toString(number)).build();
		} catch (Exception e) {
			return Response.status(Status.CONFLICT).entity("Invalid User Details").build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/addUser/{userid}/{name}/{password}/{about}")
	public Response addUser(@PathParam("userid") String userId, @PathParam("name") String name,
			@PathParam("password") String password, @PathParam("about") String about)
			throws InvalidUserException, DuplicateUserException {
		Blog blog = new Blog();
		BlogUser user = new BlogUser(userId, name, password, about);
		int number = blog.createUser(user);
		return Response.ok().entity(Integer.toString(number)).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/validate")
	public Response validateSession(@HeaderParam("userId") String userId, @HeaderParam("token") String token) {
		Blog blog = new Blog();
		AuthenticationDto response = blog.validateSession(userId, token);
		if (response == null) {
			return Response.status(Status.FORBIDDEN).build();
		}
		return Response.ok().entity(response).build();
	}

}
