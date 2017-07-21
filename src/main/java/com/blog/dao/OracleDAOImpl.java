package com.blog.dao;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.exception.SQLGrammarException;

import com.blog.api.BlogUser;
import com.blog.api.Category;
import com.blog.api.Chats;
import com.blog.api.Comments;
import com.blog.api.Favourite;
import com.blog.api.FavouriteKey;
import com.blog.api.Post;
import com.blog.dto.NewChat;
import com.blog.dto.NewComment;
import com.blog.dto.NewPost;
import com.blog.dto.UserDto;
import com.blog.logger.Logger;

@SuppressWarnings("unchecked")
public class OracleDAOImpl implements DAO {
	static EntityManagerFactory factory = Persistence.createEntityManagerFactory("doc");

	@Override
	public int initDB() {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		String userQuery = "insert into bloguser values ('p','p','Praveen Vedha','p')";
		String categoryQuery = "insert all " + "into category values('Entertainment') "
				+ "into category values('History')	" + "into category values('Politics')	"
				+ "into category values('Finance')	" + "into category values('General') " + "select 1 from dual";
		String postQuery = "insert into post values (1,'General', 'My message goes here', sysdate, 'General, Blog', 'First post title', 'p')";
		String commentQuery = "insert into comments values (1, 'first comment', 1, sysdate, 'p')";
		String chatQuery = "insert into chats values (1, 'One flew over something', sysdate, 'p')";
		em.createNativeQuery(userQuery).executeUpdate();
		em.createNativeQuery(postQuery).executeUpdate();
		em.createNativeQuery(categoryQuery).executeUpdate();
		em.createNativeQuery(commentQuery).executeUpdate();
		em.createNativeQuery(chatQuery).executeUpdate();
		em.getTransaction().commit();
		em.close();
		Logger.log("Init success");
		return 0;
	}

	@Override
	public int postCreate(Post post) {
		Logger.log("Creating post in DAO");
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		BlogUser user = em.find(BlogUser.class, post.getPostedBy().getUserid());
		// em.merge(user);
		post.setPostedBy(user);
		em.persist(post);
		em.getTransaction().commit();
		Logger.log("committed");
		em.close();
		return post.getPostId();
	}

	@Override
	public Post readPost(int postId) {
		EntityManager em = factory.createEntityManager();
		Post post = em.find(Post.class, postId);
		em.close();
		return post;
	}

	@Override
	public BlogUser getUser(String userId) {
		EntityManager em = factory.createEntityManager();
		BlogUser user = em.find(BlogUser.class, userId);
		em.close();
		return user;
	}

	@Override
	public boolean updateUser(UserDto user) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		BlogUser blogUser = em.find(BlogUser.class, user.getUserId());
		if (!user.getNewPassword().trim().isEmpty()) {
			blogUser.setPassword(user.getNewPassword());
		}
		blogUser.setAbout(user.getAbout());
		em.persist(blogUser);
		em.getTransaction().commit();
		em.close();
		return false;
	}
	
	@Override
	public boolean deleteUser(UserDto user){
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		BlogUser blogUser = em.find(BlogUser.class, user.getUserId());
		if(user.getPassword().contentEquals(blogUser.getPassword())){
			em.remove(blogUser);
			em.getTransaction().commit();
			em.close();
			return true;		
		}else{
			em.close();
			return false;
		}
	}

	@Override // We need either update/add comment
	public void update(Post post) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		em.merge(post);
		em.getTransaction().commit();
		em.close();
	}

	public void addComment() {

	}

	@Override
	public ArrayList<Post> readAllPost() {
		EntityManager em = factory.createEntityManager();
		ArrayList<Post> posts = (ArrayList<Post>) em
				.createNativeQuery("select * from post order by posted_on desc", Post.class).getResultList();
		em.close();
		return posts;
	}

	@Override
	public ArrayList<Post> readLimitedPosts(int offset) throws SQLGrammarException {
		EntityManager em = factory.createEntityManager();
		ArrayList<Post> posts = (ArrayList<Post>) em
				.createNativeQuery("select * from "
						+ "(select rownum as rnum , p.* from (select * from post  order by POSTED_ON desc) p ) "
						+ "where rnum  between :from and :to", Post.class)
				.setParameter("from", offset).setParameter("to", offset + 9).getResultList();
		em.close();
		return posts;
	}

	@Override
	public int userCreate(BlogUser user) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		em.persist(user);
		em.getTransaction().commit();
		em.close();
		return 0;
	}

	@Override
	public int commentCreate(Comments comment) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		em.persist(comment);
		em.getTransaction().commit();
		em.close();
		return 0;
	}

	@Override
	public ArrayList<Comments> readComments(int postId) {
		EntityManager em = factory.createEntityManager();
		ArrayList<Comments> comments = (ArrayList<Comments>) em
				.createNativeQuery("select * from comments where post_id = :id", Comments.class)
				.setParameter("id", postId).getResultList();
		em.close();
		return comments;
	}

	@Override
	public ArrayList<Comments> readComments(ArrayList<Integer> postIds) {
		EntityManager em = factory.createEntityManager();
		ArrayList<Comments> comments = (ArrayList<Comments>) em
				.createNativeQuery("select * from comments where post_id in :ids", Comments.class)
				.setParameter("ids", postIds).getResultList();
		return comments;
	}

	@Override
	public ArrayList<BlogUser> readAllUsers() {
		EntityManager em = factory.createEntityManager();
		ArrayList<BlogUser> blogUsers = (ArrayList<BlogUser>) em
				.createNativeQuery("select * from bloguser", BlogUser.class).getResultList();
		em.close();
		return blogUsers;
	}

	@Override
	public ArrayList<String> readUserIds() {
		EntityManager em = factory.createEntityManager();
		ArrayList<String> userIds = (ArrayList<String>) em.createNativeQuery("select userid from bloguser")
				.getResultList();
		em.close();
		return userIds;
	}

	@Override
	public BlogUser validateLogin(String userId, String password) {

		EntityManager em = factory.createEntityManager();
		String validateQuery = "select * from bloguser where  userid = :userId and password = :password";
		ArrayList<BlogUser> blogUsers = (ArrayList<BlogUser>) em.createNativeQuery(validateQuery, BlogUser.class)
				.setParameter("userId", userId).setParameter("password", password).getResultList();
		em.close();
		if (blogUsers.size() == 1) {
			return blogUsers.get(0);
		} else {
			return null;
		}
	}

	@Override
	public int getNextPostId() {
		EntityManager em = factory.createEntityManager();
		Object postId =	em.createNativeQuery("select max(post_id)+1 from post").getSingleResult();
		Logger.log("Object to string " + postId.toString());
		Logger.log("The next post id is " + postId);
		return 5;
	}

	@Override
	public int postCreate(NewPost newPost) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		int result = em
				.createNativeQuery("insert into post values((select max(post_id)+1 from post),"
						+ ":title,:message,:userid,sysdate,:tags,:category)")
				.setParameter("title", newPost.getTitle()).setParameter("message", newPost.getMessage())
				.setParameter("userid", newPost.getUserId()).setParameter("tags", newPost.getTags())
				.setParameter("category", newPost.getCategory()).executeUpdate();
		em.getTransaction().commit();
		em.close();
		return result;
	}

	@Override
	public int commentAdd(NewComment newComment) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		int result = em
				.createNativeQuery("insert into comments values((select max(comment_id)+1 from comments),"
						+ " :message, :postid, sysdate, :userid)")
				.setParameter("postid", newComment.getPostId()).setParameter("message", newComment.getMessage())
				.setParameter("userid", newComment.getUserId()).executeUpdate();
		em.getTransaction().commit();
		em.close();
		return result;
	}

	@Override
	public ArrayList<Post> searchPost(ArrayList<String> keys) {
		EntityManager em = factory.createEntityManager();
		String query = "select * from post where ";

		for (String key : keys) {
			query = query + "title like \'%" + key + "%\' or message like \'%" + key + "%\' or tags like \'%" + key
					+ "%\'";
			if (keys.indexOf(key) < keys.size() - 1) {
				query = query + " or ";
			}
		}

		Logger.log("The query is " + query);
		ArrayList<Post> posts = (ArrayList<Post>) em.createNativeQuery(query, Post.class).getResultList();

		em.close();
		return posts;
	}

	@Override
	public ArrayList<String> readCategory() {
		EntityManager em = factory.createEntityManager();
		ArrayList<Category> categories = (ArrayList<Category>) em
				.createNativeQuery("select * from category", Category.class).getResultList();
		ArrayList<String> categoryString = new ArrayList<>();
		for (Category category : categories) {
			categoryString.add(category.getCategory());
		}
		return categoryString;
	}

	@Override
	public ArrayList<Post> searchByCategory(String category) {
		EntityManager em = factory.createEntityManager();
		Logger.log("Search by category 1 " + category);
		try {
			ArrayList<Post> posts = (ArrayList<Post>) em
					.createNativeQuery("select * from post where category = :cat", Post.class)
					.setParameter("cat", category).getResultList();
			return posts;
		} catch (SQLGrammarException sqlE) {
			Logger.log("Caught SQL Grammer exception");
			return new ArrayList<Post>();
		} catch (javax.persistence.PersistenceException sqlP) {
			Logger.log("Caught SQL Grammer exception");
			return new ArrayList<Post>();
		} catch (Exception e) {
			Logger.log("Exception for empty list ");
			return new ArrayList<Post>();
		}
	}

	@Override
	public boolean addFavourite(String userId, int postId) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		Favourite favourite = new Favourite(userId, postId);
		em.persist(favourite);
		em.getTransaction().commit();
		em.close();
		return true;
	}

	@Override
	public boolean removeFavourite(String userId, int postId) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		Favourite favourite = em.find(Favourite.class, new FavouriteKey(userId, postId));
		em.remove(favourite);
		em.getTransaction().commit();
		em.close();
		return true;
	}

	@Override
	public ArrayList<Integer> readFavourites(String userId) {
		EntityManager em = factory.createEntityManager();
		String query = "select postid from favourite where USERID = :userId";
		ArrayList<Integer> titles = (ArrayList<Integer>) em.createNativeQuery(query).setParameter("userId", userId)
				.getResultList();
		em.close();
		return titles;
	}

	@Override
	public ArrayList<Chats> getTopChats() {
		EntityManager em = factory.createEntityManager();
		// ArrayList<Chats> chats = (ArrayList<Chats>)
		// em.createNativeQuery("select * from "
		// + "(select * from Chats order by POSTED_ON desc FETCH FIRST 20 ROWS
		// ONLY) "
		// + "order by posted_on", Chats.class)
		// .getResultList();
		String topChatsQuery = "select * from "
				+ "(select * from Chats order by POSTED_ON desc) where rownum < 20 order by posted_on";
		ArrayList<Chats> chats = (ArrayList<Chats>) em.createNativeQuery(topChatsQuery, Chats.class).getResultList();
		em.close();
		return chats;
	}

	@Override
	public int chatAdd(NewChat newChat) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		int result = em
				.createNativeQuery(
						"insert into chats values((select max(chat_id)+1 from chats)," + ":message, sysdate, :userid )")
				.setParameter("userid", newChat.getUserId()).setParameter("message", newChat.getMessage())
				.executeUpdate();
		em.getTransaction().commit();
		em.close();
		return result;
	}

}
