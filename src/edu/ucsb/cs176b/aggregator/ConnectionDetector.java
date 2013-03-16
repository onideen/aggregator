package edu.ucsb.cs176b.aggregator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
 
public class ConnectionDetector {
 
    private Context context;
 
    public ConnectionDetector(Context context){
        this.context = context;
    }
 
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null) {
              NetworkInfo[] networkInfo = connectivity.getAllNetworkInfo();
              if (networkInfo != null)
                  for (int i = 0; i < networkInfo.length; i++)
                      if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                          return true;
                      }
 
          }
          return false;
    }
}
