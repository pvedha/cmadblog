package com.blog.trials;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class FavouriteEmbeddable implements Serializable{

	private String userId;
	private int postId;
	
	public FavouriteEmbeddable() {
		// do nothing, needed for beans
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
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
}
