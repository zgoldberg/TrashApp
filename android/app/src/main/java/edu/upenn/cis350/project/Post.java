package edu.upenn.cis350.project;
import java.util.ArrayList;
import java.util.List;

public class Post {
	int coordX;
	int coordY;
	String data;
	String user;
	int upVotes;
	int downVotes;

	//String targetCharSeq = "*abc123*";
	ArrayList<Comment> comments; //each post has own list of comments
	static List<Post> Posts = new ArrayList<Post>();

	//constructor adds post to list
	public Post(int coordX , int coordY, String data, String user, int upVotes, int downVotes, ArrayList<Comment>  comments) {
		this.coordX = coordX;
		this.coordY = coordY;
		this.data = data;
		this.user = user;
		this.upVotes = upVotes;
		this.downVotes = downVotes;
		this.comments = comments;

		Posts.add(this);

	}

	//will hold fd or addy for pic in mongoDB, preface it by targetCharSeq so we know where it is, should be in front
	// can be edited out later
	public String parsePic(String data) {




		return null; //will return FD of pic to be included w/ post
	}


//	public void addCom (Post post, String com) {
//		Comment adder = new Comment(com,0,0);
//		post.comments.add(adder);
//	}
	public void delCom (Post post, Comment com) {
		post.comments.remove(com);
	}
	//upVotes and downVotes on post
	public void upDootPost(Post post) {
		post.upVotes++;

	}
	public void downDootPost(Post post) {
		post.downVotes++;

	}
//	public void upDootComment(Comment c) {
//		c.upVotes++;
//
//	}
//	public void downDootPost(Comment c) {
//		c.downVotes++;
//
//	}


	public void deletePost(User user, int admin, Post post, List<Post> Posts) {
		if(admin == 1) {
			Posts.remove(post);
		}
		if(user.equals(user)) {
			Posts.remove(post);
		}
	}


}
