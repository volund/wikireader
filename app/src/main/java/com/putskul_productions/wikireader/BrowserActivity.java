package com.putskul_productions.wikireader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class BrowserActivity extends AppCompatActivity implements CustomWebClient.WebClientListener, OnClickMenu {
    String no_content_html = "<html><body style='background: #ece6ff; text-align: center; color: #444; '><br><br><p>No content selected, try the settings<p></body></html>";
    String no_site_selected_html = "<html><body style='background: #ece6ff; text-align: center; color: #444;'><br><br><p>Select a website from the menu on the left.</p><p><strong>Double-tap a word</strong> to see the dictionary definition</p></body></html>";

    protected SideDrawerView mSideDrawer;
    protected DrawerLayout mDrawerLayout;
    MenuItem historyBackButton;
    MenuItem historyForwardButton;
    WebView mWebView = null;
    ProgressBar mProgressBar;
    boolean shouldClearHistoryOnLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        mWebView = findViewById(R.id.webview);
        mProgressBar = findViewById(R.id.webProgressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        mSideDrawer = findViewById(R.id.navigation_view);
        mDrawerLayout = findViewById(R.id.drawerLayout);

        mSideDrawer.setListener(this);
        configureToolbar();
        configureWebView();
        loadLanguagesOrLastUrl();
    }

    void loadLanguagesOrLastUrl() {
        if (Storage.shared.isFreshInstall(this)) {
            Storage.shared.setLanguages(this, Settings.shared.defaultLanguages());
            Storage.shared.setIsFreshInstall(this, false);
            showActivity(ContentActivity.class);
        }
        else {
            Language language = Settings.shared.getCurrentLanguage(this);
            Site site = Settings.shared.getCurrentSite(this);
            String url = Settings.shared.lastVisitedURL(this, language, site);
            mWebView.loadUrl(url);
        }
    }

    void configureWebView() {
        WebSettings webViewSettings = mWebView.getSettings();
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setDefaultFontSize(21);
        mWebView.addJavascriptInterface(new JavascriptBridge(this), "javascriptBridge");
        mWebView.setWebViewClient(new CustomWebClient(this));
        mWebView.setWebChromeClient(new LoggingChromeClient());
    }

    void configureToolbar() {
        setTitle("");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_language_black_24dp);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browser, menu);
        historyBackButton = menu.findItem(R.id.action_back);
        historyForwardButton = menu.findItem(R.id.action_forward);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        else if (id == R.id.HomeButton) {
            mWebView.loadUrl(Settings.shared.getCurrentSite(this).address);
        }
        else if ((id == R.id.action_back) && mWebView.canGoBack()) {
            mWebView.goBack();
        }
        else if ((id == R.id.action_forward) && mWebView.canGoForward())  {
            mWebView.goForward();
        }
        else if (id == R.id.action_settings) {
            showActivity(SettingsActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    void showActivity(Class klass) {
        Intent intent = new Intent(this, klass);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if ((mDrawerLayout) != null && (mDrawerLayout.isDrawerOpen(GravityCompat.START)))
            mDrawerLayout.closeDrawers();
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (!Storage.shared.hasEnabledLanguages(this)) {
            mWebView.loadData(no_content_html, "text/html; charset=utf-8", "UTF-8");
        }
        else if (Settings.shared.currentSiteIsBlank(this)) {
            // Does not load correctly after first time selecting content
            // loading from the UI thread after a delay does not fix
            // but for some unknown reason loading it twice fixes it
            mWebView.loadData(no_site_selected_html, "text/html; charset=utf-8", "UTF-8");
            mWebView.loadData(no_site_selected_html, "text/html; charset=utf-8", "UTF-8");
        }
        mSideDrawer.setData();
    }

    @Override
    public void onPageStarted(String url) {
        Language language = Settings.shared.getCurrentLanguage(this);
        Site site = Settings.shared.getCurrentSite(this);
        Settings.shared.setLastVisitedURL(this, language, site, url);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(String URL) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (historyBackButton != null) {
            historyBackButton.setEnabled(mWebView.canGoBack());
            historyBackButton.getIcon().setAlpha(mWebView.canGoBack() ? 255 : 100);
        }
        if (historyForwardButton != null) {
            historyForwardButton.setEnabled(mWebView.canGoForward());
            historyForwardButton.getIcon().setAlpha(mWebView.canGoForward() ? 255 : 100);
        }
        if(shouldClearHistoryOnLoad){
            shouldClearHistoryOnLoad = false;
            mWebView.clearHistory();
        }
    }

    // SideDrawerListener
    @Override
    public void onClick(Language language, Site site) {
        Settings.shared.setCurrentLanguage(this, language);
        Settings.shared.setCurrentSite(this, site);
        String url = Settings.shared.lastVisitedURL(this, language, site);
        shouldClearHistoryOnLoad = true;
        mWebView.loadUrl(url);
        mDrawerLayout.closeDrawers();
    }
}
