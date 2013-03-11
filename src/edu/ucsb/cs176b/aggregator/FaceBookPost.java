package edu.ucsb.cs176b.aggregator;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Picture;
import android.util.Log;

public class FaceBookPost extends Post {

	private final String TAG = "FaceBookPost";

	public static Post getPost(JSONObject jsonObject) throws JSONException {
		String type = jsonObject.getString("type");
		if (type.equals("status") || type.equals("photo")) {
			return new FaceBookPost(jsonObject);
		}
		
		return null;
	}

	public FaceBookPost(JSONObject jsonObject) throws JSONException {
		
		String userId = jsonObject.getJSONObject("from").getString("id");
		String postId = jsonObject.getString("id");
		String name = jsonObject.getJSONObject("from").getString("name");
				
		int countComment = jsonObject.getJSONObject("comments").getInt("count");
		String updatedTime = jsonObject.getString("created_time");

		try {
			setCountLikes(jsonObject.getJSONObject("likes").getInt("count"));
		}catch (JSONException e){
			Log.e(TAG, "Likes not found: " + jsonObject.getString("type"));
		}
		try {

			setPicture(jsonObject.getString("picture"));

		} catch (Exception e) {
			setPicture(null);
		}

		try {
			setMessage(jsonObject.getString("message"));
		} catch (Exception e) {
			setMessage(null);
		}

		setTitle(name);
		setUserId(userId);
//		setCountLikes(countLikes);
		setCountCommet(countComment);
		setUpdatedTime(updatedTime);

		Log.i(TAG, postId);
		Log.i(TAG, userId + "");
		Log.i(TAG, name);
		// Log.i(TAG, message);
//		Log.i(TAG, countLikes + "");
		Log.i(TAG, countComment + "");
		// Log.i(TAG, post_picture);
		Log.i(TAG, updatedTime);
		// TODO Auto-generated constructor stub

	}
}
