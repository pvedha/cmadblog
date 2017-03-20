package com.blog.biz;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import com.blog.api.BlogUser;
import com.blog.api.Chats;
import com.blog.api.Comments;
import com.blog.api.Post;
import com.blog.api.Exceptions.DuplicateUserException;
import com.blog.api.Exceptions.InvalidCommentException;
import com.blog.api.Exceptions.InvalidPostException;
import com.blog.api.Exceptions.InvalidSearchKeyException;
import com.blog.api.Exceptions.InvalidUserException;
import com.blog.dao.DAO;
import com.blog.dao.OracleDAOImpl;
import com.blog.dto.AuthenticationDto;
import com.blog.dto.ChatsDto;
import com.blog.dto.CommentDto;
import com.blog.dto.NewChat;
import com.blog.dto.NewComment;
import com.blog.dto.NewPost;
import com.blog.dto.PostDto;
import com.blog.dto.UserDto;
import com.blog.logger.Logger;

public class Blog implements BlogInterface {
	private DAO dao;// = new OracleDAOImpl();
	
	public Blog() {
		try {
			Logger.log("Initializing for OracleDAO");
			dao = new OracleDAOImpl();
		} catch (Exception e){
			Logger.log("No class found most likely " + e.getMessage());
		}
	}

	@Override
	public PostDto readPost(int postId) {
		Post post = dao.readPost(postId);
		ArrayList<Comments> comments = dao.readComments(postId);
		return getPostDto(post, comments);
	}

	@Override
	public ArrayList<PostDto> readAllPost() {
		ArrayList<Post> posts = dao.readAllPost();
		ArrayList<Comments> comments = dao.readComments(getPostIds(posts));
		return getPostDto(posts, comments);
	}

	@Override
	public ArrayList<PostDto> readLimitedPosts(int offset) {
		try { 
			ArrayList<Post> posts = dao.readLimitedPosts(offset);
			ArrayList<Comments> comments = dao.readComments(getPostIds(posts));
			return getPostDto(posts, comments);
		} catch (Exception e) {
			return new ArrayList<PostDto>();
		}
		
	}
	
	private ArrayList<PostDto> getPostDto(ArrayList<Post> posts, ArrayList<Comments> comments) {
		ArrayList<PostDto> postDtos = new ArrayList<>();
		for (Post post : posts) {
			PostDto postDto = getPostDto(post, comments);
			postDtos.add(postDto);

		}
		return postDtos;
	}

	public PostDto getPostDto(Post post, ArrayList<Comments> comments) {
		PostDto postDto = new PostDto();
		int postId = post.getPostId();
		postDto.setPostId(postId);
		postDto.setTitle(post.getTitle());
		postDto.setMessage(post.getMessage());
		postDto.setPostedBy(post.getPostedBy().getUserid());
		postDto.setUserName(post.getPostedBy().getName());
		postDto.setTags(post.getTags());		
		postDto.setPostedOn(new SimpleDateFormat("dd-MMM-yyyy, HH:mm:ss").format(post.getCreatedOn()));
		postDto.setCategory(post.getCategory());

		// This needs a better logic like multimap.
		ArrayList<CommentDto> postComments = new ArrayList<>();

		for (Comments comment : comments) {
			if (comment.getPostId() == postId) {
				CommentDto commentDto = new CommentDto();
				commentDto.setCommentId(comment.getCommentId());
				commentDto.setPostId(comment.getPostId());
				commentDto.setMessage(comment.getMessage());
				commentDto.setPostedOn(new SimpleDateFormat("dd-MMM-yyyy, HH:mm:ss").format(comment.getPostedOn()));
				commentDto.setUserId(comment.getPostedBy().getUserid());

				postComments.add(commentDto);
			}
		}

		postDto.setComments(postComments);
		return postDto;
	}

	private ArrayList<Integer> getPostIds(ArrayList<Post> posts) {
		ArrayList<Integer> postIds = new ArrayList<>();

		for (Post post : posts) {
			postIds.add(post.getPostId());
		}
		return postIds;
	}

	@Override
	public int createPost(Post post) {
		return dao.postCreate(post);
	}

	@Override
	public int createUser(BlogUser user) {
		if (user == null || user.getUserid() == null || user.getName() == null || user.getPassword() == null) {
			throw new InvalidUserException();
		}

		if (dao.getUser(user.getUserid()) != null) {
			throw new DuplicateUserException();
		}
		return dao.userCreate(user);
	}

	
	@Override
	public UserDto getUser(String userId){	
		BlogUser blogUser = dao.getUser(userId);
		UserDto user = new UserDto();
		user.setAbout(blogUser.getAbout());
		user.setUserName(blogUser.getName());
		return user;
	}
	
	@Override
	public boolean updateUser(UserDto user){
		return dao.updateUser(user);
	}
	
	@Override
	public boolean deleteUser(UserDto user){
		return dao.deleteUser(user);
	}
	
	@Override
	public int addComment(NewComment comment) {
		if (comment == null || comment.getPostId() == 0 || comment.getMessage() == null
				|| this.readPost(comment.getPostId()) == null) {
			throw new InvalidCommentException();
		}
		return dao.commentAdd(comment);
	}

	public ArrayList<Comments> readCommentsOfPost(int postId) {
		if (this.readPost(postId) == null) {
			throw new InvalidPostException();
		}
		return dao.readComments(postId);
	}

	@Override
	public ArrayList<BlogUser> readAllUsers() {
		return dao.readAllUsers();
	}

	@Override
	public ArrayList<String> readUserIds() {
		return dao.readUserIds();
	}

	@Override
	public AuthenticationDto validateLogin(String userId, String password) {
		BlogUser user = dao.validateLogin(userId, password);
		AuthenticationDto token = null;
		if (user != null) 
			token = this.makeAuthDto(user);
		return token;
	}

	@Override
	public AuthenticationDto validateSession(String userId, String token) {		
		if(!validateToken(userId, token)){
			return null;
		}
		Logger.log("proceeding to get the user details");
		AuthenticationDto response = new AuthenticationDto();		
		BlogUser user = dao.getUser(userId);
		response.setToken(token); //we can regenerate with time
		response.setAbout(user.getAbout());
		response.setName(user.getName());
		response.setUserId(user.getUserid());		
		return response;
	}
	
	private AuthenticationDto makeAuthDto(BlogUser user) {
		return (new AuthenticationDto(user.getUserid(), 
				user.getName(), user.getAbout()));
	}
	
	public Boolean validateToken(AuthenticationDto requestToken) {
		AuthenticationDto temp = new AuthenticationDto(requestToken.getUserId(),
				requestToken.getName(), requestToken.getAbout());		
		return temp.getToken().equals(requestToken.getToken());		
	}
	
	public Boolean validateToken(String userId, String token){
		if(new AuthenticationDto().genToken(userId).equals(token)){
			return true;
		}
		return false;
	}

	@Override
	public int createPost(NewPost newPost) {
		return dao.postCreate(newPost);
	}
	
	public ArrayList<PostDto> searchPost(String keys) {
		if (keys == null || keys.trim().isEmpty()) 
			throw new InvalidSearchKeyException();
		ArrayList<String> keyList = new ArrayList<>(Arrays.asList(keys.split("\\s")));
		ArrayList<Post> posts = dao.searchPost(keyList);
		ArrayList<Comments> comments = dao.readComments(getPostIds(posts));
		return getPostDto(posts, comments);
	}
	
	@Override //not working, trying to insert bloguser first
	public int createPostPersist(NewPost newPost) {
		Post post = new Post();		
		post.setPostedBy(dao.getUser(newPost.getUserId()));
		post.setTitle(newPost.getTitle());
		post.setMessage(newPost.getMessage());
		post.setTags(newPost.getTags());
		post.setCategory(newPost.getCategory());
		post.setCreatedOn(new Timestamp(System.currentTimeMillis()));
		Logger.log("CreatePostPersist");
		return dao.postCreate(post);
	}

	@Override
	public ArrayList<String> readCategory() {		
		return dao.readCategory();
	}
	
	@Override
	public ArrayList<PostDto> searchByCategory(String category) {
		ArrayList<Post> posts = dao.searchByCategory(category);
		ArrayList<Comments> comments = dao.readComments(getPostIds(posts));
		return getPostDto(posts, comments);
	}	

	@Override
	public boolean addFavourite(String userId, int postId){
		return dao.addFavourite(userId, postId);
	}
	
	@Override
	public boolean removeFavourite(String userId, int postId){
		return dao.removeFavourite(userId, postId);
	}
	
	@Override
	public ArrayList<Integer> readFavourites(String userId){
		return dao.readFavourites(userId);
	}
	
	public ArrayList<ChatsDto> readRecentChats() {
		ArrayList<Chats> chats = dao.getTopChats();
		return getChatsDtos(chats);		 		
	}

	private ArrayList<ChatsDto> getChatsDtos(ArrayList<Chats> chats) {
		ArrayList<ChatsDto> chatDtos = new ArrayList<>();
		for (Chats chat : chats) {
			ChatsDto chatDto = getChatDto(chat);
			chatDtos.add(chatDto);
		}
		return chatDtos;
	}
	
	public ChatsDto getChatDto(Chats chat) {
		ChatsDto chatDto = new ChatsDto();	
		chatDto.setChatmsg(chat.getMessage());
		chatDto.setPostedBy(chat.getUserid());		
		chatDto.setPostedon(new SimpleDateFormat("dd-MMM-yyyy, HH:mm:ss").format(chat.getPosted_on()));
		return chatDto;
	}
	
	public int addChat(NewChat chat) {
		if (chat == null ||  chat.getMessage() == null) {
			throw new InvalidCommentException();
		}
		return dao.chatAdd(chat);
	}

	@Override
	public int initDB() {
		return dao.initDB();
	}
}
