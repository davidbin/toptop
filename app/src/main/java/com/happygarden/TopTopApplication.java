package com.happygarden;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;

/**
 * Created by Administrator on 7/28/2014.
 */
public class TopTopApplication extends Application {
    private static TopTopApplication instance;
    public static String Token;
    /**
     * Create main application
     */
    public TopTopApplication() {

    }

    /**
     * Create main application
     *
     * @param context
     */
    public TopTopApplication(final Context context) {
        this();
        attachBaseContext(context);


    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        // Perform injection
        Injector.init(getRootModule(), this);

    }

    private Object getRootModule() {
        return new RootModule();
    }

    /**
     *
     * Create main application
     *
     * @param instrumentation
     */
    public TopTopApplication(final Instrumentation instrumentation) {
        this();
        attachBaseContext(instrumentation.getTargetContext());
    }

    public static TopTopApplication getInstance() {
        return instance;
    }

}
