package com.example.nitai.client_nitai;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class WikiObject implements Serializable {
    String title;
    private String englishTitle;
    private String summary;
    private String url;
    String image;

    WikiObject(JSONObject jsonObject) {
        try {
            this.title = jsonObject.getString("title");
            this.summary = jsonObject.getString("summary");
            this.url = jsonObject.getString("url");
            this.englishTitle = jsonObject.getString("englishTitle");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.image = jsonObject.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getEnglishTitle() {
        return englishTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }
}
