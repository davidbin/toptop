package com.happygarden.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.happygarden.Injector;
import com.happygarden.R;
import com.happygarden.R.id;
import com.happygarden.core.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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
import java.util.Locale;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;


public class LoginActivity extends Activity {

    @Inject protected SharedPreferences prefs;

    @InjectView(id.et_email) protected AutoCompleteTextView emailText;
    @InjectView(id.et_password) protected EditText passwordText;

    @InjectView(id.et_reg_email) protected AutoCompleteTextView regEmailText;
    @InjectView(id.et_reg_password) protected EditText regPasswordText;
    @InjectView(id.et_reg_fullname) protected EditText regFullName;

    @InjectView(id.viewFlipper) protected ViewFlipper vf;


    int mode;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Locale locale = new Locale("vn");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        final Activity activity = this;
        setContentView(R.layout.load_screen);
        Injector.inject(this);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                String token = prefs.getString(Constants.Token,"");
                if(token=="") {
                    goToMain();
                    //setContentView(R.layout.login_activity);
                    //Views.inject(activity);

                }
                else {
                    new MyAsyncTask().execute(token);
                }


            }
        },3000);

    }

    public void goToMain(){
        try {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            this.finish();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void switchView(final View view) {
        vf.showNext();
        regEmailText.setText("");
        regPasswordText.setText("");
        regFullName.setText("");
    }

    public void handleRegister(final View view) {
        if(regEmailText.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), "Email is required",Toast.LENGTH_LONG).show();
        else if(regPasswordText.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), "Password is required",Toast.LENGTH_LONG).show();
        else if(regFullName.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), "Full Name is required",Toast.LENGTH_LONG).show();
        else {
            new RegisterAsyncTask().execute();
        }
    }

    public void handleLogin(final View view) {
        if(emailText.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), "Email is required",Toast.LENGTH_LONG).show();
        else if(passwordText.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), "Password is required",Toast.LENGTH_LONG).show();
        else {
            mode=1;
            new MyAsyncTask().execute("");
        }
    }


    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        Dialog dlg;
        String message;
        String token;

        boolean isSuccess;
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            //check if token was passed
            if(params[0] == "") {

                login();
            }
            else {

                verifyToken(params[0]);
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(mode==1) {
                dlg = new Dialog(LoginActivity.this);
                dlg.setTitle("Sign in...");
                dlg.show();
            }
        }

        protected void onPostExecute(Double result) {
            //pb.setVisibility(View.GONE);

            if(mode==1) {
                dlg.dismiss();
            }
            if(message!=null){
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_LONG).show();
            }
            //verify token
            if(isSuccess){
                goToMain();
            }
            else {
                if(mode==0){
                     setContentView(R.layout.login_activity);
                     Views.inject(LoginActivity.this);
                }

            }
        }

        protected void onProgressUpdate(Integer... progress) {
            //pb.setProgress(progress[0]);
        }

        public void verifyToken(String paramToken) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(Constants.Http.SERVER + "/token.php?token=" + paramToken);
            try {
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                if(response.getStatusLine().getStatusCode() == 200) {
                   isSuccess=true;
             }
                else {
                    String data = inputStreamToString(is);
                    JSONObject json = new JSONObject(data);
                    message = json.getString("message");
                }
            }
            catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void login() {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constants.Http.SERVER + "/auth.php");

            try {
                // Add your data

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username",emailText.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("password",passwordText.getText().toString()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                if(response.getStatusLine().getStatusCode() == 200) {
                    String data = inputStreamToString(is);
                    JSONObject json = new JSONObject(data);
                    message = "Welcome " + json.getString("fullname");
                    token = json.getString("token");
                    prefs.edit().putString("com.happygarden.token", token).apply();
                    isSuccess=true;
                }
                else {
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


    private class RegisterAsyncTask extends AsyncTask<String, Integer, Double> {
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
            dlg = new Dialog(LoginActivity.this);
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

                vf.showNext();

            }

        }

        public void submit() {
            // Create a new HttpClient and Post Header

             HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Constants.Http.SERVER + "/user.php");

                try {
                    // Add your data

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    nameValuePairs.add(new BasicNameValuePair("email", regEmailText.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("password", regPasswordText.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("fullname", regFullName.getText().toString()));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    if (response.getStatusLine().getStatusCode() == 200) {
                        message = "Register success";
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


}
