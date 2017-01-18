package com.blog.rest;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.blog.api.Post;
import com.blog.biz.Blog;

@Path("/")
public class RestController {

	public RestController() {
		// TODO Auto-generated constructor stub
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/post")
	public Response readAllPosts() {
		Blog blog = new Blog();
		ArrayList<Post> posts = blog.readAllPost();
		return Response.ok().entity(posts).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/post/{no}")
	public Response read(@PathParam("no") int number) {
		Blog blog = new Blog();
		return Response.ok().entity(blog.readPost(number)).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/version")
	public Response getVersion() {
		return Response.ok().entity("1.1").build();
	}
}