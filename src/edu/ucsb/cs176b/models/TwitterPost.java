package edu.ucsb.cs176b.models;

import android.util.Log;
import twitter4j.Status;

public class TwitterPost extends Post {

	private final static String TAG = "TwitterPost";
	
	public TwitterPost(Status status) {

		
//		Log.v(TAG, status.getUser().getName());
//		Log.v(TAG, status.getText());
//		Log.v(TAG, status.getRetweetCount() + "");
//		Log.v(TAG, status.getUser().getMiniProfileImageURL());
//		Log.v(TAG, status.getCreatedAt().toGMTString());
//		
		
		
		setTitle(status.getUser().getName());
		setMessage(status.getText());
		
		//Retweets
		setCountCommet(status.getRetweetCount());
		Log.v(TAG, "hm");
		if(status.getMediaEntities() != null) {
			Log.v(TAG, "yo");
		}
		
		setProfilePictureUrl(status.getUser().getBiggerProfileImageURL());
		setUpdatedTime(status.getCreatedAt().toGMTString());
		
	}

}
