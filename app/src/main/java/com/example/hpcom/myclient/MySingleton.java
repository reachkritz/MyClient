package com.example.hpcom.myclient;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

/**
 * Created by hp.com on 3/25/2018.
 */

public class MySingleton {
    private static MySingleton mInstance;
    private static Context mctx;
    private com.android.volley.RequestQueue requestQueue;

    private MySingleton(Context context)
    {
        mctx=context;
        requestQueue=getRequestQueue();
    }

    private com.android.volley.RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
           requestQueue= Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized MySingleton getmInstance(Context context)
    {
        if(mInstance==null)
        {
            mInstance=new MySingleton(context);
        }
        return mInstance;
    }

    public<T> void addToRequestque(Request<T> request)
    {
        getRequestQueue().add(request);
    }


}
