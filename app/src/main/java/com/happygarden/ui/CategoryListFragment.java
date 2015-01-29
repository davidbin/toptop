package com.happygarden.ui;

        import android.accounts.OperationCanceledException;
        import android.app.Activity;
        import android.content.Intent;
        import android.content.res.Resources;
        import android.os.Bundle;
        import android.support.v4.content.Loader;
        import android.support.v4.view.ViewPager;
        import android.view.View;
        import android.widget.ListView;


        import com.happygarden.BootstrapServiceProvider;
        import com.happygarden.Injector;
        import com.happygarden.R;

        import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
        import com.happygarden.authenticator.LogoutService;
        import com.happygarden.core.Category;

        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.List;

        import javax.inject.Inject;

        import butterknife.InjectView;
        import butterknife.Views;


public class CategoryListFragment extends ItemListFragment<Category> {

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
        setEmptyText(R.string.no_data);
    }

    @Override
    protected void configureList(Activity activity, ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);

    }

    @Override
    protected LogoutService getLogoutService() {
        return logoutService;
    }

    @Override
    public void onDestroyView() {
        setListAdapter(null);

        super.onDestroyView();
    }

    @Override
    public Loader<List<Category>> onCreateLoader(int id, Bundle args) {
        final List<Category> initialItems = items;
        return new ThrowableLoader<List<Category>>(getActivity(), items) {

            @Override
            public List<Category> loadData() throws Exception {
                try {
                    if (getActivity() != null) {
                        List<Category> categories = new ArrayList<Category>();
                        categories.add(new Category("1",R.string.cat_study_name,R.drawable.gravatar_icon,R.string.cat_study_desp));
                        categories.add(new Category("2",R.string.cat_home_name,R.drawable.gravatar_icon,R.string.cat_home_desp));
                        //return serviceProvider.getService(getActivity()).getCategories();
                        return categories;

                    } else {
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
    protected SingleTypeAdapter<Category> createAdapter(List<Category> items) {
        return new CategoryListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Category category = ((Category) l.getItemAtPosition(position));
        //startActivity(new Intent(getActivity(), CategoryActivity.class).putExtra("category_items", category));
        ((MainActivity) getActivity()).setCategory(category.getID());
        pager.setCurrentItem(1,true);
        pager.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.error_loading_categories;
    }
}
