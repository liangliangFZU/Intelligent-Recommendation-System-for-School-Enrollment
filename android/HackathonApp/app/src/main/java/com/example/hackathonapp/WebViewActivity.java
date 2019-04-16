package com.example.hackathonapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent=getIntent();
        String url=intent.getStringExtra("url");

        WebView detailWebView=(WebView)findViewById(R.id.detail_web_view);

        detailWebView.getSettings().setJavaScriptEnabled(true);
        detailWebView.getSettings().setDomStorageEnabled(true);
        detailWebView.getSettings().setUseWideViewPort(true);
        detailWebView.setWebViewClient(new WebViewClient());
        detailWebView.loadUrl(url);

    }
}
