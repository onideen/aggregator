package edu.ucsb.cs176b.models;

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
	private String title;
	
	/**
	 * The actual message given in the post
	 */
	private String message;

	/**
	 * The time the post was posted
	 */
	private String updatedTime;
	
	private String imageUrl = null;
	private long countComment;

	private String profilePictureUrl = null;
	
	protected void setUserId(String userId){
		this.userId = userId;
	}
	public String getUserId(){
		return userId;
	}
	
	protected void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return title;
	}
	
	protected void setMessage(String message){
		this.message = message;
		
	}
	public String getMessage(){
		return message;
	}
	
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
	
	protected void setCountCommet(long l){
		this.countComment = l;
	}
	public long getCountComment(){
		return countComment;
	}
	
	protected void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
		
	}
	public String getImageUrl(){
		return imageUrl;
	}
	
	protected void setProfilePictureUrl(String url) {
		this.profilePictureUrl = url;
	}
	
	public String getProfilePictureUrl() {
		return profilePictureUrl;
	}
}
