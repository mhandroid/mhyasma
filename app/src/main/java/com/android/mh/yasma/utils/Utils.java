package com.android.mh.yasma.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.mh.yasma.R;
import com.android.mh.yasma.ui.AlbumsDetailActivity;

/**
 * Utility class for application utility method
 */
public class Utils {
public static final String ALBUM_ID = "ALBUM_ID";
    public static final String ALBUM_TITLE = "ALBUM_TITLE";
    /**
     * Method to check network is available or not
     * @param context
     * @return
     */
    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void showToastMsg(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
