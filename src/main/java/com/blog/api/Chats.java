package com.blog.api;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Chats {
    @Id
	protected int chat_id;
    @Column(name="posted_by")
	protected String userid;
	protected String message;	
	protected Timestamp posted_on;
	
	public Chats(int chats_id, String userid, String message, Timestamp posted_on) {
		super();
		this.chat_id = chats_id;
		this.userid = userid;
		this.message = message;
		this.posted_on = posted_on;
	}

	public Chats() {
		super();		
	}

	public int getChats_id() {
		return chat_id;
	}

	public void setChats_id(int chats_id) {
		this.chat_id = chats_id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getPosted_on() {
		return posted_on;
	}

	public void setPosted_on(Timestamp posted_on) {
		this.posted_on = posted_on;
	}
		
}
