package com.blog.api;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.ws.rs.FormParam;

@Entity
public class BlogUser {
	@Id
	@FormParam("userid")
	protected String userid;
	@FormParam("name")
	protected String name; 
	@FormParam("password")
	protected String password;
	@FormParam("about")
	protected String about;
	
	
	public BlogUser(String userid, String name, String password, String about) {
		super();
		this.userid = userid;
		this.name = name;
		this.password = password;
		this.about = about;
	}
	
	public BlogUser(){
		//this is for entity bean
	}
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	
	
}
