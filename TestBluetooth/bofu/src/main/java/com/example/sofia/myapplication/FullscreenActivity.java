package com.example.sofia.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
    }

    public void but(View v) {
        startActivity(new Intent(this,ActivityHorizontal.class));
    }
    public void but1(View v) {
        startActivity(new Intent(this,Activity1.class));
    }
    public void but2(View v) {
        startActivity(new Intent(this,Activity2.class));
    }
}
