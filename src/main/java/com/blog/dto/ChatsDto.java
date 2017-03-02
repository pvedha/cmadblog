package com.blog.dto;

public class ChatsDto {
	String postedby;
	String chatmsg;
	String postedon;
	
	public ChatsDto(String userid, String chatmsg, String postedon) {
		super();
		this.postedby = userid;
		this.chatmsg = chatmsg;
		this.postedon = postedon;
	}
	
	public ChatsDto() {
		super();
	}

	public String getPostedBy() {
		return postedby;
	}

	public void setPostedBy(String userid) {
		this.postedby = userid;
	}

	public String getChatmsg() {
		return chatmsg;
	}

	public void setChatmsg(String chatmsg) {
		this.chatmsg = chatmsg;
	}

	public String getPostedon() {
		return postedon;
	}

	public void setPostedon(String postedon) {
		this.postedon = postedon;
	}
	
}
