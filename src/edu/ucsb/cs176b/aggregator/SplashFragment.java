package edu.ucsb.cs176b.aggregator;

import java.util.Arrays;

import com.facebook.widget.LoginButton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;



public class SplashFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash, container, false);
        LoginButton authButton = (LoginButton) view.findViewById(R.id.login_button);
        authButton.setReadPermissions(Arrays.asList("read_stream"));
        ImageButton bird = (ImageButton) view.findViewById(R.id.images);
        bird.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent bird = new Intent(getActivity(), TwitterFeedActivity.class); 
				startActivity(bird);
			}
		});
        
        return view;
    }
}
