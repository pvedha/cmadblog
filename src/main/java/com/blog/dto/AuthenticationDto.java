package com.blog.dto;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.blog.logger.Logger;

public class AuthenticationDto {

	private String userId;
	private String name;
	private String about;
	private String token;
	
	public String genToken(String userId) {
		
		MessageDigest m;
		try {
			m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(userId.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			String hashtext = bigInt.toString(16);
			// Now we need to zero pad it if you actually want the full 32 chars.
			while(hashtext.length() < 32 ){
			  hashtext = "0"+hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			Logger.log(e.getMessage());
			return null;
		}
		
	}
	
	public AuthenticationDto(String userId, String name, String about) {
		super();
		this.userId = userId;
		this.name = name;
		this.about = about;
		this.setToken(this.genToken(userId));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public AuthenticationDto() {
		// do nothing, needed for bean		
	}

}
