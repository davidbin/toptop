
package com.happygarden;

import android.accounts.AccountsException;
import android.app.Activity;

import com.happygarden.authenticator.TokenProvider;
import com.happygarden.core.BootstrapService;

import java.io.IOException;

import javax.inject.Inject;

import retrofit.RestAdapter;

/**
 * Provider for a {@link com.happygarden.core.BootstrapService} instance
 */
public class BootstrapServiceProvider {

    private RestAdapter restAdapter;
    private TokenProvider tokenProvider;

    public BootstrapServiceProvider(RestAdapter restAdapter,TokenProvider tokenProvider) {
        this.restAdapter = restAdapter;
        this.tokenProvider = tokenProvider;

    }

    /**
     * Get service for configured key provider
     * <p/>
     * This method gets an auth key and so it blocks and shouldn't be called on the main thread.
     *
     * @return bootstrap service
     * @throws IOException
     * @throws AccountsException
     */
    public BootstrapService getService(final Activity activity)
            throws IOException {
        String token = tokenProvider.getToken(activity);

        // TODO: See how that affects the bootstrap service.
        return new BootstrapService(restAdapter,token);
    }
}
