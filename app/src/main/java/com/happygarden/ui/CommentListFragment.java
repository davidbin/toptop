package com.happygarden.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import com.happygarden.BootstrapServiceProvider;
import com.happygarden.Injector;
import com.happygarden.R;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.happygarden.authenticator.LogoutService;
import com.happygarden.core.Comment;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;


public class CommentListFragment extends ItemListFragment<Comment> implements UpdateableFragment {
    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem itemNewComment = menu.findItem(R.id.new_comment);
        MainActivity activity = (MainActivity)getActivity();
        if(activity.getMenuItem() != null) {
            itemNewComment.setVisible(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (!isUsable()) {
            return false;
        }
        switch (item.getItemId()) {
            case R.id.new_comment:
                newComment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void newComment() {
        ViewFlipper vf = (ViewFlipper)getActivity().findViewById( R.id.viewFlipper );
        vf.setBackgroundResource(R.drawable.main_background);
        vf.showNext();
        getActivity().getActionBar().hide();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(R.string.no_comment);

    }

    @Override
    protected void configureList(Activity activity, ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);


    }

    @Override
    public void update(){
        refreshWithProgress();
    }

    @Override
    protected LogoutService getLogoutService() {
        return logoutService;
    }


    @Override
    public Loader<List<Comment>> onCreateLoader(int id, Bundle args) {
        final List<Comment> initialItems = items;
        return new ThrowableLoader<List<Comment>>(getActivity(), items) {

            @Override
            public List<Comment> loadData() throws Exception {
                String itemID = ((MainActivity) getActivity()).getMenuItem();
                try {

                    if (getActivity() != null && itemID != null ) {
                        return serviceProvider.getService(getActivity()).getComments(itemID);

                    }
                    else {
                        return Collections.emptyList();
                    }

                } catch (Exception e) {
                    Activity activity = getActivity();
                    if (activity != null)
                        activity.finish();
                    return initialItems;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(final Loader<List<Comment>> loader, final List<Comment> items) {
        super.onLoadFinished(loader, items);

    }

    @Override
    protected SingleTypeAdapter<Comment> createAdapter(final List<Comment> items) {
        return new CommentListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        final Comment item = ((Comment) l.getItemAtPosition(position));
        //startActivity(new Intent(getActivity(), MenuItemActivity.class).putExtra("comment_items", item));
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.error_loading_comments;
    }
}
