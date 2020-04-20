package com.deekson.haircare.service;

import android.util.Log;

import com.deekson.haircare.Constants;
import com.deekson.haircare.Model.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HairCareService {
    public  static  void getArticles(Callback callback,String section){
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        HttpUrl.Builder urlBuilder = null;
        if (section.equals("Kids")){
            urlBuilder = Objects.requireNonNull(HttpUrl.parse(Constants.KIDS_SECTION_URL).newBuilder());

        }
        if (section.equals("Men")){
             urlBuilder = Objects.requireNonNull(HttpUrl.parse(Constants.MEN_SECTION_URL).newBuilder());

        }
        if (section.equals("Women")){
            urlBuilder = Objects.requireNonNull(HttpUrl.parse(Constants.WOMEN_SECTION_URL).newBuilder());

        }
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    public ArrayList<Article> articles(Response response){
        ArrayList<Article> article = new ArrayList<>();
        try {
            String jsonData = response.body().string();
            JSONObject virus =new JSONObject(jsonData);
            JSONArray jsonArray=virus.getJSONArray("articles");
            JSONObject JSON = jsonArray.getJSONObject(0);
            Log.d("LENGTH", "articles: "+JSON.length());

            if (response.isSuccessful()){
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject albumJson = jsonArray.getJSONObject(i);
                    String title = albumJson.getString("title");
                    String Artice=albumJson.getString("article");

                    Article articleModel= new Article(Artice,title);
                    article.add(articleModel);
                }
            }
          } catch (IOException e) {


             } catch (JSONException e) {
                  e.printStackTrace();
              }
        return article;
    }
}
