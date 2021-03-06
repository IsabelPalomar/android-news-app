package android.example.com.newsapp.activities;

import android.content.Context;
import android.example.com.newsapp.R;
import android.example.com.newsapp.adapters.NewsAdapter;
import android.example.com.newsapp.models.News;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NewsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ListView lvNews;
    private NewsAdapter newsAdapter;
    ScheduledExecutorService scheduleTaskExecutor;
    private static final int DELAY = 60;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        lvNews = (ListView) findViewById(R.id.lvNews);

        //Creates the NewsAdapter and assign it to the ListView
        ArrayList<News> arrNews = new ArrayList<>();
        newsAdapter = new NewsAdapter(this, arrNews);
        lvNews.setAdapter(newsAdapter);

        //Update the UI every minute (60 seconds)
        scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run()
            {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        fetchNews();
                    }
                });
            }
        }, 0, DELAY, TimeUnit.SECONDS);

    }

    /**
     * Fetch the news using ConnectivityManager
     */
    private void fetchNews() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new NewsClient().execute();

        } else {
            Snackbar.make(lvNews, "No connection", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    /**
     * Google News API client to send network requests
     */
    public class NewsClient extends AsyncTask<String, Void, JSONObject> {

        private static final String API_BASE_URL = "https://ajax.googleapis.com/";
        private static final String API_PARAMETERS = "ajax/services/feed/load?v=1.0&q=";
        private static final String RSS_SITE = "http://rss.nytimes.com/services/xml/rss/nyt/FashionandStyle.xml?json";

        @Override
        protected JSONObject doInBackground(String... query) {

            try {
                return downloadUrl(API_BASE_URL + API_PARAMETERS + RSS_SITE);
            } catch (IOException e) {
                return null;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(JSONObject result) {

            if (result != null) {
                try {
                        // Get the entries json array
                        JSONArray entries = result.getJSONArray("entries");

                        // Parse json array into array of model objects
                        final ArrayList<News> news = News.fromJson(entries);
                        // Remove all entries from the adapter
                        newsAdapter.clear();
                        // Load model objects into the adapter
                        for (News entry : news) {
                            newsAdapter.add(entry);
                        }
                        newsAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(ProgressBar.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Snackbar.make(lvNews, "No results found", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private JSONObject downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            is = conn.getInputStream();
            return readIt(is);

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * Return the feed elements
     * @param is - InputStream
     * @return feed JSON Object
     */
    private JSONObject readIt(InputStream is) {

        JSONObject jsonObject = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            jsonObject = new JSONObject(sb.toString()).getJSONObject("responseData").getJSONObject("feed");

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}