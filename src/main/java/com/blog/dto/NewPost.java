package com.blog.dto;

public class NewPost {
	
	private String userId;
	private String title;
	private String message;
	private String tags;
	private String category;
	
	public NewPost() {
		// do nothing, needed for bean
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTags() {
		return tags;
	}

	public void setTag(String tags) {
		this.tags = tags;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	
}
