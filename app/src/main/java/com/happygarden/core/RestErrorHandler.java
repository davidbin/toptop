package com.happygarden.core;

import com.happygarden.events.NetworkErrorEvent;
import com.happygarden.events.RestAdapterErrorEvent;
import com.squareup.otto.Bus;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

public class RestErrorHandler implements ErrorHandler {

    public static final int HTTP_NOT_FOUND = 404;
    public static final int INVALID_LOGIN_PARAMETERS = 101;

    private Bus bus;

    public RestErrorHandler(Bus bus) {
        this.bus = bus;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {
        if(cause != null) {
            if (cause.isNetworkError()) {
                bus.post(new NetworkErrorEvent(cause));
            } else {
                bus.post(new RestAdapterErrorEvent(cause));
            }
        }

        // Example of how you'd check for a unauthorized result
        // if (cause != null && cause.getStatus() == 401) {
        //     return new UnauthorizedException(cause);
        // }

        // You could also put some generic error handling in here so you can start
        // getting analytics on error rates/etc. Perhaps ship your logs off to
        // Splunk, Loggly, etc

        return cause;
    }

    /**
     * If a user passes an incorrect username/password combo in we could
     * get a unauthorized error back from the API. On parse.com this means
     * we get back a HTTP 404 with an error as JSON in the body as such:
     *
     *  {
     *     code: 101,
     *     error: "invalid login parameters"
     *  }
     *
     *  }
     *
     * Therefore we need to check for the 101 and the 404.
     *
     * @param cause The initial error.
     * @return
     */

}
