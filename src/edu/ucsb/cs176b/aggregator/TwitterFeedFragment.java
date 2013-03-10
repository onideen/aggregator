package edu.ucsb.cs176b.aggregator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

/**
 * Fragment that represents the feed for aggregation
 */
public class TwitterFeedFragment extends Fragment {

	private final String TAG = "TwitterFeedFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.twitterfeed, container, false);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
/*
		Log.i(TAG, "Kom hit");
		try {
			// The factory instance is re-useable and thread safe.
			Twitter twitter = TwitterFactory.getSingleton();
			twitter.setOAuthConsumer("505M9IoZ3qkIoBGnJIi8Sg", "nlQ3ejvmmVDitUhpgLyArk7HjCO7Wk9R1Un8YTBL8M");
			RequestToken requestToken;
			requestToken = twitter.getOAuthRequestToken();
			AccessToken accessToken = null;
			
			while (null == accessToken) {
				Log.i(TAG, requestToken.getAuthorizationURL());
				String pin = "";
				try {
					if (pin.length() > 0) {
						accessToken = twitter.getOAuthAccessToken(requestToken,
								pin);
					} else {
						accessToken = twitter.getOAuthAccessToken();
					}
				} catch (TwitterException te) {
					if (401 == te.getStatusCode()) {
						Log.e(TAG, "Unable to get the access token.");
					} else {
						Log.e(TAG, te.toString());
					}
				}
			}
			// persist to the accessToken for future reference.
			Status status = twitter.updateStatus("Ny update?");
			System.out.println("Successfully updated the status to ["
					+ status.getText() + "].");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
		
*/
	}

}
