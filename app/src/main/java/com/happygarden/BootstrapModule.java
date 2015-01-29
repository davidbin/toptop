package com.happygarden;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happygarden.authenticator.LogoutService;
import com.happygarden.authenticator.TokenProvider;
import com.happygarden.core.Constants;
import com.happygarden.core.PostFromAnyThreadBus;
import com.happygarden.core.RestErrorHandler;
import com.happygarden.ui.CategoryActivity;
import com.happygarden.ui.CategoryListFragment;
import com.happygarden.ui.CommentActivity;
import com.happygarden.ui.CommentListFragment;
import com.happygarden.ui.LoginActivity;
import com.happygarden.ui.MainActivity;
import com.happygarden.ui.MenuItemActivity;
import com.happygarden.ui.MenuItemListFragment;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;


/**
 * Dagger module for setting up provides statements.
 * Register all of your entry points below.
 */
@Module(
        complete = false,

        injects = {
                TopTopApplication.class,
                LoginActivity.class,
                MainActivity.class,
                CategoryActivity.class,
                CategoryListFragment.class,
                MenuItemActivity.class,
                MenuItemListFragment.class,
                CommentActivity.class,
                CommentListFragment.class

        }
)
public class BootstrapModule {
    @Singleton
    @Provides
    Bus provideOttoBus() {
        return new PostFromAnyThreadBus();
    }
    @Provides
    @Singleton
    LogoutService provideLogoutService(final Context context) {
        return new LogoutService(context);
    }

    @Provides
    BootstrapServiceProvider provideBootstrapServiceProvider(RestAdapter restAdapter,TokenProvider tokenProvider) {
        return new BootstrapServiceProvider(restAdapter,tokenProvider);
    }

    @Provides
    TokenProvider provideTokenProvider(SharedPreferences prefs) {
        return new TokenProvider(prefs);
    }

    @Provides
    Gson provideGson() {
        /**
         * GSON instance to use for all request  with date format set up for proper parsing.
         * <p/>
         * You can also configure GSON with different naming policies for your API.
         * Maybe your API is Rails API and all json values are lower case with an underscore,
         * like this "first_name" instead of "firstName".
         * You can configure GSON as such below.
         * <p/>
         *
         * public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd")
         *         .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
         */
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    @Provides
    RestErrorHandler provideRestErrorHandler(Bus bus) {
        return new RestErrorHandler(bus);
    }



    @Provides
    RestAdapter provideRestAdapter(RestErrorHandler restErrorHandler, Gson gson) {
        return new RestAdapter.Builder()
                .setEndpoint(Constants.Http.SERVER)
                .setErrorHandler(restErrorHandler)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
    }


}
