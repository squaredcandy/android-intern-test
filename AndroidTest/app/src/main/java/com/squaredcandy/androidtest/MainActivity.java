package com.squaredcandy.androidtest;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private ArrayList<TvMazeApiModel> models;
    private RecyclerView recyclerView;
    private TvMazeApiAdapter adapter;
    private WebView webView;
    private boolean inWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        models = new ArrayList<>();
        new fetchAPIData().execute();
        recyclerView = findViewById(R.id.recyclerView);
        webView = findViewById(R.id.webView1);
        hideWebView();

        adapter = new TvMazeApiAdapter(models, false);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!adapter.showLong) return;
                TvMazeApiModel model = models.get(recyclerView.getChildLayoutPosition(v));
                System.out.println(v);
                showWebView(model.getUrl());
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if(Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if(inWebView) {
                hideWebView();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void hideWebView() {
        webView.setVisibility(View.GONE);
        inWebView = false;
        invalidateOptionsMenu();
    }

    public void showWebView(String url) {
        webView.setVisibility(View.VISIBLE);
        inWebView = true;
        webView.setWebViewClient(new WebClient());
        webView.loadUrl(url);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!inWebView) {
            getMenuInflater().inflate(R.menu.menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.web_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();

        switch(menuItem) {
            case R.id.switchView:
                adapter.showLong = !adapter.showLong;
                Toast.makeText(MainActivity.this,
                    (adapter.showLong) ? "Switching to Long View" : "Switching to Compact View",
                    Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                break;
            case R.id.back_button:
                hideWebView();
                break;
            case R.id.action_refresh:
                webView.reload();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onLoadResource(WebView view, String url){
            //view.loadUrl(url);
        }
    }

    private class fetchAPIData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {

                URL url = new URL("http://api.tvmaze.com/shows/1/episodes");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                int lengthOfFile = urlConnection.getContentLength();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.e("Json1", forecastJsonStr);

                JSONArray jsonArray = new JSONArray(forecastJsonStr);
                JSONObject images;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    TvMazeApiModel contactModel = new TvMazeApiModel();
                    contactModel.setId(jsonObject.getInt("id"));
                    contactModel.setUrl(jsonObject.getString("url"));
                    contactModel.setName(jsonObject.getString("name"));
                    contactModel.setSeason(jsonObject.getInt("season"));
                    contactModel.setEpisode(jsonObject.getInt("number"));
                    contactModel.setAirDate(jsonObject.getString("airdate"));
                    contactModel.setAirTime(jsonObject.getString("airtime"));
                    contactModel.setAirStamp(jsonObject.getString("airstamp"));
                    contactModel.setRunTime(jsonObject.getInt("runtime"));
                    images = jsonObject.getJSONObject("image");
                    contactModel.setMediumImage(images.getString("medium"));
                    contactModel.setOriginalImage(images.getString("original"));
                    contactModel.setSummary(jsonObject.getString("summary"));
                    contactModel.setLinks(jsonObject.getJSONObject("_links")
                            .getJSONObject("self").getString("href"));
                    //contactModel.print();
                    models.add(contactModel);
                }

                return forecastJsonStr;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return forecastJsonStr;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter.notifyDataSetChanged();
        }
    }
}
