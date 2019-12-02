package com.example.vamsikikkuriproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Resume extends AppCompatActivity {
    private static final String BACK_IMG = "";
    private ConstraintLayout layout;
    private int background;
    private SharedPreferences backgroundPref;
    private String backPref = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
        layout = findViewById(R.id.resume_layout);
        background = R.drawable.background;
        backgroundPref = getSharedPreferences(backPref, MODE_PRIVATE);

        background = backgroundPref.getInt(BACK_IMG, background);
        layout.setBackgroundResource(background);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.resume_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_1) {
            background = R.drawable.background1;
            layout.setBackgroundResource(background);
        } else if (id == R.id.action_2) {
            background = R.drawable.background2;
            layout.setBackgroundResource(background);
        } else if (id == R.id.action_reset) {
            background = R.drawable.background;
            layout.setBackgroundResource(background);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor preferencesEditor = backgroundPref.edit();
        preferencesEditor.putInt(BACK_IMG, background);
        preferencesEditor.apply();
    }
}
