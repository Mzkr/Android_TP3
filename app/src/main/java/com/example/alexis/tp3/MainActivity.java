package com.example.alexis.tp3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String current_user = preferences.getString("ACTIVE_USER", "");

        if (!current_user.equals("") ) {

            Intent intent = new Intent(this, ModeConnectey.class);
            intent.putExtra("email", current_user);
            startActivity(intent);
            finish();
        }
        else {
            setContentView(R.layout.activity_main);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void createAccount(View view){
        Intent intent = new Intent(MainActivity.this, CreateAccount.class);  //Lancer l'activité DisplayVue
        startActivity(intent);    //Afficher la vue

    }
    public void connectAccount(View view){
        Intent intent2 = new Intent(MainActivity.this, ConnectAccount.class);  //Lancer l'activité DisplayVue
        startActivity(intent2);    //Afficher la vue

    }
}
