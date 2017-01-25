package com.blog.biz;

import java.util.ArrayList;

import com.blog.api.BlogUser;
import com.blog.api.Comments;
import com.blog.api.DuplicateUserException;
import com.blog.api.InvalidCommentException;
import com.blog.api.InvalidPostException;
import com.blog.api.InvalidUserException;
import com.blog.api.Post;

public interface BlogInterface {
	//post
	ArrayList<Post> readAllPost();
	Post readPost(int number);
	int createPost(Post post);
	
	//comment
	int addComment(Comments comment) throws InvalidCommentException;	
	public ArrayList<Comments> readCommentsOfPost(int postId) throws InvalidPostException;
	
	//user
	ArrayList<BlogUser> readAllUsers();
	ArrayList<String> readUserIds();
	int createUser(BlogUser user) throws InvalidUserException, DuplicateUserException;
	BlogUser validateLogin(String userId, String password);
}
