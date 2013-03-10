package edu.ucsb.cs176b.aggregator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public class MainActivity extends FragmentActivity {

	private static final int SPLASH = 0;
	private static final int FACEBOOK_FEED = 1;
	private static final int TWITTER_FEED = 2;
	private static final int SETTINGS = 3;
	private static final int FRAGMENT_COUNT = SETTINGS + 1;

	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
	private MenuItem settings;
	private MenuItem facebookFeed;
	private MenuItem twitterFeed;
	private boolean isResumed = false;
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		FragmentManager fm = getSupportFragmentManager();
		fragments[SPLASH] = fm.findFragmentById(R.id.splashFragment);
		fragments[FACEBOOK_FEED] = fm.findFragmentById(R.id.facebookFeedFragment);
		fragments[TWITTER_FEED] = fm.findFragmentById(R.id.twitterFeedFragment);
		fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);

		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			transaction.hide(fragments[i]);
		}
		transaction.commit();
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		isResumed = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		Session session = Session.getActiveSession();

		if (session != null && session.isOpened()) {
			// if the session is already open, try to show the selection
			// fragment
			showFragment(FACEBOOK_FEED, false);
		} else {
			// otherwise present the splash screen and ask the user to login.
			showFragment(SPLASH, false);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// only add the menu when the selection fragment is showing
		if (fragments[SPLASH].isHidden()) {
			if (menu.size() == 0) {
				facebookFeed = menu.add(R.string.facebook_feed);
				twitterFeed = menu.add(R.string.twitter_feed);
				settings = menu.add(R.string.settings);
			}
			return true;
		} else {
			menu.clear();
			settings = null;
			facebookFeed = null;
			twitterFeed = null;
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.equals(twitterFeed)) {
			showFragment(TWITTER_FEED, true);
			return true;
		}
		else if (item.equals(facebookFeed)) {
			showFragment(FACEBOOK_FEED, true);
			return true;
		}
		else if (item.equals(settings)) {
			showFragment(SETTINGS, true);
			return true;
		}
		return false;
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (isResumed) {
			FragmentManager manager = getSupportFragmentManager();
			int backStackSize = manager.getBackStackEntryCount();
			for (int i = 0; i < backStackSize; i++) {
				manager.popBackStack();
			}
			// check for the OPENED state instead of session.isOpened() since
			// for the
			// OPENED_TOKEN_UPDATED state, the selection fragment should already
			// be showing.
			if (state.equals(SessionState.OPENED)) {
				showFragment(FACEBOOK_FEED, false);
			} else if (state.isClosed()) {
				showFragment(SPLASH, false);
			}
		}
	}

	private void showFragment(int fragmentIndex, boolean addToBackStack) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			if (i == fragmentIndex) {
				transaction.show(fragments[i]);
			} else {
				transaction.hide(fragments[i]);
			}
		}
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}
	
	
	protected void onNewIntent(Intent intent) { 
	    super.onNewIntent(intent); 
	    //get the retrieved data 
	    Uri twitURI = intent.getData(); 
	    //make sure the url is correct 
	    if(twitURI!=null && twitURI.toString().startsWith(TwitterFeedFragment.TWIT_URL)) 
	    { 
	        //is verifcation - get the returned data 
	        String oaVerifier = twitURI.getQueryParameter("oauth_verifier"); 
	  
	    } 
	}
}
