package com.happygarden.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.happygarden.TopTopApplication;
import com.happygarden.R;
import com.happygarden.core.MenuItem;
import com.squareup.picasso.Picasso;


import java.util.List;

public class MenuItemListAdapter extends SingleTypeAdapter<MenuItem> {
    /**
     * @param inflater
     * @param items
     */
    public MenuItemListAdapter(final LayoutInflater inflater, final List<MenuItem> items) {
        super(inflater, R.layout.menu_list_item);
        setItems(items);
    }

    /**
     * @param inflater
     */
    public MenuItemListAdapter(final LayoutInflater inflater) {
        this(inflater,null);
    }

    @Override
    public long getItemId(final int position) {
        final String id = getItem(position).getItemID();
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.iv_photo,R.id.tv_name};
    }

    @Override
    protected void update(final int position, final MenuItem item) {
        Picasso.with(TopTopApplication.getInstance())
                .load(item.getImage())
                .placeholder(R.drawable.gravatar_icon)
                .into(imageView(0));
        setText(1, item.getName());

    }
}
