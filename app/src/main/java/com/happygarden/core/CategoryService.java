package com.happygarden.core;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Interface for defining the news service to communicate with Parse.com
 */
public interface CategoryService {

    @GET(Constants.Http.URL_CATEGORY)
    CategoryWrapper getCategories(@Query("token") String token);

}
