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

import com.blog.api.Exceptions.InvalidCommentException;
import com.blog.api.Exceptions.InvalidSearchKeyException;
import com.blog.biz.Blog;
import com.blog.dto.NewComment;
import com.blog.dto.NewPost;
import com.blog.dto.PostDto;
import com.blog.logger.Logger;

@Path("/post")
public class PostController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/all")
	public Response readAllPosts() {
		Blog blog = new Blog();
		ArrayList<PostDto> posts = blog.readAllPost();
		return Response.ok().entity(posts).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/offset/{offset}")
	public Response readLimitedPosts(@PathParam("offset") int offset) {
		Blog blog = new Blog();
		ArrayList<PostDto> posts = blog.readLimitedPosts(offset);
		return Response.ok().entity(posts).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{no}")
	public Response read(@PathParam("no") int number) {
		Blog blog = new Blog();
		return Response.ok().entity(blog.readPost(number)).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/search/{keys}")
	public Response search(@PathParam("keys") String keys) {
		Blog blog = new Blog();
		try {
			ArrayList<PostDto> posts = blog.searchPost(keys);
			return Response.ok().entity(posts).build();
		} catch (InvalidSearchKeyException e) {
			return Response.ok().entity(Status.NO_CONTENT).build();
		}		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/category/{category}")
	public Response searchByCategory(@PathParam("category") String category) {
		Blog blog = new Blog();
		ArrayList<PostDto> posts = new ArrayList<>();
		try{
			posts = blog.searchByCategory(category);
		} catch (InvalidSearchKeyException e) {
			return Response.ok().entity(Status.NO_CONTENT).build();
		}
		catch (Exception e){
			Logger.log(e.getMessage());
		}	
		return Response.ok().entity(posts).build();
	}
	
	//not used??
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/makePost")
	public Response makePost(NewPost post) {
		Blog blog = new Blog();
		int number = blog.createPostPersist(post);
		return Response.ok().entity(Integer.toString(number)).build();
	}	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/newPost")
	public Response newPost(NewPost newPost) {
		Blog blog = new Blog();
		int number = blog.createPost(newPost);
		return Response.ok().entity(Integer.toString(number)).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/addComment")
	public Response addComment(NewComment comment) {
		Blog blog = new Blog();
		try {
			int number = blog.addComment(comment);
			return Response.ok().entity(Integer.toString(number)).build();
		} catch (InvalidCommentException e) {
			return Response.ok().entity(Status.NOT_MODIFIED).build();
		}
	}
	
}
