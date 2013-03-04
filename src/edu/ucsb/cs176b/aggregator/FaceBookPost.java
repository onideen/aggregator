package edu.ucsb.cs176b.aggregator;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Picture;
import android.util.Log;

public class FaceBookPost extends Post {

	private final String TAG = "FaceBookPost";
	
	
	public FaceBookPost(JSONObject jsonObject) throws JSONException {
		String userId = jsonObject.getJSONObject("from").getString("id");
		String postId = jsonObject.getString("id"); 
		String post_picture = jsonObject.getString("picture");
		String name = jsonObject.getJSONObject("from").getString("name"); 
		String message = jsonObject.getString("message");
		int countLikes = jsonObject.getJSONObject("likes").getInt("count"); 
		int countComment = jsonObject.getJSONObject("comments").getInt("count"); 
		
		
		setTitle(name);
		setUserId(userId);
		setMessage(message);
		setCountLikes(countLikes);
		setCountCommet(countComment); 
		setPicture(post_picture);
		
		Log.i(TAG, postId);
		Log.i(TAG, userId + "");
		Log.i(TAG, name);
		Log.i(TAG, message);
		Log.i(TAG, countLikes + "");
		Log.i(TAG, countComment + "");
//		Log.i(TAG, post_picture);
		// TODO Auto-generated constructor stub
	}

}
