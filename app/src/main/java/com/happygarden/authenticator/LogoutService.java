package com.happygarden.authenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.happygarden.core.Constants;
import com.happygarden.util.Ln;
import com.happygarden.util.SafeAsyncTask;

import javax.inject.Inject;


/**
 * Class used for logging a user out.
 */
public class LogoutService {

    protected final Context context;


    @Inject
    public LogoutService(final Context context) {
        this.context = context;

    }

    public void logout(final Runnable onSuccess) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(Constants.Token,"").commit();
        onSuccess.run();
    }


}
