package edu.ucsb.cs176b.aggregator;

import java.io.InputStream;
import java.util.Date;

import android.graphics.Picture;
//import android.text.GetChars;
import android.net.Uri;

public abstract class Post {

	/**
	 * A class that contains all the information of a post we want to view.
	 * 
	 * TODO: finish it
	 */
	
	
	
	
	/**
	 * The user that posted the post and the owner of the wall it was posted on. 
	 */
	
	private String userId;
	protected void setUserId(String userId){
		this.userId = userId;
	}
	public String getUserId(){
		return userId;
	}
	
	private String title;
	protected void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return title;
	}
	
	/**
	 * The actual message given in the post
	 */
	private String message;
	protected void setMessage(String message){
		this.message = message;
		
	}
	public String getMessage(){
		return message;
	}
	
	/**
	 * The time the post was posted
	 */
	private String updatedTime;
	protected void setUpdatedTime(String updatedTime){
		this.updatedTime = updatedTime;
		
	}
	public String getUpdatedTime(){
		return updatedTime;
	}
	


	/**
	 * 
	 */
	private int countLikes;
	protected void setCountLikes(int countLikes){
		this.countLikes = countLikes;
		
	}
	public int getCountLikes(){
		return countLikes;
	}
	
	private int countComment;
	protected void setCountCommet(int countComment){
		this.countComment = countComment;
	}
	public int getCountComment(){
		return countComment;
	}
	
//if picture ex post 126979200807258_134911490014029
	private String post_picture;
	protected void setPicture(String picture){
//		this.post_picture = Uri.parse(picture);
		this.post_picture = picture;
		
	}
	public String getPicture(){
		return post_picture;
	}
	
}
