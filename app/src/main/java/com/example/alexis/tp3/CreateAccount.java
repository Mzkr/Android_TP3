package com.example.alexis.tp3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import app.AppController;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexis on 17/06/2015.
 */
public class CreateAccount extends ActionBarActivity {

    private ProgressDialog pDialog;
    private static final String TAG = CreateAccount.class.getSimpleName();
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void validateForm(View v) {
        final Context context = this;
        EditText nomView = (EditText) findViewById(R.id.form_nom);
        final String nom = nomView.getText().toString();

        EditText emailView = (EditText) findViewById(R.id.form_mail);
        final String email = emailView.getText().toString();

        EditText mdpView = (EditText) findViewById(R.id.form_mdp);
        final String mdp = mdpView.getText().toString();



        if (nom.isEmpty() || email.isEmpty() || mdp.isEmpty()) {

            CharSequence text = "Champs non rempli !";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else{
            RequestQueue mRequestQueue;
            // Instantiate the cache
            Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
            // Set up the network to use HttpURLConnection as the HTTP client.
            Network network = new BasicNetwork(new HurlStack());
            // Instantiate the RequestQueue with the cache and network.
            mRequestQueue = new RequestQueue(cache, network);
            // Start the queue
            mRequestQueue.start();

            Map<String, String> params = new HashMap<>();
            params = new HashMap<String, String>();
            params.put("email", email);
            params.put("password", mdp);
            params.put("name", nom);

            // Creating volley request obj
            String tag_json_obj = "json_obj_req";

            String url = "http://questioncode.fr:10007/api/users";

            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
                    url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            pDialog.hide();

                            SharedPreferences settings = getSharedPreferences(email, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("nom", nom);
                            editor.putString("mail", email);
                            editor.putString("mdp", mdp);
                            editor.putString("token", response.toString());
                            editor.commit();

                            SharedPreferences.Editor editor2 = preferences.edit();
                            editor2.putString(email + "_token", response.toString());
                            editor2.putString("ACTIVE_USER", email);
                            editor2.apply();

                            Intent intent = new Intent(CreateAccount.this, ModeConnectey.class);
                            intent.putExtra("email", email);
                            startActivity(intent);

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    // hide the progress dialog
                    pDialog.hide();
                    final int httpStatusCode = error.networkResponse.statusCode;
                    CharSequence text = null;
                    if(httpStatusCode == 422){
                        text = "Email deja utilis&eacute;";
                    }
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        }
    }
}
