package edu.ucsb.cs176b.aggregator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class FaceBookPost extends Post {

	private final String TAG = "FaceBookPost";
	
	
	public FaceBookPost(JSONObject jsonObject) throws JSONException {
		long personId = jsonObject.getJSONObject("from").getLong("id");
		String postId = jsonObject.getString("id"); 
		String name = jsonObject.getJSONObject("from").getString("name"); 
		String message = jsonObject.getString("message");
		int countLikes = jsonObject.getJSONObject("likes").getInt("count"); 
		int countComment = jsonObject.getJSONObject("comments").getInt("count"); 
		
		
		setTitle(name);
		
		setMessage(message);
		setCountLikes(countLikes);
		setCountCommet(countComment); 
		
		Log.i(TAG, postId);
		Log.i(TAG, personId + "");
		Log.i(TAG, name);
		Log.i(TAG, message);
		Log.i(TAG, countLikes + "");
		Log.i(TAG, countComment + "");
		// TODO Auto-generated constructor stub
	}

}
