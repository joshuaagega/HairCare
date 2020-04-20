package com.deekson.haircare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
Button men,women,kids;
String section;
TextView sec;
AdView adView;
    SharedPreferences.Editor editor;
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_custom);

        editor = getSharedPreferences("Section",MODE_PRIVATE).edit();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Section",MODE_PRIVATE);
        section = preferences.getString("Section",null);
        String stat= preferences.getString("Stat",null);
        if (stat!=null) {
            if (section != null) {
                Intent intent = new Intent(getApplicationContext(), Articles.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("Section", section);
                editor.putString("Section", section);
                editor.commit();
                startActivity(intent);
            }
        }
        sec = findViewById(R.id.sec);
        sec.setVisibility(View.GONE);

        men = findViewById(R.id.Men);
        women = findViewById(R.id.Women);
        kids = findViewById(R.id.Kids);
        adView = findViewById(R.id.adView);

        men.setOnClickListener(this);
        women.setOnClickListener(this);
        kids.setOnClickListener(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d("InitializationStatus", "onInitializationComplete: "+initializationStatus.getAdapterStatusMap());
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v==men){
            Intent intent = new Intent(getApplicationContext(),Articles.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("Section","Men");
            editor.putString("Section","Men");
            editor.commit();
            startActivity(intent);
        }
        if (v==women){
            Intent intent = new Intent(getApplicationContext(),Articles.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("Section","Women");
            editor.putString("Section","Woman");
            editor.commit();
            startActivity(intent);
        }
        if (v==kids){
            Intent intent = new Intent(getApplicationContext(),Articles.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("Section","Kids");
            editor.putString("Section","Kids");
            editor.commit();
            startActivity(intent);
        }
    }
}
