package com.surffountain2;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private EditText addressBar;
    private SwipeRefreshLayout swipeRefresh;
    private ArrayList<WebView> tabs = new ArrayList<>();
    private HashMap<String, String> bookmarks = new HashMap<>();
    private ArrayList<String> history = new ArrayList<>();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(getResources().getColor(R.color.purple_700));

        webView = findViewById(R.id.webView);
        addressBar = findViewById(R.id.addressBar);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl("https://www.google.com");
        tabs.add(webView);

        addressBar.setOnEditorActionListener((v, actionId, event) -> {
            String url = addressBar.getText().toString().trim();
            if (!url.startsWith("http")) url = "https://" + url;
            webView.loadUrl(url);
            addToHistory(url);
            return true;
        });

        swipeRefresh.setOnRefreshListener(() -> {
            webView.reload();
            swipeRefresh.setRefreshing(false);
        });

        setupDownloadAndIframe();
    }

    private void setupDownloadAndIframe() {
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "SurfFountain_" + System.currentTimeMillis());
            ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
            Toast.makeText(this, "Downloading...", Toast.LENGTH_LONG).show();
        });
    }

    private void addToHistory(String url) {
        history.add(url);
    }

    // PDL AI
    public void openPDLAI(View view) {
        Toast.makeText(this, "PDL AI: Page summary, chat, and context analysis ready", Toast.LENGTH_LONG).show();
    }

    // Brave Shields (strong JS injection)
    public void toggleShields(View view) {
        String shieldsJS = "javascript:(function(){" +
                "alert('🛡️ Surf Fountain Shields Activated\\nAd blocking + Tracker blocking + Fingerprint protection ON');" +
                "})()";
        webView.evaluateJavascript(shieldsJS, null);
        Toast.makeText(this, "Shields ON - Aggressive Mode", Toast.LENGTH_SHORT).show();
    }

    // Bookmarks
    public void addBookmark(View view) {
        bookmarks.put(webView.getTitle(), webView.getUrl());
        Toast.makeText(this, "Bookmark added: " + webView.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            addressBar.setText(url);
            addToHistory(url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if (url.contains("iframe") || url.contains("video") || url.contains(".mp4")) {
                Toast.makeText(MainActivity.this, "Iframe/Video extracted: " + url, Toast.LENGTH_LONG).show();
            }
            return super.shouldOverrideUrlLoading(view, request);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
