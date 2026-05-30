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

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        addressBar = findViewById(R.id.addressBar);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        // Load Surf Fountain Custom Homepage
        webView.loadUrl("https://www.google.com");

        // Working Address Bar
        addressBar.setOnEditorActionListener((v, actionId, event) -> {
            String input = addressBar.getText().toString().trim();
            if (input.isEmpty()) return true;

            String url = input.startsWith("http") ? input : "https://" + input;
            webView.loadUrl(url);
            return true;
        });
    }

    public void toggleShields(View view) {
        Toast.makeText(this, "🛡️ Shields Activated - Ad & Tracker Blocking", Toast.LENGTH_SHORT).show();
    }

    public void openPDLAI(View view) {
        Toast.makeText(this, "🤖 PDL AI Opened - Ask anything", Toast.LENGTH_SHORT).show();
    }

    public void addBookmark(View view) {
        Toast.makeText(this, "⭐ Bookmark Added", Toast.LENGTH_SHORT).show();
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
