package com.happygarden.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;


import com.happygarden.BootstrapServiceProvider;
import com.happygarden.Injector;
import com.happygarden.R;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.happygarden.authenticator.LogoutService;
import com.happygarden.core.MenuItem;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;


public class MenuItemListFragment extends ItemListFragment<MenuItem> implements UpdateableFragment {
    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @InjectView(R.id.vp_pages)
    protected ViewPager pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Views.inject(this, getActivity());
        setEmptyText(R.string.no_menuitem);
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
    public Loader<List<MenuItem>> onCreateLoader(int id, Bundle args) {
        final List<MenuItem> initialItems = items;
        return new ThrowableLoader<List<MenuItem>>(getActivity(), items) {

            @Override
            public List<MenuItem> loadData() throws Exception {
                String category = ((MainActivity) getActivity()).getCategory();
                try {

                    if (getActivity() != null && category != null ) {
                        return serviceProvider.getService(getActivity()).getMenuItems(category);

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

    public void onKeyDown() {
        pager.setCurrentItem(1);
    }

    @Override
    public void onLoadFinished(final Loader<List<MenuItem>> loader, final List<MenuItem> items) {
        super.onLoadFinished(loader, items);

    }

    @Override
    protected SingleTypeAdapter<MenuItem> createAdapter(final List<MenuItem> items) {
        return new MenuItemListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
       final MenuItem item = ((MenuItem) l.getItemAtPosition(position));
        //startActivity(new Intent(getActivity(), MenuItemActivity.class).putExtra("menu_items", item));
        ((MainActivity) getActivity()).setMenuItem(item.getItemID());
        //getActivity().invalidateOptionsMenu();
        pager.setCurrentItem(2, true);
        pager.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.error_loading_menu;
    }
}
