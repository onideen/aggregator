package edu.ucsb.cs176b.aggregator;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import twitter4j.Twitter; 
import twitter4j.auth.RequestToken; 
import android.content.SharedPreferences; 
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
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
import android.view.View.OnClickListener;
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
/*
	private static final String TWITTER_CONSUMER_KEY = "m2X7d6mzJnrkZeOjysfMw";
	private static final String TWITTER_CONSUMER_SECRET = "eEwPHKfSu5VomMLxlSFi6bsvoWLJLKf2GeaSZjrOCt0";
	public final static String TWIT_URL = "aggregator:///"; 
	
	private SharedPreferences twitterPrefs;
	
	private final String TAG = "TwitterFeedFragment";
	private Button btnLoginTwitter;
	private Twitter twitter;
	private RequestToken requestToken;
*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.twitterfeed, container, false);
		return view;

	}
	/*	
		if(twitterPrefs.getString("user_token", null)==null) { 
			View view = inflater.inflate(R.layout.twitterfeed, container, false);
			//get a twitter instance for authentication 
			twitter = new TwitterFactory().getInstance(); 
			  
			//pass developer key and secret 
			twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
			
			try 
			{ 
			    //get authentication request token 
			    requestToken = twitter.getOAuthRequestToken(TWIT_URL); 
			    
			    Button signIn = (Button) view.findViewById(R.id.twitter_signin);
			    signIn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						twitterLogin();
					}

				});
			} 
			catch(TwitterException te) { Log.e(TAG, "TE " + te.getMessage()); } 
			
			
			return view;
			
		}else { 
		    //user preferences are set - get timeline 
		    setupTimeline();
		    return null;
		} 

	}

	private void setupTimeline() {
		// TODO Auto-generated method stub
		
	}

	private void twitterLogin() {
		String authURL = requestToken.getAuthenticationURL();
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authURL)));	
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	//	twitterPrefs = getActivity().getSharedPreferences("Twit",0);
			
	}
 */
	
 

}
