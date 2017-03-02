package com.blog.api;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(FavouriteKey.class)
public class Favourite {
	@Id
	private String userId;
	@Id
	private int postId;
	
	public Favourite() {
		//Needed for bean
	}

	public Favourite(String userId, int postId) {
		this.userId = userId;
		this.postId = postId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	

}
