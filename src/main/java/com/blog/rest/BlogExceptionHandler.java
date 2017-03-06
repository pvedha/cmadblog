package com.blog.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.blog.api.Exceptions.BlogException;
import com.blog.api.Exceptions.InvalidUserException;
 
@Provider
public class BlogExceptionHandler implements ExceptionMapper<BlogException> 
{
 	@Override
	public Response toResponse(BlogException e) {
 		System.out.println("Got blog exception" + e.getMessage() + ".." + e.getClass());
 		
 		Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
 		
 		if(e instanceof InvalidUserException){
 			httpStatus = Response.Status.NOT_FOUND;
 		}
 		
		return Response.status(httpStatus).entity(e.getMessage()).build();
	}
}
