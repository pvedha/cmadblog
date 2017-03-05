package com.blog.biz;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.easymock.Mock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blog.dto.AuthenticationDto;
import com.blog.dto.NewComment;
import com.blog.dto.NewPost;
import com.blog.dto.PostDto;
import com.blog.dto.UserDto;
import com.blog.api.Exceptions.InvalidSearchKeyException;
import com.blog.api.BlogUser;
import com.blog.api.Exceptions.DuplicateUserException;
import com.blog.api.Exceptions.InvalidCommentException;


public class BlogTest {
	
	@Mock
	static private BlogInterface toTest;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		toTest = new Blog();
	}

	@Before
	public void setUp() throws Exception {
		//toTest = new Blog();
	}

	@Test
	public void testBlog() {
		Blog initBlog = new Blog();
		assertTrue(initBlog != null);
	}

	@Test
	public void testReadPost() {
		
	}

	@Test
	public void testReadAllPost() {
		ArrayList<PostDto> posts = toTest.readAllPost();
		assertTrue(posts.size() > 0);
	}

	@Test
	public void testReadLimitedPosts() {
		ArrayList<PostDto> posts = toTest.readLimitedPosts(0);
		assertTrue(posts.size() >= 0);
	}
	
	@Test
	public void testUserTokenValidate() {
			assertTrue(((Blog) toTest).validateToken("hello", new AuthenticationDto("hello", "a", "b").getToken()));		
	}
	
	@Test
	public void testUserTokenNegValidate() {
		 assertFalse(((Blog) toTest).validateToken("abc ", "def "));
	}
	
	@Test(expected=InvalidSearchKeyException.class) 
	public void testNullSearch() {
		toTest.searchPost("");		
	}
	
	@Test(expected=InvalidCommentException.class)
	public void testNullComment()  {
		toTest.addComment(new NewComment());
		
	}

	/*
	 * @author: pvedha
	 * Identify a user, if user exists, delete and create. 
	 * Delete the test user after completion.
	 * 
	 */
	@Test//(expected=DuplicateUserException.class)
	public void testCreateUser() {		
		BlogUser user = new BlogUser("test", "test","test","test");
		UserDto userDto = new UserDto();
		boolean testUserAvailable = true;
		try{
		 userDto = toTest.getUser(user.getUserid());
		} catch (NullPointerException e){
			testUserAvailable = false;
		}
		if(!testUserAvailable){
			try{
				assertEquals("User created successfully", 0, toTest.createUser(user));
			}catch(DuplicateUserException e){
				fail("Userid not available, failed to create new user");
			}
		}
		//userid or user already available
	
		userDto.setUserId(user.getUserid());
		userDto.setPassword(user.getPassword());
		try{
		assertTrue("Existing user deleted" ,toTest.deleteUser(userDto));
		} catch (Exception e){
			fail("user cannot be deleted, check the password");
		}
	}
	
	
	
	
	
	
	/*
	 * @author: pvedha
	 * Identify a user, if user exists, try creating again. 
	 * if user not available create and create again. 
	 * Delete the test user after completion.
	 * 
	 */
	@Test//(expected=DuplicateUserException.class)
	public void testCreateDuplicateUser() {		
		BlogUser user = new BlogUser("test", "test","test","test");
		UserDto userDto = new UserDto();
		boolean testUserAvailable = true;
		try{
		 userDto = toTest.getUser(user.getUserid());
		} catch (NullPointerException e){
			testUserAvailable = false;
		}
		if(!testUserAvailable){
			try{
				assertEquals("User created successfully", 0, toTest.createUser(user));
			}catch(DuplicateUserException e){
				fail("Userid not available, failed to create new user");
			}
		}
		
		//user available, now create again.
		
		try{
			toTest.createUser(user);
		}catch(DuplicateUserException e){
			assertTrue("Duplicate User Exception Received for existing user", true); 
			//should there be a better way instead of assertTrue, like opposite of fail()?
		}
		
		//userid or user already available
	
		userDto.setUserId(user.getUserid());
		userDto.setPassword(user.getPassword());
		try{
		assertTrue("Existing user deleted" ,toTest.deleteUser(userDto));
		} catch (Exception e){
			fail("user cannot be deleted, check the password");
		}
	}
	
	
	/*
	 * @author: pvedha
	 * Identify a user, if user exists, try deleting with wrong password.
	 * if user not available, create one and delete with wrong password. 
	 * finally, just delete the test user. 
	 * 
	 */
	@Test//(expected=DuplicateUserException.class)
	public void testDeleteUserWrongPassword() {		
		BlogUser user = new BlogUser("test", "test","test","test");
		UserDto userDto = new UserDto();
		boolean testUserAvailable = true;
		try{
		 userDto = toTest.getUser(user.getUserid());
		} catch (NullPointerException e){
			testUserAvailable = false;
		}
		if(!testUserAvailable){
			try{
				assertEquals("User created successfully", 0, toTest.createUser(user));
			}catch(DuplicateUserException e){
				fail("Userid not available, failed to create new user");
			}
		}
		
		//user available, now delete with wrong password.
		userDto.setUserId(user.getUserid());
		userDto.setPassword("wrongPassword");
		try{
			assertFalse("User with wrong password not deleted",toTest.deleteUser(userDto));
		}catch(Exception e){
			//assertTrue("Duplicate User Exception Received for existing user", true);
			fail("User deletion fails for reason unknown. Resolve this");
			//should there be a better way instead of assertTrue, like opposite of fail()?
		}
		
		//userid or user already available, delete with correct password
	
		userDto.setUserId(user.getUserid());
		userDto.setPassword(user.getPassword());
		try{
		assertTrue("Existing user deleted" ,toTest.deleteUser(userDto));
		} catch (Exception e){
			fail("user cannot be deleted, check the password");
		}
	}
	
	/*@Test Praveen check this case
	public void testCreatePost() {
		NewPost newPost = new NewPost();
		newPost.setUserId("aswin");
		newPost.setCategory("Politics");
		newPost.setTitle("Joker elected cm");
		newPost.setTag("joker");
		newPost.setMessage("Crazy people rule the world");
		int postId = toTest.createPost(newPost);
		assertTrue(postId != 0);		
	}
	//Execute after success of previous test case
	@Test 
	public void testSearch() {
		toTest.searchPost("joker");		
	}
	@Test 
	public void testAddComment() {
	   NewComment cmt = new NewComment();
		cmt.setMessage("Helloworld");
		cmt.setUserId("aswin");
		cmt.setPostId(1);
		toTest.addComment(cmt);
	}
	
	
	*/
/*
	@Test
	public void testGetPostDto() {
		//fail("Not yet implemented");
	}

	

	@Test
	public void testCreateUser() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetUser() {
		//fail("Not yet implemented");
	}

	@Test
	public void testUpdateUser() {
		//fail("Not yet implemented");
	}

	@Test
	public void testAddComment() {
		//fail("Not yet implemented");
	}

	@Test
	public void testReadCommentsOfPost() {
//		fail("Not yet implemented");
	}

	@Test
	public void testReadAllUsers() {
//		fail("Not yet implemented");
	}

	@Test
	public void testReadUserIds() {
//		fail("Not yet implemented");
	}

	@Test
	public void testValidateLogin() {
//		fail("Not yet implemented");
	}

	@Test
	public void testValidateSession() {
//		fail("Not yet implemented");
	}

	@Test
	public void testValidateTokenAuthenticationDto() {
//		fail("Not yet implemented");
	}

	@Test
	public void testValidateTokenStringString() {
//		fail("Not yet implemented");
	}

	@Test
	public void testCreatePostNewPost() {
//		fail("Not yet implemented");
	}

	@Test
	public void testSearchPost() {
//		fail("Not yet implemented");
	}

	@Test
	public void testCreatePostPersist() {
//		fail("Not yet implemented");
	}

	@Test
	public void testReadCategory() {
//		fail("Not yet implemented");
	}

	@Test
	public void testSearchByCategory() {
//		fail("Not yet implemented");
	}

	@Test
	public void testAddFavourite() {
//		fail("Not yet implemented");
	}

	@Test
	public void testRemoveFavourite() {
//		fail("Not yet implemented");
	}

	@Test
	public void testReadFavourites() {
//		fail("Not yet implemented");
	}

	@Test
	public void testReadRecentChats() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetChatDto() {
//		fail("Not yet implemented");
	}

	@Test
	public void testAddChat() {
//		fail("Not yet implemented");
	}

	@Test
	public void testInitDB() {
//		fail("Not yet implemented");
	}*/

}
