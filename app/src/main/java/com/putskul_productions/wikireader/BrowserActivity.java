/*
Copyright 2021 Amos JOSHUA

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
    
package com.putskul_productions.wikireader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class BrowserActivity extends AppCompatActivity implements CustomWebClient.WebClientListener, SideDrawerListener {
    String no_content_html = "<html><body style='background: #ece6ff; text-align: center; color: #444; '><br><br><p>No content selected, try the settings<p></body></html>";
    String no_site_selected_html = "<html><body style='background: #ece6ff; text-align: center; color: #444;'><br><br><p>Press \uD83C\uDF10 to select a website.</p></body></html>";

    protected SideDrawerView mSideDrawer;
    protected DrawerLayout mDrawerLayout;
    MenuItem historyBackButton;
    MenuItem historyForwardButton;
    WebView mWebView = null;
    LinearLayout mProgressBar;
    boolean shouldClearHistoryOnLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        mWebView = findViewById(R.id.webview);
        mProgressBar = findViewById(R.id.webProgressBar);
        mSideDrawer = findViewById(R.id.navigation_view);
        mDrawerLayout = findViewById(R.id.drawerLayout);

        mSideDrawer.setListener(this);
        configureToolbar();
        configureWebView();
        loadLanguagesOrLastUrl();
    }

    void loadLanguagesOrLastUrl() {
        if (App.shared.settings.isFreshInstall(this)) {
            App.shared.model.setLanguages(this, App.shared.model.defaultLanguages());
            App.shared.settings.setIsFreshInstall(this, false);
            //showActivity(ContentActivity.class);
            showActivity(WelcomeActivity.class);
        }
        else {
            Language language = App.shared.settings.getCurrentLanguage(this);
            Site site = App.shared.settings.getCurrentSite(this);
            String url = App.shared.settings.lastVisitedURL(this, language, site);
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
            mWebView.loadUrl(App.shared.settings.getCurrentSite(this).address);
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
        if ((mDrawerLayout) != null && (mDrawerLayout.isDrawerOpen(GravityCompat.START))) {
            mDrawerLayout.closeDrawers();
        }
        else if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (!App.shared.model.hasEnabledLanguages(this)) {
            mWebView.loadData(no_content_html, "text/html; charset=utf-8", "UTF-8");
        }
        else if (App.shared.settings.currentSiteIsBlank(this)) {
            // Does not load correctly after first time selecting content
            // loading from the UI thread after a delay does not fix
            // but for some unknown reason loading it twice fixes it
            mWebView.loadData(no_site_selected_html, "text/html; charset=utf-8", "UTF-8");
            mWebView.loadData(no_site_selected_html, "text/html; charset=utf-8", "UTF-8");
        }
        mSideDrawer.loadData();
    }

    @Override
    public void onPageStarted(String url) {
        Language language = App.shared.settings.getCurrentLanguage(this);
        Site site = App.shared.settings.getCurrentSite(this);
        App.shared.settings.setLastVisitedURL(this, language, site, url);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(String URL) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if(shouldClearHistoryOnLoad){
            shouldClearHistoryOnLoad = false;
            mWebView.clearHistory();
        }
        if (historyBackButton != null) {
            historyBackButton.setEnabled(mWebView.canGoBack());
            historyBackButton.getIcon().setAlpha(mWebView.canGoBack() ? 255 : 100);
        }
        if (historyForwardButton != null) {
            historyForwardButton.setEnabled(mWebView.canGoForward());
            historyForwardButton.getIcon().setAlpha(mWebView.canGoForward() ? 255 : 100);
        }

    }

    // SideDrawerListener
    @Override
    public void onSideDrawerItemClick(Language language, Site site) {
        App.shared.settings.setCurrentLanguage(this, language);
        App.shared.settings.setCurrentSite(this, site);
        String lastUrl = App.shared.settings.lastVisitedURL(this, language, site);

        shouldClearHistoryOnLoad = true;
        mWebView.loadUrl(site.address);
        mDrawerLayout.closeDrawers();
    }
}
