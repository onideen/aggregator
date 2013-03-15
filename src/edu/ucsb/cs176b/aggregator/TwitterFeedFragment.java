package edu.ucsb.cs176b.aggregator;


import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter; 
import twitter4j.auth.RequestToken; 
import android.content.SharedPreferences; 
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
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

import edu.ucsb.cs176b.models.Post;
import edu.ucsb.cs176b.models.PostAdapter;
import edu.ucsb.cs176b.models.TwitterPost;

/**
 * Fragment that represents the feed for aggregation
 */
public class TwitterFeedFragment extends Fragment {

	private static final String TWITTER_CONSUMER_KEY = "m2X7d6mzJnrkZeOjysfMw";
	private static final String TWITTER_CONSUMER_SECRET = "eEwPHKfSu5VomMLxlSFi6bsvoWLJLKf2GeaSZjrOCt0";
	public final static String TWIT_URL = "aggregator://twitter"; 

	private SharedPreferences twitterPrefs;

	private final String TAG = "TwitterFeedFragment";
	private Button btnLoginTwitter;
	private Twitter twitter;
	private RequestToken requestToken;
	private List<Post> posts;
	private PostAdapter postAdapter;
	private ListView postList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		//find out if the user preferences are set 

		//no user preferences so prompt to sign in 
		View view = inflater.inflate(R.layout.twitterfeed, container, false);
		btnLoginTwitter = (Button)view.findViewById(R.id.twitter_signin);
		
		btnLoginTwitter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String authURL = requestToken.getAuthenticationURL(); 
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authURL)));
			}
		});

/*****NOE FEIL SOM KOMMER NAR DISSE LASTES INN */
		//postAdapter = new PostAdapter(getActivity(), R.layout.list_twitter_post, posts);
	//	postList.setAdapter(postAdapter);
		
		
		return view;

	}

	private void getTwitterTimeline() {
		Log.v(TAG, "Setup Timeline");
		
		try {
			
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
			  .setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET)
			  .setOAuthAccessToken(twitterPrefs.getString("user_token", ""))
			  .setOAuthAccessTokenSecret(twitterPrefs.getString("user_secret", ""));
			Log.v(TAG, "userToken: " + twitterPrefs.getString("user_token", ""));
			Log.v(TAG, "userSecret: " + twitterPrefs.getString("user_secret", ""));
			TwitterFactory tf = new TwitterFactory(cb.build());
			Twitter twittertime = tf.getInstance();
			
			Log.v(TAG, "Twitter screen name: " + twittertime.getScreenName());
			
			ResponseList<Status> statuses = twittertime.getHomeTimeline();
			
			for (Status status : statuses) {
				Post post = new TwitterPost(status);
				if (post != null) posts.add(post);
				
			}
		} catch (Exception e) {
			Log.e(TAG, "Timeline, " + e.getMessage());
		}
		
	}


	public void handleTwitterCallBack(Intent intent) {
		//get the retrieved data 
		Uri twitURI = intent.getData(); 
		//make sure the url is correct 
		if(twitURI!=null && twitURI.toString().startsWith(TwitterFeedFragment.TWIT_URL)) 
		{ 
			//is verifcation - get the returned data 
			String oaVerifier = twitURI.getQueryParameter("oauth_verifier");

			//attempt to retrieve access token 
			try
			{ 
				//try to get an access token using the returned data from the verification page 
				AccessToken accToken = twitter.getOAuthAccessToken(requestToken, oaVerifier); 

				//add the token and secret to shared prefs for future reference 
				twitterPrefs.edit() 
				.putString("user_token", accToken.getToken()) 
				.putString("user_secret", accToken.getTokenSecret()) 
				.commit(); 

				twitter.setOAuthAccessToken(accToken);
				
				getTwitterTimeline(); 
			} 
			catch (TwitterException te) 
			{ Log.e(TAG, "Failed to get access token: " + te.getMessage()); } 
		}
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		posts = new ArrayList<Post>();
		
		//get the preferences for the app 
		twitterPrefs = getActivity().getSharedPreferences("TwitNicePrefs", 0); 
		
		
		if(twitterPrefs.getString("user_token", null)==null) { 
			
			twitter = new TwitterFactory().getInstance();
	
			//pass dev key and secret
			twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
	
			// Try to get request token
			try {
				requestToken = twitter.getOAuthRequestToken(TWIT_URL);
			} catch (TwitterException te){
				Log.e(TAG, "TE " + te.getMessage());
			}
	
			//setup button for click listener 
			
			} else {
			getTwitterTimeline();
		}
		

	}




}
