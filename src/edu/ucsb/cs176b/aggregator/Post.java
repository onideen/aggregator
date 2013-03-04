package edu.ucsb.cs176b.aggregator;

import java.util.Date;

import android.graphics.Picture;
//import android.text.GetChars;

public abstract class Post {

	/**
	 * A class that contains all the information of a post we want to view.
	 * 
	 * TODO: finish it
	 */
	
	
	
	
	/**
	 * The user that posted the post and the owner of the wall it was posted on. 
	 */
	private String title;
	protected void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return title;
	}
	
	private Picture profilePic;
	protected void setProfilePic(Picture profilePic){
		this.profilePic = profilePic;
		
	}
	public Picture getProfilePic(){
		return profilePic;
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
	private Date posted_on;
	protected void setPosted_on(Date date){
		this.posted_on = date;
		
	}
	public Date getPosted_on(){
		return posted_on;
	}
	

	private Picture picture;
	protected void setPicture(Picture picture){
		this.picture = picture;
		
	}
	public Picture getPicture(){
		return picture;
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
	
	//public void setTitle(String t) {
	//	title = t;
	//}
}
