package edu.ucsb.cs176b.aggregator;


import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.auth.RequestToken; 
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences; 
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import edu.ucsb.cs176b.models.AlertDialogManager;
import edu.ucsb.cs176b.models.ConnectionDetector;
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

	private final String TAG = "TwitterFeedFragment";
	// Twitter oauth urls
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";	

	// Preference Constants
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	// Buttons
	private Button btnLoginTwitter;
	private Button btnRefresh;
	private Button btnLogoutTwitter;

	private PostAdapter postAdapter;
	private ListView postList;

	// Twitter
	private static Twitter twitter;
	private static RequestToken requestToken;
	
	// Shared Preferences
	private static SharedPreferences twitterPreferences;
	
	// Internet Connection detector
	private ConnectionDetector cd;
	
	// Alert Dialog Manager
	private AlertDialogManager alert = new AlertDialogManager();
	
	//List
	private List<Post> posts;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		posts = new ArrayList<Post>();
		
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(TwitterFeedActivity.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}

		btnLoginTwitter = (Button) findViewById(R.id.btnLoginTwitter);
		btnRefresh = (Button) findViewById(R.id.btnRefresh);
		btnLogoutTwitter = (Button) findViewById(R.id.btnLogoutTwitter);

		postList = (ListView) findViewById(R.id.twit_post_list);
		postAdapter = new PostAdapter(this, R.layout.list_twitter_post, posts);
		postList.setAdapter(postAdapter);

		twitterPreferences = getApplicationContext().getSharedPreferences("TwitterPrefs", 0);


		btnLoginTwitter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Call login twitter function
				loginToTwitter();
			}
		});

		btnRefresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new GetTwitterPosts().execute(twitter);
			}
		});

		btnLogoutTwitter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Call logout twitter function
				logoutFromTwitter();
			}
		});
		

		if (!isTwitterLoggedInAlready()) {
			Uri uri = getIntent().getData();
			if (uri != null && uri.toString().startsWith(TWIT_URL)) {
				// oAuth verifier
				String verifier = uri
						.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

				try {
					// Get the access token
					AccessToken accessToken = twitter.getOAuthAccessToken(
							requestToken, verifier);

					// Shared Preferences
					Editor e = twitterPreferences.edit();

					e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
					e.putString(PREF_KEY_OAUTH_SECRET,
							accessToken.getTokenSecret());

					// Store login status - true
					e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
					e.commit(); // save changes

				} catch (Exception e) {
					// Check log for login errors
					Log.e(TAG, "Twitter Login Error");
				}
			}
		} else {
			// Hide login button
			btnLoginTwitter.setVisibility(View.GONE);

			// Show Update Twitter
			btnRefresh.setVisibility(View.VISIBLE);
			btnLogoutTwitter.setVisibility(View.VISIBLE);
			
			new GetTwitterPosts().execute(twitter);
		}
	}

	private void loginToTwitter() {
		if (!isTwitterLoggedInAlready()) {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
			Configuration configuration = builder.build();
			
			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {
				requestToken = twitter.getOAuthRequestToken(TWIT_URL);
				this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		} else {
			
			// Hide login button
			btnLoginTwitter.setVisibility(View.GONE);

			// Show Update Twitter
			btnRefresh.setVisibility(View.VISIBLE);
			btnLogoutTwitter.setVisibility(View.VISIBLE);
			
			new GetTwitterPosts().execute(twitter);
		}
	}
	
	private void logoutFromTwitter() {
		// Clear the shared preferences
		Editor e = twitterPreferences.edit();
		e.remove(PREF_KEY_OAUTH_TOKEN);
		e.remove(PREF_KEY_OAUTH_SECRET);
		e.remove(PREF_KEY_TWITTER_LOGIN);
		e.commit();

		btnLogoutTwitter.setVisibility(View.GONE);
		btnRefresh.setVisibility(View.GONE);
		postList.setVisibility(View.GONE);
		
		btnLoginTwitter.setVisibility(View.VISIBLE);
	}

	private boolean isTwitterLoggedInAlready() {
		// return twitter login status from Shared Preferences
		return twitterPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	}

	protected void onResume() {
		super.onResume();
	}
	
	class GetTwitterPosts extends AsyncTask<Twitter, Void, ArrayList<Post>> {

		private ProgressDialog pDialog;
		
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TwitterFeedActivity.this);
			pDialog.setMessage("Getting twitter timeline");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected ArrayList<Post> doInBackground(Twitter... twitters) {
		
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
				builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
				
				// Access Token 
				String access_token = twitterPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
				// Access Token Secret
				String access_token_secret = twitterPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
				
				AccessToken accessToken = new AccessToken(access_token, access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
				
				// Update status
				ResponseList<twitter4j.Status> response = twitter.getHomeTimeline();

				for (twitter4j.Status status : response) {
					posts.add(new TwitterPost(status));
					Log.v(TAG, status.getUser().getName());
				}
				
			} catch (TwitterException e) {
				Log.e("Twitter Update Error", e.getMessage());
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(ArrayList<Post> result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			postAdapter.notifyDataSetChanged();
		}
	}
}
