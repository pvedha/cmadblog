package com.blog.api;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) // Check whether this works
											// while posting
	protected int post_Id;
	protected String title;
	protected String message;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "posted_by", referencedColumnName="userid")	
	protected BlogUser postedBy;
	protected Timestamp posted_on;
	protected String tags;
	protected String category;
	// Need to add comment array - dont. 

	public Post() {
		// nothing to do. needed for entity
	}

	public Post(int postId, String title, String message, BlogUser postedBy, Timestamp createdOn, String tags,
			String category) {
		super();
		this.post_Id = postId;
		this.title = title;
		this.message = message;
		this.postedBy = postedBy;
		this.posted_on = createdOn;
		this.tags = tags;
		this.category = category;
	}

	public int getPostId() {
		return post_Id;
	}

	public void setPostId(int postId) {
		this.post_Id = postId;
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

	public BlogUser getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(BlogUser postedBy) {
		this.postedBy = postedBy;
	}

	public Timestamp getCreatedOn() {
		return posted_on;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.posted_on = createdOn;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
