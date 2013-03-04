package edu.ucsb.cs176b.aggregator;

import org.json.JSONArray;
import org.json.JSONObject;

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
import android.webkit.WebView.FindListener;
import android.widget.*;
import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.ProfilePictureView;


/**
 * Fragment that represents the feed for aggregation
 */
public class FacebookFeedFragment extends Fragment {
	private String TAG = "FeedFragment";

    private static final Uri M_FACEBOOK_URL = Uri.parse("http://m.facebook.com");

    private static final int REAUTH_ACTIVITY_CODE = 100;
    Post post;
    private TextView postTitle;
    private TextView postMessage;
    private TextView countComment;
    private TextView countLikes;
    private TextView date;

    
    private ProgressDialog progressDialog;
    
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.feed, container, false);

        postTitle = (TextView) view.findViewById(R.id.post_title);
        postMessage = (TextView) view.findViewById(R.id.post_message);
        countLikes = (TextView) view.findViewById(R.id.countLikes);
        countComment = (TextView) view.findViewById(R.id.countComment);
        //date =(TextView) view.findViewById(R.id.posted_on);
        init(savedInstanceState);
        

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REAUTH_ACTIVITY_CODE) {
            uiHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        uiHelper.onSaveInstanceState(bundle);
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


    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (session != null && session.isOpened()) {
            makeFacebookFeedRequest(session);
        }
    }

    private void makeFacebookFeedRequest(final Session session) {
        // Show a progress dialog because sometimes the requests can take a while.
        progressDialog = ProgressDialog.show(getActivity(), "", getActivity().getResources().getString(R.string.progress_dialog_text), true);

    	//Request request = Request.newGraphPathRequest(session, "me/home/", new Request.Callback() {
    	Request request = Request.newGraphPathRequest(session, "576520092360758_594046270608140", new Request.Callback() {
			@Override
			public void onCompleted(Response response) {
				GraphObject newsFeed = response.getGraphObject();
				String s = "";
				if (progressDialog != null) {
					progressDialog.dismiss();
					progressDialog = null;
				}
				if (session == Session.getActiveSession()) {
					//JSONObject obj = newsFeed.getInnerJSONObject();
					
					
					 try{  
						 s += newsFeed.getProperty("data"); // elements in news feed 
//						 JSONArray jsonArray = new JSONArray(s);
//				      Log.i(TAG, "Number of entries " + jsonArray.length());
				      for (int i = 0; i < 1; i++) {
//				    	  Post post = new FaceBookPost( jsonArray.getJSONObject(i));
				    	  post = new FaceBookPost( newsFeed.getInnerJSONObject());
					        Log.i(TAG, i + "");
				      }
					
					 }
					 catch (Exception e) {
					     Log.wtf(TAG, e);
						 e.printStackTrace();
					    }
					
					
					//requestResponse.setText(s);
				}		              
				if (response.getError() != null) {
                    handleError(response.getError());
                }
			}
		});
		request.executeAndWait();
		updateView();
	}


    private void updateView() {
    	postTitle.setText(post.getTitle());
    	postMessage.setText(post.getMessage());
    	countComment.setText(post.getCountComment() + "");
    	countLikes.setText(post.getCountLikes() + "");
    	//date.setText(post.getPosted_on().toString());
	}

	/**
     * Resets the view to the initial defaults.
     */
    private void init(Bundle savedInstanceState) {

        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
//            makeMeRequest(session);
            makeFacebookFeedRequest(session);
        }
    }



    private void handleError(FacebookRequestError error) {
        DialogInterface.OnClickListener listener = null;
        String dialogBody = null;

        if (error == null) {
            dialogBody = getString(R.string.error_dialog_default_text);
        } else {
            switch (error.getCategory()) {
                case AUTHENTICATION_RETRY:
                    // tell the user what happened by getting the message id, and
                    // retry the operation later
                    String userAction = (error.shouldNotifyUser()) ? "" :
                            getString(error.getUserActionMessageId());
                    dialogBody = getString(R.string.error_authentication_retry, userAction);
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, M_FACEBOOK_URL);
                            startActivity(intent);
                        }
                    };
                    break;

                case AUTHENTICATION_REOPEN_SESSION:
                    // close the session and reopen it.
                    dialogBody = getString(R.string.error_authentication_reopen);
                    listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Session session = Session.getActiveSession();
                            if (session != null && !session.isClosed()) {
                                session.closeAndClearTokenInformation();
                            }
                        }
                    };
                    break;

                case SERVER:
                case THROTTLING:
                    // this is usually temporary, don't clear the fields, and
                    // ask the user to try again
                    dialogBody = getString(R.string.error_server);
                    break;

                case BAD_REQUEST:
                    // this is likely a coding error, ask the user to file a bug
                    dialogBody = getString(R.string.error_bad_request, error.getErrorMessage());
                    break;

                case OTHER:
                case CLIENT:
                default:
                    // an unknown issue occurred, this could be a code error, or
                    // a server side issue, log the issue, and either ask the
                    // user to retry, or file a bug
                    dialogBody = getString(R.string.error_unknown, error.getErrorMessage());
                    break;
            }
        }

        new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.error_dialog_button_text, listener)
                .setTitle(R.string.error_dialog_title)
                .setMessage(dialogBody)
                .show();
    }
}