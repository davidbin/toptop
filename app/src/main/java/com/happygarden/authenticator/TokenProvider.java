package com.happygarden.authenticator;

import android.app.Activity;
import android.content.SharedPreferences;

import com.happygarden.core.Constants;

import java.io.IOException;

public class TokenProvider {
    private SharedPreferences prefs;
    public TokenProvider(SharedPreferences prefs){
        this.prefs = prefs;
    }
    public String getToken(final Activity activity) throws IOException {
        return prefs.getString(Constants.Token,"");
    }

}
