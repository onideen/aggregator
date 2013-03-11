package edu.ucsb.cs176b.aggregator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class FaceBookPost extends Post {

	private final static String TAG = "FaceBookPost";

	public static Post getPost(JSONObject jsonObject) {
		String type = "";
		try {
			type = jsonObject.getString("type");
		} catch (JSONException e) {
			Log.e(TAG, e.toString());
		}
		if (type.equals("status") || type.equals("photo")) {
			return new FaceBookPost(jsonObject, type);
		}

		return null;
	}

	public FaceBookPost(JSONObject jsonObject, String type) {

		try {
			setUserId(jsonObject.getJSONObject("from").getString("id"));
			setTitle(jsonObject.getJSONObject("from").getString("name"));
			setCountCommet(jsonObject.getJSONObject("comments").getInt("count"));
			setCountLikes(jsonObject.getJSONObject("likes").getInt("count"));
			setPicture(jsonObject.getString("picture"));
			setMessage(jsonObject.getString("message"));
			setUpdatedTime(jsonObject.getString("created_time"));
		} catch (JSONException e1) {
			Log.e(TAG, e1.toString());
		}

	}
}
