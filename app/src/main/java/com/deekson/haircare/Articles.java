package com.deekson.haircare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.deekson.haircare.Adapter.ArticlesAdapter;
import com.deekson.haircare.Model.Article;
import com.deekson.haircare.service.HairCareService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Articles extends AppCompatActivity implements View.OnClickListener {
String type;
RecyclerView recyclerView;
ArticlesAdapter mAdapter;
ArrayList<Article> articlesArray =new ArrayList<>();
TextView Error;
ProgressBar progressBar;
ConstraintLayout ErrorSection;
Button retry;
AdView adView2;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView sec;
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_custom);


        editor = getSharedPreferences("Section",MODE_PRIVATE).edit();
        preferences = getApplicationContext().getSharedPreferences("Section",MODE_PRIVATE);
        recyclerView= findViewById(R.id.recyclerView);
        progressBar= findViewById(R.id.progressBar);
        ErrorSection = findViewById(R.id.ErrorSection);
        retry = findViewById(R.id.retry);
        retry.setOnClickListener(this);
        Error= findViewById(R.id.Error);
        sec = findViewById(R.id.sec);
        sec.setVisibility(View.VISIBLE);
        adView2 = findViewById(R.id.adView2);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d("InitializationStatus", "onInitializationComplete: "+initializationStatus.getAdapterStatusMap());
                AdRequest adRequest = new AdRequest.Builder().build();
                adView2.loadAd(adRequest);
            }
        });



            Intent intent = getIntent();
            if (intent != null) {
                type = intent.getStringExtra("Section");
            } else {
                type = preferences.getString("Section", null);
            }
        sec.setText(type+"'s "+"Section");
        if (isNetworkAvailable(getApplicationContext())) {
            ErrorSection.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            Error.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            getArticles(type);
        }
        else{
            ErrorSection.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            Error.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        NetworkInfo[] info = connectivity.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    Log.d("NetworkConnection", "isNetworkAvailable: Yes");
                    return true;
                }
            }
        }
        return false;
    }

    private void getArticles(String type) {
        final HairCareService hairCareService= new HairCareService();
        HairCareService.getArticles(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                articlesArray = hairCareService.articles(response);
                Articles.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (articlesArray.size() >0) {
                            progressBar.setVisibility(View.GONE);
                            Error.setVisibility(View.GONE);
                            mAdapter = new ArticlesAdapter(getApplicationContext(), articlesArray);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Articles.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(mAdapter);
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            Error.setVisibility(View.VISIBLE);
                            Log.d("Error","load unsuccessful");

                        }
                    }
                });
            }
        }, type);
    }

    @Override
    public void onClick(View v) {
        if (v == retry){
            if (isNetworkAvailable(getApplicationContext())){
                ErrorSection.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                Error.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                getArticles(type);
            }
            else{
                ErrorSection.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Error.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        preferences.edit().clear().commit();
        startActivity(intent);
    }
}
