package android.example.com.newsapp.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class News {

    private String title;
    private String contentSnippet;
    private String link;
    private String imageUrl;

    public News(String title, String contentSnippet, String link, String imageUrl) {
        this.title = title;
        this.contentSnippet = contentSnippet;
        this.link = link;
        this.imageUrl = imageUrl;
    }

    public News() {

    }

    public String getTitle() {
        return title;
    }

    public String getContentSnippet() {
        return contentSnippet;
    }

    public String getLink() {
        return link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public static ArrayList<News> fromJson(JSONArray jsonArray) {
        ArrayList<News> entries = new ArrayList<News>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject entryJson = null;
            try {
                entryJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            News entry = News.fromJson(entryJson);
            if (entry != null) {
                entries.add(entry);
            }
        }
        return entries;
    }

    // Returns a Entry given the expected JSON
    public static News fromJson(JSONObject jsonObject) {
        News entry = new News();
        JSONObject entryObj = null;
        JSONObject images = null;

        try {
            //images = jsonObject.getJSONObject("mediaGroups").getJSONObject("contents");

            entry.title = jsonObject.has("title") ? jsonObject.getString("title") : "";
            entry.contentSnippet = jsonObject.has("contentSnippet") ? jsonObject.getString("contentSnippet") : "";
            /*if (images.has("url")) {
                entry.imageUrl = images.getString("url");
            }*/

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return entry;
    }

}
