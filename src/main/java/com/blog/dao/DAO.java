package com.blog.dao;

import java.util.ArrayList;

import com.blog.api.BlogUser;
import com.blog.api.Chats;
import com.blog.api.Comments;
import com.blog.api.Post;
import com.blog.dto.NewChat;
import com.blog.dto.NewComment;
import com.blog.dto.NewPost;
import com.blog.dto.UserDto;

public interface DAO {

	
	//post
	int postCreate(Post post);
	Post readPost(int p);
	ArrayList<Post> readAllPost();
	void update(Post post);	
	int getNextPostId();
	int postCreate(NewPost newPost);
	ArrayList<Post> searchPost(ArrayList<String> keys);
	ArrayList<Post> searchByCategory(String category);
	
	//comment
	int commentCreate(Comments comment);
	ArrayList<Comments> readComments(int postId);
	ArrayList<Comments> readComments(ArrayList<Integer> postIds);
	
	//user
	int userCreate(BlogUser user);	
	ArrayList<BlogUser> readAllUsers();
	ArrayList<String> readUserIds();
	BlogUser validateLogin(String userId, String password);
	BlogUser getUser(String userId);
	boolean updateUser(UserDto user);
	boolean deleteUser(UserDto user);
	
		
	//generic
	ArrayList<String> readCategory();
	int commentAdd(NewComment newPost);
	boolean addFavourite(String userId, int postId);
	boolean removeFavourite(String userId, int postId);
	ArrayList<Integer> readFavourites(String userId);
	int chatAdd(NewChat chat);
	ArrayList<Chats> getTopChats();
	int initDB();
	ArrayList<Post> readLimitedPosts(int offset);	
}
