package edu.ucsb.cs176b.models;

import twitter4j.Status;

public class TwitterPost extends Post {

	
	
	public TwitterPost(Status status) {

		
		setTitle(status.getUser().getName());
		setMessage(status.getText());
		
		//Retweets
		setCountCommet(status.getRetweetCount());
		
		setProfilePictureUrl(status.getUser().getMiniProfileImageURL());
		setUpdatedTime(status.getCreatedAt().toGMTString());
		
	}

}
