package com.happygarden.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.RatingBar;

import com.happygarden.R;
import com.happygarden.core.Comment;


import java.util.List;

public class CommentListAdapter extends AlternatingColorListAdapter<Comment> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public CommentListAdapter(final LayoutInflater inflater, final List<Comment> items,
                           final boolean selectable) {
        super(R.layout.comment_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public CommentListAdapter(final LayoutInflater inflater, final List<Comment> items) {
        super(R.layout.comment_list_item, inflater, items);
    }

    @Override
    public long getItemId(final int position) {
        final String id = getItem(position).getUser();
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.iv_photo, R.id.tv_user,R.id.tv_comment,R.id.ratingBar, R.id.tv_date};
    }

    @Override
    protected void update(final int position, final Comment item) {
        super.update(position, item);
        imageView(0).setImageResource(R.drawable.unknown_user);
        setText(1, item.getUser());
        setText(2, item.getComment());
        RatingBar  rate = getView(3,RatingBar.class);
        rate.setRating(Float.parseFloat(item.getRating()));
        setText(4, item.getDate());

    }
}
