package edu.ucsb.cs176b.aggregator;

import com.facebook.*;
import com.facebook.model.*;

import edu.ucsb.cs176b.aggregator.R;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;




public class MainActivity extends Activity {

	private TextView textViewResults;
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    // start Facebook Login
	    Session.openActiveSession(this, true, new Session.StatusCallback() {
	    	
	      // callback when session changes state
	      @Override
	      public void call(Session session, SessionState state, Exception exception) {
	        if (session.isOpened()) {
	        	TextView welcome = (TextView) findViewById(R.id.welcome);
	        	welcome.setText("Hello " + "!");

	          // make request to the /me API
	          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

	            // callback after Graph API response with user object
	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	              if (user != null) {
	                TextView welcome = (TextView) findViewById(R.id.welcome);
	                welcome.setText("Hello " + user.getName() + "!");
	                doBatchRequest();
	              }
	            }
	          });
	        }
	      }
	    });
	  }

	  @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	  }
	  
	  private void doBatchRequest() {
		    textViewResults = (TextView) this.findViewById(R.id.textViewResults);
		    textViewResults.setText("");

		    String[] requestIds = {"me"};

		    RequestBatch requestBatch = new RequestBatch();
		    for (final String requestId : requestIds) {
		        requestBatch.add(new Request(Session.getActiveSession(), 
		                requestId, null, null, new Request.Callback() {
		            public void onCompleted(Response response) {
		                GraphObject graphObject = response.getGraphObject();
		                String s = textViewResults.getText().toString();
		                if (graphObject != null) {
		                    if (graphObject.getProperty("id") != null) {
		                        s = s + String.format("%s: %s\n", 
		                                graphObject.getProperty("id"), 
		                                graphObject.getProperty("home"));
		                        
		                    }
		                }
		                textViewResults.setText(s);
		            }
		        }));
		    }
		    requestBatch.executeAsync();
		}

}
