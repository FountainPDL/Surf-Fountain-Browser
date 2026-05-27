package com.surffountain.browser;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

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

        try {
            setContentView(R.layout.activity_main);

            webView = findViewById(R.id.webView);
            addressBar = findViewById(R.id.addressBar);
            swipeRefresh = findViewById(R.id.swipeRefresh);

            if (webView == null || addressBar == null || swipeRefresh == null) {
                Toast.makeText(this, "Layout error", Toast.LENGTH_LONG).show();
                return;
            }

            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);

            webView.setWebChromeClient(new WebChromeClient());

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    addressBar.setText(url);
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return false;
                }
            });

            addressBar.setOnEditorActionListener((v, actionId, event) -> {
                String url = addressBar.getText().toString().trim();

                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://" + url;
                }

                webView.loadUrl(url);
                return true;
            });

            swipeRefresh.setOnRefreshListener(() -> {
                webView.reload();
                swipeRefresh.setRefreshing(false);
            });

            webView.loadUrl("https://google.com");

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
