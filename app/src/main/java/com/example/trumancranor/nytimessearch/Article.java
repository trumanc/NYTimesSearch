package com.example.trumancranor.nytimessearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Article {
    private String webUrl;
    private String headline;
    private String thumbnailUrl;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }


    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if (multimedia.length() > 0) {
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbnailUrl = "http://www.nytimes.com/" + multimediaJson.getString("url");
            } else {
                this.thumbnailUrl = "";
            }
        } catch (JSONException e) {

        }
    }


    public static List<Article> fromJSONArray(JSONArray array) {
        ArrayList<Article> results = new ArrayList<Article>();

        for (int ind = 0; ind < array.length(); ind++) {
            try {
                results.add(new Article(array.getJSONObject(ind)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
