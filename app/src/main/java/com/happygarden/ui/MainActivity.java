package com.happygarden.ui;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.happygarden.BootstrapServiceProvider;
import com.happygarden.R;
import com.happygarden.core.Constants;
import com.happygarden.util.Ln;
import com.happygarden.util.UIUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

public class MainActivity extends BootstrapFragmentActivity {
    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected SharedPreferences prefs;
    private DrawerLayout drawerLayout;

    @InjectView(R.id.viewFlipper)
    protected ViewFlipper vf;

    @InjectView(R.id.comment)
    protected EditText comment;

    @InjectView(R.id.ratingBar)
    protected RatingBar ratingBar;

    private String category;
    private String itemID;
    private int page_no;

    @Override
    public void onCreate(Bundle bundle) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(bundle);
        setContentView(R.layout.main_activity);
        Views.inject(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        initScreen();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            // do something on back.
            if(vf.getCurrentView() instanceof LinearLayout) {
                vf.showNext();
                getActionBar().show();
            }
            else {
                CarouselFragment fragment = (CarouselFragment)getSupportFragmentManager().getFragments().get(0);
                ViewPager pager = fragment.getPager();
                if(page_no > 0) {
                    page_no -= 1;
                    pager.setCurrentItem(page_no,true);

                }
                else {
                    return false;
                }
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    private boolean isTablet() {
        return UIUtils.isTablet(this);
    }


    private void initScreen() {

                Ln.d("Foo");
                final FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new CarouselFragment())
                        .commit();


    }

    public void handleSubmit(final View view) {
        if(comment.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), "Comment is required", Toast.LENGTH_LONG).show();
        else {
           new SubmitAsyncTask().execute();
        }
    }



    private class SubmitAsyncTask extends AsyncTask<String, Integer, Double> {
        Dialog dlg;
        String message;

        boolean isSuccess;
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            //check if token was passed
            submit();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dlg = new Dialog(MainActivity.this);
            dlg.setTitle("Sending data...");
            dlg.show();

        }

        protected void onPostExecute(Double result) {
            //pb.setVisibility(View.GONE);
              dlg.dismiss();

            if(message!=null){
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_LONG).show();
            }

            if(isSuccess){
                comment.setText("");
                vf.showNext();
                getActionBar().show();
                CarouselFragment fragment = (CarouselFragment)getSupportFragmentManager().getFragments().get(0);
                ViewPager pager = fragment.getPager();
                pager.setCurrentItem(2);
                pager.getAdapter().notifyDataSetChanged();
                //BootstrapPagerAdapter adapter = (BootstrapPagerAdapter)pager.getAdapter();
                //CommentListFragment cmtFragment = (CommentListFragment)adapter.getItem(2);
                //cmtFragment.forceRefresh();
            }

        }

        public void submit() {
            // Create a new HttpClient and Post Header

            String token = prefs.getString(Constants.Token,"");
            if(token=="") {
                //logout
            }
            else {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Constants.Http.SERVER + "/comment.php?token=" + token);

                try {
                    // Add your data

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    nameValuePairs.add(new BasicNameValuePair("itemID", itemID));
                    nameValuePairs.add(new BasicNameValuePair("comment", comment.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("rating", Float.toString(ratingBar.getRating())));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    if (response.getStatusLine().getStatusCode() == 200) {
                        isSuccess = true;
                    } else {
                        String data = inputStreamToString(is);
                        JSONObject json = new JSONObject(data);
                        message = json.getString("message");
                    }

                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        // Fast Implementation
        private String inputStreamToString(InputStream is) {
            String line = "";
            StringBuilder total = new StringBuilder();

            // Wrap a BufferedReader around the InputStream
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            // Read response until the end
            try {
                while ((line = rd.readLine()) != null) {
                    total.append(line);
                }
                // Return full string
                return total.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";

        }

    }

    public void setCategory(String category) {
        this.category = category;
        page_no = 1;
    }
    public String getCategory(){
        return category;
    }
    public void setMenuItem(String itemID) {
        this.itemID = itemID;
        page_no = 2;
    }
    public String getMenuItem(){
        return itemID;
    }

}
