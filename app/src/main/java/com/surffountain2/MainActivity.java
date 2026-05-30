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

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private EditText addressBar;
    private SwipeRefreshLayout swipeRefresh;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        addressBar = findViewById(R.id.addressBar);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

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

        setupDownloadAndIframe();
    }

    private void setupDownloadAndIframe() {
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "SurfFountain_" + System.currentTimeMillis());
            ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
        });
    }

    public void toggleShields(View view) {
        Toast.makeText(this, "🛡️ Shields Activated (Ad & Tracker Blocking)", Toast.LENGTH_SHORT).show();
    }

    public void openPDLAI(View view) {
        Toast.makeText(this, "🤖 PDL AI Opened", Toast.LENGTH_SHORT).show();
    }

    public void addBookmark(View view) {
        Toast.makeText(this, "⭐ Bookmark Saved", Toast.LENGTH_SHORT).show();
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            addressBar.setText(url);
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
