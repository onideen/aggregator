package edu.ucsb.cs176b.aggregator;


import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter; 
import twitter4j.auth.RequestToken; 
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences; 
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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
	
	// lbl update
	private TextView lblUpdate;
	private TextView lblUserName;

	// Progress dialog
	private ProgressDialog pDialog;

	// Twitter
	private static Twitter twitter;
	private static RequestToken requestToken;
	
	// Shared Preferences
	private static SharedPreferences twitterPreferences;
	
	// Internet Connection detector
	private ConnectionDetector cd;
	
	// Alert Dialog Manager
	private AlertDialogManager alert = new AlertDialogManager();
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
			// stop executing code by return
			return;
		}
		
		// Check if twitter keys are set
		if(TWITTER_CONSUMER_KEY.trim().length() == 0 || TWITTER_CONSUMER_SECRET.trim().length() == 0){
			// Internet Connection is not present
			alert.showAlertDialog(TwitterFeedActivity.this, "Twitter oAuth tokens", "Please set your twitter oauth tokens first!", false);
			// stop executing code by return
			return;
		}

		// All UI elements
		btnLoginTwitter = (Button) findViewById(R.id.btnLoginTwitter);
		btnRefresh = (Button) findViewById(R.id.btnRefresh);
		btnLogoutTwitter = (Button) findViewById(R.id.btnLogoutTwitter);
		lblUserName = (TextView) findViewById(R.id.lblUserName);

		// Shared Preferences
		twitterPreferences = getApplicationContext().getSharedPreferences(
				"TwitterPrefs", 0);

		/**
		 * Twitter login button click event will call loginToTwitter() function
		 * */
		btnLoginTwitter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Call login twitter function
				loginToTwitter();
			}
		});

		/**
		 * Button click event to Update Status, will call updateTwitterStatus()
		 * function
		 * */
		btnRefresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Call update status function
				getTwitterPosts();
			}

		});

		/**
		 * Button click event for logout from twitter
		 * */
		btnLogoutTwitter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Call logout twitter function
				logoutFromTwitter();
			}
		});
		

		/** This if conditions is tested once is
		 * redirected from twitter page. Parse the uri to get oAuth
		 * Verifier
		 * */
		
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

					// After getting access token, access token secret
					// store them in application preferences
					e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
					e.putString(PREF_KEY_OAUTH_SECRET,
							accessToken.getTokenSecret());
					// Store login status - true
					e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
					e.commit(); // save changes

					Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

					// Hide login button
					btnLoginTwitter.setVisibility(View.GONE);

					// Show Update Twitter
					lblUpdate.setVisibility(View.VISIBLE);
					btnRefresh.setVisibility(View.VISIBLE);
					btnLogoutTwitter.setVisibility(View.VISIBLE);
					
					// Getting user details from twitter
					// For now i am getting his name only
					long userID = accessToken.getUserId();
					User user = twitter.showUser(userID);
					String username = user.getName();
					
					// Displaying in xml ui
					lblUserName.setText(Html.fromHtml("<b>Welcome " + username + "</b>"));
				} catch (Exception e) {
					// Check log for login errors
					Log.e("Twitter Login Error", "> " + e.getMessage());
				}
			}
		}
		
		//getTwitterPosts();

	}

	private void getTwitterPosts() {
		posts = new ArrayList<Post>();
		
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
			twitter.getHomeTimeline();
		} catch (TwitterException e) {
			// Error in updating status
			Log.d(TAG, e.getMessage());
		}
		
	}

	/**
	 * Function to login twitter
	 * */
	private void loginToTwitter() {
		// Check if already logged in
		if (!isTwitterLoggedInAlready()) {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
			Configuration configuration = builder.build();
			
			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {
				requestToken = twitter.getOAuthRequestToken(TWIT_URL);
//				requestToken = new MyTask().execute(twitter).get();
				this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(requestToken.getAuthenticationURL())));
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		} else {
			// user already logged into twitter
			Toast.makeText(getApplicationContext(),
					"Already Logged into twitter", Toast.LENGTH_LONG).show();
			
			
		}
	}

	/**
	 * Function to update status
	 * */
	class updateTwitterStatus extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TwitterFeedActivity.this);
			pDialog.setMessage("Updating to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			Log.d("Tweet Text", "> " + args[0]);
			String status = args[0];
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
				twitter4j.Status response = twitter.updateStatus(status);
				
				Log.d("Status", "> " + response.getText());
			} catch (TwitterException e) {
				// Error in updating status
				Log.d("Twitter Update Error", e.getMessage());
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(),
							"Status tweeted successfully", Toast.LENGTH_SHORT)
							.show();
				}
			});
		}

	}

	/**
	 * Function to logout from twitter
	 * It will just clear the application shared preferences
	 * */
	private void logoutFromTwitter() {
		// Clear the shared preferences
		Editor e = twitterPreferences.edit();
		e.remove(PREF_KEY_OAUTH_TOKEN);
		e.remove(PREF_KEY_OAUTH_SECRET);
		e.remove(PREF_KEY_TWITTER_LOGIN);
		e.commit();

		// After this take the appropriate action
		// I am showing the hiding/showing buttons again
		// You might not needed this code
		btnLogoutTwitter.setVisibility(View.GONE);
		btnRefresh.setVisibility(View.GONE);
		lblUpdate.setVisibility(View.GONE);
		lblUserName.setText("");
		lblUserName.setVisibility(View.GONE);

		btnLoginTwitter.setVisibility(View.VISIBLE);
	}

	/**
	 * Check user already logged in your application using twitter Login flag is
	 * fetched from Shared Preferences
	 * */
	private boolean isTwitterLoggedInAlready() {
		// return twitter login status from Shared Preferences
		return twitterPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	}

	protected void onResume() {
		super.onResume();
	}

}
