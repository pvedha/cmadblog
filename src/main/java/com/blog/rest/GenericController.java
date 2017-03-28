package com.blog.rest;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.blog.api.Exceptions.BlogException;
import com.blog.api.Exceptions.InvalidCommentException;
import com.blog.api.Exceptions.InvalidUserException;
import com.blog.biz.Blog;
import com.blog.dto.ChatsDto;
import com.blog.dto.NewChat;

@Path("/")
public class GenericController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/version")
	public Response getVersion() {
		return Response.ok().entity("4.2").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/exp/{no}")
	public Response getVersionException(@PathParam("no") int val) {
		switch (val) {
		case 1:
			System.out.println("case 1");
			throw new BlogException("BlogException");
		case 2:
			System.out.println("InvalidUserException");
			throw new InvalidUserException("Invalid user man");
			default:				
		}
		return Response.ok().entity("4.1").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/category")
	public Response readCategory() {
		Blog blog = new Blog();
		ArrayList<String> categories = blog.readCategory();
		return Response.ok().entity(categories).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/favourite/{userId}")
	public Response readCategory(@PathParam("userId") String userId) {
		Blog blog = new Blog();
		ArrayList<Integer> favourites = blog.readFavourites(userId);
		return Response.ok().entity(favourites).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/favourite/add/{userId}/{postId}")
	public Response addFavourite(@PathParam("userId") String userId, @PathParam("postId") int postId) {
		Blog blog = new Blog();
		boolean result = blog.addFavourite(userId, postId);
		return Response.ok().entity(Boolean.toString(result)).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/favourite/remove/{userId}/{postId}")
	public Response removeFavourite(@PathParam("userId") String userId, @PathParam("postId") int postId) {
		Blog blog = new Blog();
		boolean result = blog.removeFavourite(userId, postId);
		return Response.ok().entity(Boolean.toString(result)).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getChats")
	public Response getChats() {
		Blog blog = new Blog();
		ArrayList<ChatsDto> posts = blog.readRecentChats();
		return Response.ok().entity(posts).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/newChat")
	public Response newChat(NewChat chat) {
		Blog blog = new Blog();

		int number = blog.addChat(chat);
		return Response.ok().entity(Integer.toString(number)).build();

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/initDB")
	public Response initDB() {
		Blog blog = new Blog();
		int number = blog.initDB();
		return Response.ok().entity(Integer.toString(number)).build();
	}
}
