package com.example.alexis.tp3;

/**
 * Created by Alexis on 17/06/2015.
 */
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ModeConnectey extends ActionBarActivity {

    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_connectey);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String user_email = preferences.getString("ACTIVE_USER", "");

        SharedPreferences settings = getSharedPreferences(getIntent().getStringExtra("email"), 0);
        String nom = settings.getString("nom", "");

        TextView tv = new TextView(this);
        tv.setText("Hello, "+nom+" !!");
        setContentView(tv);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mode_connectey, menu);
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
        if (id == R.id.action_deco) {
            killPref();
        }

        return super.onOptionsItemSelected(item);
    }
    public void killPref(){
        String user_email = preferences.getString("ACTIVE_USER", "");

        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("ACTIVE_USER");
        editor.remove(user_email + "_token");
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}