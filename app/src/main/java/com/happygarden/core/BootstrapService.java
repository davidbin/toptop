
package com.happygarden.core;

import com.happygarden.authenticator.TokenProvider;

import java.util.List;

import retrofit.RestAdapter;

/**
 * Bootstrap API service
 */
public class BootstrapService {

    private RestAdapter restAdapter;
    private String token;
    /**
     * Create bootstrap service
     * Default CTOR
     */
    public BootstrapService() {
    }

    /**
     * Create bootstrap service
     *
     * @param restAdapter The RestAdapter that allows HTTP Communication.
     */
    public BootstrapService(RestAdapter restAdapter,String token) {
        this.restAdapter = restAdapter;
        this.token= token;
    }

    private CategoryService getCategoryService() {
        return getRestAdapter().create(CategoryService.class);
    }

    private MenuItemService getMenuItemService() {
        return getRestAdapter().create(MenuItemService.class);
    }

    private CommentService getCommentService() {
        return getRestAdapter().create(CommentService.class);
    }

    private RestAdapter getRestAdapter() {
        return restAdapter;
    }

    /**
     * Get all bootstrap News that exists on Parse.com
     */

    public List<Category> getCategories() {
        return getCategoryService().getCategories(token).getResults();
    }

     public List<MenuItem> getMenuItems(String category) {
        return getMenuItemService().getMenuItems(token,category).getResults();
    }

    public List<Comment> getComments(String itemID) {
        return getCommentService().getComments(token,itemID).getResults();
    }


}