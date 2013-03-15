package edu.ucsb.cs176b.aggregator;


import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter; 
import twitter4j.auth.RequestToken; 
import android.app.Activity;
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
public class TwitterFeedActivity extends Activity {

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



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		posts = new ArrayList<Post>();

		//get the preferences for the app 
		twitterPrefs = getSharedPreferences("TwitterPrefs", 0); 
		setContentView(R.layout.twitterfeed);


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
			btnLoginTwitter = (Button) findViewById(R.id.twitter_signin);
			btnLoginTwitter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String authURL = requestToken.getAuthenticationURL(); 
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authURL))); 					
				}
			});

		} else {
			getTwitterTimeline();
		}

		postList = (ListView) findViewById(R.id.twit_post_list);
		postAdapter = new PostAdapter(this, R.layout.list_twitter_post, posts);
		
		Log.v(TAG, postList.toString());
		postList.setAdapter(postAdapter); // GIR NULLPOINTER
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		Log.i(TAG, "onNewIntent");
		Uri twitURI = intent.getData(); 
		//make sure the url is correct 
		if(twitURI!=null && twitURI.toString().startsWith(TWIT_URL)) 
		{ 
			//is verifcation - get the returned data 

			String oaVerifier = twitURI.getQueryParameter("oauth_verifier"); 
			//attempt to retrieve access token 
			try
			{ 
				//try to get an access token using the returned data from the verification page 
				Log.v(TAG, "hit");
				AccessToken accToken = twitter.getOAuthAccessToken(requestToken, oaVerifier); 

				//add the token and secret to shared prefs for future reference 
				twitterPrefs.edit() 
				.putString("user_token", accToken.getToken()) 
				.putString("user_secret", accToken.getTokenSecret()) 
				.commit(); 
				twitter.setOAuthAccessToken(accToken);
				Log.v(TAG, "men ikke lenger");				
				getTwitterTimeline(); 
			} 
			catch (TwitterException te) 
			{ Log.e(TAG, "Failed to get access token: " + te.getMessage()); } 
		} 
	}


}
