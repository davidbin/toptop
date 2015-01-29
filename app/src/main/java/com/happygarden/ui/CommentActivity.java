package com.happygarden.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.happygarden.R;
import com.happygarden.core.Category;
import com.happygarden.core.MenuItem;

import butterknife.InjectView;

public class CommentActivity extends BootstrapActivity {

    private MenuItem items;

    @InjectView(R.id.iv_photo) protected ImageView photo;
    @InjectView(R.id.tv_title) protected TextView title;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menuitem);

        if (getIntent() != null && getIntent().getExtras() != null) {
            items = (MenuItem) getIntent().getExtras().getSerializable("menu_items");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle(items.getName());
        title.setText(items.getName());


    }

}
