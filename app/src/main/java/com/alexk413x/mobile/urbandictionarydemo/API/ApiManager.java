package com.alexk413x.mobile.urbandictionarydemo.API;

import android.content.Context;

import com.alexk413x.mobile.urbandictionarydemo.BuildConfig;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApiManager {
    private static ApiManager instance = null;

    static final String baseUrl = BuildConfig.API_URL;
    static final String apiHostId = BuildConfig.API_HOST_ID;
    static final String apiHost = BuildConfig.API_HOST;
    static final String apiKeyId = BuildConfig.API_KEY_ID;
    static final String apiKey = BuildConfig.API_KEY;
    static RequestQueue httpQueue;


    private ApiManager(Context context) {
        httpQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized void getInstance(Context context)
    {
        if (instance == null) {
            instance = new ApiManager(context);
        }
    }
}
