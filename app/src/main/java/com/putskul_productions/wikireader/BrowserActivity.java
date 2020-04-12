package com.putskul_productions.wikireader;

import android.content.Context;
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


public class BrowserActivity extends AppCompatActivity implements CustomWebClient.WebClientListener {
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

        Settings.shared.defaultLanguages();
        setTitle("");
        boolean isFirstRun = Storage.shared.isFreshInstall(this);
        if (isFirstRun) {
            Storage.shared.setLanguages(this, Settings.shared.defaultLanguages());
            Storage.shared.setIsFreshInstall(this, false);
            showLanguageSelectionActivity();
        }

        setContentView(R.layout.activity_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_language_black_24dp);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mWebView = findViewById(R.id.webview);
        mProgressBar = findViewById(R.id.webProgressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        mSideDrawer = findViewById(R.id.navigation_view);
        mDrawerLayout = findViewById(R.id.drawerLayout);

        final Context context = this;
        mSideDrawer.setmListener(new OnClickMenu() {
            @Override
            public void onClick(Language language, Site site) {
                Settings.shared.setCurrentLanguage(context, language);
                Settings.shared.setCurrentSite(context, site);
                /*
                Log.e("WIKIREADER", "loading last URL: [" + site.lastVisitedURL + "] for site [" + site.address +"]");*/
                String url = Settings.shared.lastVisitedURL(context, language, site);
                shouldClearHistoryOnLoad = true;
                mWebView.loadUrl(url);
                closeDrawer(null);
            }
        });

        final BrowserActivity finalThis = this;

        WebSettings webViewSettings = mWebView.getSettings();
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setDefaultFontSize(21);
        mWebView.addJavascriptInterface(new JavascriptBridge(this), "javascriptBridge");

        mWebView.setWebViewClient(new CustomWebClient(this));


        mWebView.setWebChromeClient(new LoggingChromeClient());

        if (!isFirstRun) {
            Language language = Settings.shared.getCurrentLanguage(this);
            Site site = Settings.shared.getCurrentSite(this);
            String url = Settings.shared.lastVisitedURL(this, language, site);
            mWebView.loadUrl(url);
        }
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
            openDrawer();
        }
        else if (id == R.id.HomeButton) {
            mWebView.loadUrl(Settings.shared.getCurrentSite(this).address);
        }
        else if (id == R.id.action_back) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            }
            return true;
        }
        else if (id == R.id.action_forward) {
            if (mWebView.canGoForward()) {
                mWebView.goForward();
            }
            return true;
        }
        else if (id == R.id.action_settings) {
            showSettingsActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    void showSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    void showLanguageSelectionActivity() {
        Intent intent = new Intent(this, ContentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if((mDrawerLayout) != null && (mDrawerLayout.isDrawerOpen(GravityCompat.START)))
            closeDrawer(null);
        else {
            super.onBackPressed();
        }
    }

    public void closeDrawer(DrawerLayout.DrawerListener listener) {
        mDrawerLayout.setDrawerListener(listener);
        mDrawerLayout.closeDrawers();
    }

    public void openDrawer() {
        mDrawerLayout.setDrawerListener(null);
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onResume(){
        if (Storage.shared.enabledLanguages(this).size() == 0) {
            String html = "<html><body style='background: #ece6ff; text-align: center; color: #444; '><br><br><p>No content selected, try the settings<p></body></html>";
            mWebView.loadData(html, "text/html; charset=utf-8", "UTF-8");
        }
        else if (Settings.shared.getCurrentSite(this).equals(Site.BlankSite)) {
            String html = "<html><body style='background: #ece6ff; text-align: center; color: #444;'><br><br><p>Select a website from the menu on the left.</p><p><strong>Double-tap a word</strong> to see the dictionary definition</p></body></html>";
            mWebView.loadData(html, "text/html; charset=utf-8", "UTF-8");
        }

        super.onResume();
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
}
