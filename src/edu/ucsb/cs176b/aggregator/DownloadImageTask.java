package edu.ucsb.cs176b.aggregator;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<ImageView, Void, Bitmap> {

	private static final String TAG = "DownloadImageTask";
	private ImageView imageView = null;
	private String path;
	
	
	public DownloadImageTask(ImageView imageView) {
		this.imageView = imageView;
		path = imageView.getTag().toString();
	}
	
	@Override
	protected Bitmap doInBackground(ImageView... imageViews) {
		return downloadImage(imageView.getTag().toString());
	}

	@Override
	protected void onPostExecute(Bitmap result) {
	
		if (!imageView.getTag().toString().equals(path)) {
			return;
		}
		
		if (result != null && imageView != null){
			imageView.setImageBitmap(result);
			imageView.setVisibility(View.VISIBLE);
		} else {
			imageView.setVisibility(View.GONE);
		}
	}
	
	private Bitmap downloadImage(String url) {
		
		Bitmap bitmap = null;
		try {
			URL iURL = new URL(url);
			URLConnection conn = iURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bitmap = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
		}
		catch (IOException e) {
			Log.e(TAG, "Error while downloading image from server", e);
		}
		
		return bitmap;
	}

}
