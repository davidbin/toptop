package com.happygarden.core;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Administrator on 8/2/2014.
 */
public interface MenuItemService {
    @GET(Constants.Http.URL_MENUITEM)
    MenuItemWrapper getMenuItems(
            @Query("category") String category);
}
