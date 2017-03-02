package com.blog.api;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Comments {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) // Check whether this works
													// while posting
	protected int comment_Id;
	protected int post_Id;
	protected String message;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "posted_by")
	protected BlogUser postedBy;
	protected Timestamp posted_on;
	
	public int getCommentId() {
		return comment_Id;
	}

	public void setCommentId(int comment_Id) {
		this.comment_Id = comment_Id;
	}

	public int getPostId() {
		return post_Id;
	}

	public void setPostId(int post_Id) {
		this.post_Id = post_Id;
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

	public Timestamp getPostedOn() {
		return posted_on;
	}

	public void setPostedOn(Timestamp posted_on) {
		this.posted_on = posted_on;
	}

	public Comments() {
		// nothing to do. needed for entity
	}

	public Comments(int comment_Id, int post_Id, String message, BlogUser postedBy, Timestamp posted_on) {
		super();
		this.comment_Id = comment_Id;
		this.post_Id = post_Id;
		this.message = message;
		this.postedBy = postedBy;
		this.posted_on = posted_on;
	}
	

}
