package com.happygarden.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.happygarden.R;
import com.happygarden.core.Category;

import butterknife.InjectView;

public class CategoryActivity extends BootstrapActivity {

    private Category categories;

    @InjectView(R.id.tv_name) protected TextView title;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category);
        if(false) {
            if (getIntent() != null && getIntent().getExtras() != null) {
                categories = (Category) getIntent().getExtras().getSerializable("category_items");
            }

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            setTitle(categories.getName());
            title.setText(categories.getName());
        }

    }

}
