package com.sanatmondal.gasdistribution.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class PrefServiceURL {
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "GasDistribution";
    public static final String KEY_URL = "url";

    public PrefServiceURL(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createPrefServiceURL(String url){
        editor.putString(KEY_URL, url);
        editor.commit();
    }

    public String getURL(){
        return pref.getString(KEY_URL, "");
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_URL, pref.getString(KEY_URL, null));
        return user;
    }

    public void clearPrefServiceURL(){
        editor.clear();
        editor.commit();
    }

}
