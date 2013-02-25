package edu.ucsb.cs176b.aggregator;

import java.util.ArrayList;
import java.util.List;

import com.facebook.*;
import com.facebook.Session.StatusCallback;
import com.facebook.model.*;

import edu.ucsb.cs176b.aggregator.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	private MainFragment mainFragment;
	

	private static final String applicationID = "133312576839682";
	private static final String TAG = "MainActivity";
	private static final String PENDING_REQUEST_BUNDLE_KEY = null;
	private TextView textViewResults;
	private Session session;
	private boolean pendingRequest;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	        mainFragment = new MainFragment();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, mainFragment)
	        .commit();
	    } else {
	        // Or set the fragment from restored state info
	        mainFragment = (MainFragment) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	    }
	}
/*
	private void doRequest() {
		if (this.session.isOpened()) {
			textViewResults.setText("HELLI");
			sendRequest();
		} else {
			StatusCallback callback = new StatusCallback() {
				public void call(Session session, SessionState state,
						Exception exception) {
					Log.e(TAG, "Error: " + exception);
					if (exception != null) {
						new AlertDialog.Builder(MainActivity.this)
								.setTitle(R.string.login_failed_dialog_title)
								.setMessage(exception.getMessage())
								.setPositiveButton(R.string.ok_button, null)
								.show();
						MainActivity.this.session = createSession();
					}
				}
			};
			pendingRequest = true;
			this.session.openForRead(new Session.OpenRequest(this).setCallback(callback));
		}

	}

	
	 @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);

	        outState.putBoolean(PENDING_REQUEST_BUNDLE_KEY, pendingRequest);
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
							String s = textViewResults.getText().toString();
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

	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.session.onActivityResult(this, requestCode, resultCode, data) &&
                pendingRequest &&
                this.session.getState().isOpened()) {
            sendRequest();
        }
    }
	/**
	 * Finds the active session, if active session does not exist or can no
	 * longer be used for {@link Request} it creates a new session
	 * 
	 * @return the active session
	 */
/*
	private Session createSession() {
		Session activSession = Session.getActiveSession();
		if (activSession == null || activSession.getState().isClosed()) {
			activSession = new Session.Builder(this).setApplicationId(applicationID).build();
			Session.setActiveSession(activSession);
		}
		return activSession;
	}
*/
}
