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

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(Color.parseColor("#6A1B9A"));

        webView = findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient());

        // Long press features
        registerForContextMenu(webView);

        webView.loadUrl("https://duckduckgo.com");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Download Video");
        menu.add(0, 2, 0, "Extract Iframe URLs");
        menu.add(0, 3, 0, "PDL AI");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            // Video download
            Toast.makeText(this, "Video download started", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == 2) {
            webView.evaluateJavascript("Array.from(document.querySelectorAll('iframe')).map(i => i.src).join('\\n')", value -> Toast.makeText(this, "Iframes: " + value, Toast.LENGTH_LONG).show());
        } else if (item.getItemId() == 3) {
            Toast.makeText(this, "PDL AI activated", Toast.LENGTH_LONG).show();
        }
        return super.onContextItemSelected(item);
    }
}
