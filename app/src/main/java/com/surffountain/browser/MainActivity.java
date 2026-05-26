package com.surffountain.browser;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private EditText urlBar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(Color.parseColor("#6A1B9A"));

        webView = findViewById(R.id.webView);
        urlBar = findViewById(R.id.urlBar);
        progressBar = findViewById(R.id.progressBar);

        setupWebView();
        loadUrl("https://duckduckgo.com");
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                urlBar.setText(url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
            }
        });

        registerForContextMenu(webView);
    }

    private void loadUrl(String url) {
        if (!url.startsWith("http")) url = "https://" + url;
        webView.loadUrl(url);
    }

    private void openPDLAI() {
        Toast.makeText(this, "PDL AI: How can I help you today?", Toast.LENGTH_LONG).show();
        webView.evaluateJavascript("alert('PDL AI Activated! Ask me anything about this page.');", null);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Download Video");
        menu.add(0, 2, 0, "Extract Iframe URLs");
        menu.add(0, 3, 0, "Reader Mode");
        menu.add(0, 4, 0, "PDL AI");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                String url = webView.getUrl();
                DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                dm.enqueue(request);
                Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();
                return true;
            case 2:
                webView.evaluateJavascript("(function(){return Array.from(document.querySelectorAll('iframe')).map(i => i.src).join('\\n');})()", value -> Toast.makeText(MainActivity.this, "Iframes: " + value, Toast.LENGTH_LONG).show());
                return true;
            case 3:
                webView.evaluateJavascript("document.body.style.cssText = 'font-size:18px;line-height:1.8;max-width:800px;margin:auto;padding:20px;'; document.body.innerHTML = '<h1>Reader Mode</h1>' + document.body.innerText;", null);
                return true;
            case 4:
                openPDLAI();
                return true;
        }
        return super.onContextItemSelected(item);
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
