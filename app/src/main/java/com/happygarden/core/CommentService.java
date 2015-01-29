package com.happygarden.core;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Administrator on 8/2/2014.
 */
public interface CommentService {
    @GET(Constants.Http.URL_COMMENT)
    CommentWrapper getComments(
            @Query("token") String token,
            @Query("itemID") String itemID);
}
