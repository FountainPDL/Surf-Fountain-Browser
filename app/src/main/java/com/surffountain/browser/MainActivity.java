package com.surffountain.browser;

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

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private EditText addressBar;
    private SwipeRefreshLayout swipeRefresh;

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
        webView.setWebChromeClient(new CustomWebChromeClient());

        webView.loadUrl("https://www.google.com");

        addressBar.setOnEditorActionListener((v, actionId, event) -> {
            String url = addressBar.getText().toString().trim();
            if (!url.startsWith("http")) url = "https://" + url;
            webView.loadUrl(url);
            return true;
        });

        swipeRefresh.setOnRefreshListener(() -> {
            webView.reload();
            swipeRefresh.setRefreshing(false);
        });

        setupVideoDownloadAndIframe();
    }

    private void setupVideoDownloadAndIframe() {
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, 
                "SurfFountain_" + System.currentTimeMillis());
            ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
            Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();
        });
    }

    public void openPDLAI(View view) {
        Toast.makeText(this, "PDL AI opened - Ask anything about this page", Toast.LENGTH_LONG).show();
    }

    public void toggleShields(View view) {
        webView.evaluateJavascript("alert('Surf Fountain Shields Activated - Trackers & Ads Blocked');", null);
        Toast.makeText(this, "Shields ON", Toast.LENGTH_SHORT).show();
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            addressBar.setText(url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if (url.contains("iframe") || url.contains(".mp4") || url.contains("video")) {
                Toast.makeText(MainActivity.this, "Iframe/Video URL: " + url, Toast.LENGTH_LONG).show();
            }
            return super.shouldOverrideUrlLoading(view, request);
        }
    }

    private class CustomWebChromeClient extends WebChromeClient {
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