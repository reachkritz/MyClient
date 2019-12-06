package com.example.hpcom.myclient;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by hp.com on 3/25/2018.
 */

public class FcmInstanceIdService extends FirebaseInstanceIdService{
public void onTokenRefresh() {

    String recent_token;
    recent_token = FirebaseInstanceId.getInstance().getToken();
    SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(getString(R.string.FCM_TOKEN),recent_token);
    editor.commit();
}
}
