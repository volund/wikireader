package com.putskul_productions.wikireader;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class LookupDialog extends Dialog implements android.view.View.OnClickListener {
    public Activity context;
    public Button mCloseButton;
    public WebView mWebView;
    public String url;
    public Runnable onDismiss;

    public LookupDialog(Activity act, String url, Runnable onDismissRunnable) {
        super(act, R.style.Theme_AppCompat_DialogWhenLarge);
        this.context = act;
        this.url = url;
        this.onDismiss = onDismissRunnable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lookup_dialog);
        mWebView = findViewById(R.id.lookupWebview);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(this.url);
        mCloseButton = findViewById(R.id.btn_close);
        mCloseButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_close) {
            dismiss();
            this.onDismiss.run();
        }
    }
}