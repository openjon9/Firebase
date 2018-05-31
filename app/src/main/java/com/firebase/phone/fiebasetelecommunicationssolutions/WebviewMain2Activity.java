package com.firebase.phone.fiebasetelecommunicationssolutions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebviewMain2Activity extends AppCompatActivity {

    TextView show;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_main2);

        show=(TextView)findViewById(R.id.show);
        webView=(WebView)findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
    //意圖接收傳過來的物件資料
        Intent intent=getIntent();
        String str2=intent.getStringExtra("url");
        String str3=intent.getStringExtra("name");

    //將得到的適配器內容用 Textview 顯示出來
        show.setText(str3);
    //將集合得到的網址 用Webview 顯示出來
        webView.loadUrl(str2);
    }
    public void Back(View view) {
        finish();
    }
}

