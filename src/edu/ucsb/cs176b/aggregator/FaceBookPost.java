package edu.ucsb.cs176b.aggregator;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Picture;
import android.util.Log;

public class FaceBookPost extends Post {

	private final String TAG = "FaceBookPost";


	public FaceBookPost(JSONObject jsonObject) throws JSONException {
		//hent in post type
		String type = jsonObject.getString("type");
		if (!type.equals("status") ){
			Log.i(TAG, type);
			return;
		}
		else{
			String userId = jsonObject.getJSONObject("from").getString("id");
			String postId = jsonObject.getString("id"); 
			String name = jsonObject.getJSONObject("from").getString("name"); 
			int countLikes = jsonObject.getJSONObject("likes").getInt("count"); 
			int countComment = jsonObject.getJSONObject("comments").getInt("count"); 
			String updatedTime = jsonObject.getString("created_time");

			try{
				
				setPicture(jsonObject.getString("picture"));
				

			}
			catch(Exception e){
				setPicture(null);
			}

			try{
				setMessage( jsonObject.getString("message"));
			}
			catch(Exception e){
				setMessage(null);
			}

			setTitle(name);
			setUserId(userId);
			setCountLikes(countLikes);
			setCountCommet(countComment); 
			setUpdatedTime(updatedTime);

			Log.i(TAG, postId);
			Log.i(TAG, userId + "");
			Log.i(TAG, name);
			//Log.i(TAG, message);
			Log.i(TAG, countLikes + "");
			Log.i(TAG, countComment + "");
			//Log.i(TAG, post_picture);
			Log.i(TAG, updatedTime);
			// TODO Auto-generated constructor stub


		}



	}
}

