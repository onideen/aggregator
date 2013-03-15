package edu.ucsb.cs176b.aggregator;

public abstract class Post {
	
	private String userId;
	private String title;
	private String message;
	private String updatedTime;
	private String imageUrl = null;

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
	
	protected void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
		
	}
	public String getImageUrl(){
		return imageUrl;
	}
}
