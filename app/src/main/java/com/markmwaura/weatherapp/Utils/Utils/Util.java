package com.markmwaura.weatherapp.Utils.Utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Cache;

/**
 * Created by mark on 5/18/17.
 */

public class Util {

    private static Cache.Entry mEntry;
    static ProgressDialog pDialog;
    public static void showLoadingDialog(Context mContext) {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Receiving Updates...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();
    }

}
