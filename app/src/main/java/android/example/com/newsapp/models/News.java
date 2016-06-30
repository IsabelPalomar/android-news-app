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

    private static final String ENTRY_MEDIA_NODE = "mediaGroups";
    private static final String ENTRY_CONTENTS_NODE = "contents";

    private static final String TITLE_ELEMENT_NAME = "title";
    private static final String CONTENT_ELEMENT_NAME = "contentSnippet";
    private static final String LINK_ELEMENT_NAME = "link";
    private static final String IMAGE_ELEMENT_NAME = "url";

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
        JSONObject contents = null;
        try {

            contents = getImageUrl(jsonObject);
            entry.title = jsonObject.has(TITLE_ELEMENT_NAME) ? jsonObject.getString(TITLE_ELEMENT_NAME) : "";
            entry.contentSnippet = jsonObject.has(CONTENT_ELEMENT_NAME) ? jsonObject.getString(CONTENT_ELEMENT_NAME) : "";
            entry.link = jsonObject.has(LINK_ELEMENT_NAME) ? jsonObject.getString(LINK_ELEMENT_NAME) : "";

            if(contents != null){
                if (contents.has(IMAGE_ELEMENT_NAME))
                    entry.imageUrl = contents.getString(IMAGE_ELEMENT_NAME);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return entry;
    }

    /**
     * Returns the image URL based in the JSON object RSS hierarchy
     */
    private static JSONObject getImageUrl(JSONObject jsonObject) {

        JSONObject contents = null;
        try {
            contents = jsonObject.getJSONArray(ENTRY_MEDIA_NODE).getJSONObject(0).getJSONArray(ENTRY_CONTENTS_NODE).getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contents;
    }

}
