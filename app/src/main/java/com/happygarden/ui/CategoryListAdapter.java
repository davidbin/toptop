package com.happygarden.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;

import com.happygarden.R;
import com.happygarden.core.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryListAdapter extends AlternatingColorListAdapter<Category> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public CategoryListAdapter(final LayoutInflater inflater, final List<Category> items,
                           final boolean selectable) {
        super(R.layout.category_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public CategoryListAdapter(final LayoutInflater inflater, final List<Category> items) {
        super(R.layout.category_list_item, inflater, items);
    }

    @Override
    public long getItemId(final int position) {
        final String id = getItem(position).getID();
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.cat_photo,R.id.tv_name};
    }

    @Override
    protected void update(final int position, final Category item) {
        super.update(position, item);
        imageView(0).setImageResource(item.getImage());
        setText(1, item.getName());

        //setNumber(R.id.tv_date, item.getCreatedAt());
    }
}
