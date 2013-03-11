package edu.ucsb.cs176b.aggregator;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.facebook.widget.ProfilePictureView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			postView = (LinearLayout) vi.inflate(resource, null);
		} else {
			postView = (LinearLayout) convertView;
		}
		// FInd all elements in view
		ProfilePictureView profilePictureView = (ProfilePictureView)postView.findViewById(R.id.selection_profile_pic);
		ImageView post_picture = (ImageView) postView.findViewById(R.id.post_picture);
		TextView postTitle = (TextView) postView.findViewById(R.id.post_title);
		TextView postMessage = (TextView) postView.findViewById(R.id.post_message);
		TextView countLikes = (TextView) postView.findViewById(R.id.countLikes);
		TextView countComment = (TextView) postView.findViewById(R.id.countComment);
		ImageView likeImage = (ImageView) postView.findViewById(R.id.likeImage);
		ImageView commentImage = (ImageView) postView.findViewById(R.id.commentImg);
		TextView updatedTime = (TextView) postView.findViewById(R.id.updatedTime);

		profilePictureView.setCropped(true);

		profilePictureView.setProfileId(post.getUserId());
		postTitle.setText(post.getTitle());

		if (post.getImageUrl() != null){
			
			post_picture.setTag(post.getImageUrl());
			new DownloadImageTask().execute(post_picture);
		}
		postMessage.setText(post.getMessage());
		countComment.setText(post.getCountComment() + "");
		countLikes.setText(post.getCountLikes() + "");
		updatedTime.setText(post.getUpdatedTime());

		return postView;
	}

}