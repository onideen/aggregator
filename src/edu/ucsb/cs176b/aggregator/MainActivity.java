package edu.ucsb.cs176b.aggregator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.facebook.*;
import com.facebook.Session.StatusCallback;
import com.facebook.model.*;
import com.facebook.widget.LoginButton;

import edu.ucsb.cs176b.aggregator.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	private FacebookFragment facebookFragment;
	

	private static final String applicationID = "133312576839682";
	private static final String TAG = "MainActivity";
	private static final String PENDING_REQUEST_BUNDLE_KEY = null;
	private TextView textViewResults;
	private Session session;
	private boolean pendingRequest;

	
	private UiLifecycleHelper uiHelper;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);

	    setContentView(R.layout.main);
		
		LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
		authButton.setReadPermissions(Arrays.asList("read_stream"));
	    
		
		
		
		
	}
	
	 private void sendRequest() {
		textViewResults.setText("HELLEDUSAN");

		String[] requestIds = { "me" };

		List<Request> requests = new ArrayList<Request>();
		for (final String requestId : requestIds) {
			requests.add(new Request(session, requestId, null, null,
					new Request.Callback() {
						public void onCompleted(Response response) {
							
							GraphObject graphObject = response.getGraphObject();
							FacebookRequestError error = response.getError();
							//String s = textViewResults.getText().toString();
							String s = "";
							if (graphObject != null) {
								if (graphObject.getProperty("id") != null) {
									s = s
											+ String.format(
													"%s: %s\n",
													graphObject
															.getProperty("id"),
													graphObject
															.getProperty("name"));
								} else {
									s = s
											+ String.format(
													"%s: <no such id>\n",
													requestId);
								}
							} else if (error != null) {
								s = s
										+ String.format("Error: %s",
												error.getErrorMessage());
							}
							//textViewResults.setText(s);
						}
					}));
		}
		pendingRequest = false;
		Request.executeBatchAndWait(requests);

	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	    }
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}
		
		uiHelper.onResume();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}	
}
