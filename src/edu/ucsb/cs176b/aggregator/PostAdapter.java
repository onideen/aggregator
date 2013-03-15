package edu.ucsb.cs176b.aggregator;

import java.util.List;

import com.facebook.widget.ProfilePictureView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PostAdapter extends ArrayAdapter<Post> {

	private final String TAG = "PostAdapter";
	private int resource;

	public PostAdapter(Context context, int resource, List<Post> posts) {
		super(context, resource, posts);
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout postView;
		Post post = getItem(position);

		// Inflate view
//		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			postView = (LinearLayout) vi.inflate(resource, null);
//		} 
//		else {
//			postView = (LinearLayout) convertView;
//		}
		// FInd all elements in view
		ProfilePictureView profilePictureView = (ProfilePictureView)postView.findViewById(R.id.selection_profile_pic);
		ImageView post_picture = (ImageView) postView.findViewById(R.id.post_picture);
		TextView postTitle = (TextView) postView.findViewById(R.id.post_title);
		TextView postMessage = (TextView) postView.findViewById(R.id.post_message);
		TextView countLikes = (TextView) postView.findViewById(R.id.count_likes);
		TextView countComment = (TextView) postView.findViewById(R.id.count_comments);
		TextView updatedTime = (TextView) postView.findViewById(R.id.updatedTime);

		profilePictureView.setCropped(true);
		profilePictureView.setProfileId(post.getUserId());
		postTitle.setText(post.getTitle());

		if (post.getImageUrl() != null){
			Log.i(TAG, "Downloading: " + post.getImageUrl());
			post_picture.setTag(post.getImageUrl());
			post_picture.setVisibility(View.VISIBLE);
			new DownloadImageTask(post_picture).execute();
		}
		else {
			Log.i(TAG, "No image... " + post.getTitle());
			post_picture.setVisibility(View.GONE);
		}
		postMessage.setText(post.getMessage());
		countComment.setText(post.getCountComment() + "");
		countLikes.setText(post.getCountLikes() + "");
		updatedTime.setText(post.getUpdatedTime());

		return postView;
	}
}
