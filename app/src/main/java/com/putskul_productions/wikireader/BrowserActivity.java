package com.putskul_productions.wikireader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Set;


public class BrowserActivity extends AppCompatActivity {
    protected SimpleLeftMenuView mLeftMenuView;
    protected DrawerLayout mDrawerLayout;
    WebView mWebView = null;
    boolean shouldClearHistoryOnLoad = false;
    MenuItem historyBackButton;
    MenuItem historyForwardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Settings.shared.defaultLanguages();

        boolean isFirstRun = Storage.shared.isFreshInstall(this);
        if (isFirstRun) {
            Storage.shared.setLanguages(this, Settings.shared.defaultLanguages());
            Storage.shared.setIsFreshInstall(this, false);
            showLanguageSelectionActivity();
        }

        setContentView(R.layout.activity_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_language_black_24dp);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.ic_language_black_24dp);
        mWebView = findViewById(R.id.webview);
        mLeftMenuView = findViewById(R.id.navigation_view);
        mDrawerLayout = findViewById(R.id.drawerLayout);

        final Context context = this;
        mLeftMenuView.setmListener(new OnClickMenu() {
            @Override
            public void onClick(Language language, Site site) {

                Settings.shared.setCurrentLanguage(context, language);
                Settings.shared.setCurrentSite(context, site);
                /*
                Log.e("WIKIREADER", "loading last URL: [" + site.lastVisitedURL + "] for site [" + site.address +"]");*/
                String url = Settings.shared.lastVisitedURL(context, language, site);
                Log.e("WIKIREADER", "DBG loading last URL: [" + url + "] for site [" + language.label + "-" + site.label +"]");
                Log.e("WIKIREADER", "DBG did clear history ");
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

        mWebView.setWebViewClient(new WebViewClient() {
            private int       webViewPreviousState;
            private final int PAGE_STARTED    = 0x1;
            private final int PAGE_REDIRECTED = 0x2;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                webViewPreviousState = PAGE_REDIRECTED;
                mWebView.loadUrl(urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webViewPreviousState = PAGE_STARTED;

                Language language = Settings.shared.getCurrentLanguage(finalThis);
                Site site = Settings.shared.getCurrentSite(finalThis);

                /*
                site.lastVisitedURL = url;
                Log.e("WIKIREADER", "setting last URL: [" + site.lastVisitedURL + "] for site [" + site.address +"]");
                Storage.shared.updateLanguage(finalThis, language); // use implicit fact that the current site is part of the current language
                */
                Log.e("WIKIREADER", "DBG setting last URL: [" + site.lastVisitedURL + "] for  [" + language.label + "-" +site.label +"]");
                Settings.shared.setLastVisitedURL(finalThis, language, site, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if(shouldClearHistoryOnLoad){
                    Log.d("WIKIREADER", "DBG A clearing History now");
                    shouldClearHistoryOnLoad = false;
                    mWebView.clearHistory();
                }

                historyBackButton.setEnabled(mWebView.canGoBack());
                historyBackButton.getIcon().setAlpha(mWebView.canGoBack() ? 255 : 100);
                historyForwardButton.setEnabled(mWebView.canGoForward());
                historyForwardButton.getIcon().setAlpha(mWebView.canGoForward() ? 255 : 100);

                if (webViewPreviousState == PAGE_STARTED) {
                    final String clickEvent = "document.body.addEventListener('click', function(event){  " +
                            "  var target = event.target || event.srcElement; " +
                            "  if ((target.tagName === 'A') || (target.tagName === 'a')) { " +
                            "    console.log('DBG did tap link' + JSON.stringify(target)); " +
                            "    event.preventDefault(); javascriptBridge.handleLink(target.getAttribute('href'), target.innerHTML); " +
                            "    return false; " +
                            "  } " +
                            "  else if ((target.tagName === 'h2') || (target.tagName === 'H2')) {" +
                            "    if ((target.className != null) && (target.className.indexOf('collapsible-heading') >= 0)) { " +
                            "      var expanded_ids = [];" +
                            "      var divs = document.querySelectorAll('div[aria-expanded=\"true\"');" +
                            "      var expanded_ids = Array.prototype.map.call(divs, function(div) { return div.id }); " +
                            "      javascriptBridge.saveExpandedSections(JSON.stringify(expanded_ids));" +
                            "    } " +
                            "  }" +
                            "}, false); ";

                    final String dblclickEvent = "document.body.addEventListener('dblclick', function(event){ " +
                    "  wr_wikiReaderWordLookup(true); " +
			        "}, false); ";

                    final String lookupFunction = "function wr_wikiReaderWordLookup(composite) { " +
                            "  var s = window.getSelection();" +
                            "  s.modify('extend','backward','word'); " +
                            "  var rect1 = s.getRangeAt(0).getBoundingClientRect();" +
                            "  var b = s.toString();" +
                            "  s.modify('extend','forward','word');" +
                            "  var a = s.toString();" +
                            "  var rect2 = s.getRangeAt(0).getBoundingClientRect();" +
                            "  s.modify('move','forward','character');" +
                            "  var selectiondiv = document.getElementById('__wikireader_selectiondiv');" +
                            "  if (selectiondiv == null) {" +
                            "    selectiondiv = document.createElement('div');" +
                            "    selectiondiv.setAttribute('id', '__wikireader_selectiondiv');" +
                            "    selectiondiv.style.display='none'; document.body.appendChild(selectiondiv);" +
                            "  }" +
                            "  var rx=s.getRangeAt(0).getBoundingClientRect();" +
                            "  var relative=document.body.parentNode.getBoundingClientRect();" +
                            "  selectiondiv.style.top =(rx.bottom -relative.top)+'px';" +
                            "  selectiondiv.style.right=-(rx.right-relative.right)+'px';" +
                            "  selectiondiv.style.position='absolute';" +
                            "  selectiondiv.style.borderTop = '3px solid #000000';" +
                            "  selectiondiv.style.width = (rect1.width + rect2.width)+'px';" +
                            "  if (composite) {" +
                            "    javascriptBridge.lookupCompositeWord(b+a);" +
                            "  }" +
                            "  else {" +
                            "    javascriptBridge.lookupWord(b+a);" +
                            "  }" +
                            "}";

                    Log.e("WIKIREADER", "DBG X on start expanding " + Settings.shared.getExpandedSections(finalThis));

                    // TODO: this script can't find the right h2 to click it...
                    //       seems to work though if I just select al h2s (except then it expands all sections)
                    final String expandSections = "" +
                            "var expanded_section_ids = JSON.parse('" + Settings.shared.getExpandedSections(finalThis) + "'); " +
                            " for (var expanded_section_id of expanded_section_ids) { " +
                            "    console.log('DBG clicking ' + expanded_section_id + '| ' + 'h2[aria-controls=\"' + expanded_section_id + '\"]'); " +
                            "    var h2s = document.querySelectorAll('h2[aria-controls=\"' + expanded_section_id + '\"');" +
                            "    console.log('DBG found for clicking ' + JSON.stringify(h2s));" +
                            "    for (var h2 of h2s) { " +
                            "      h2.click(); console.log('DBG clicked on ' + h2); " +
                            "    } " +
                            " } ";


                    mWebView.evaluateJavascript(lookupFunction + clickEvent + dblclickEvent + expandSections, null);
                }

            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e("WEBVIEW", consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }
        });

        if (!isFirstRun) {
            Language language = Settings.shared.getCurrentLanguage(this);
            Site site = Settings.shared.getCurrentSite(this);
            String url = Settings.shared.lastVisitedURL(this, language, site);
            mWebView.loadUrl(url);
        }
    }

    public void promptForLink(String url) {
        Log.e("WIKIREADER", "prompting for " + url);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

/*
        Log.e("WIKIREADER", "DBG T00 saving state..");
        String saveStateScript =  "  console.log('DBG T08'); " +
                "  var expanded_ids = [];" +
                "  var divs = document.querySelectorAll('div[aria-expanded=\"true\"');" +
                "  var expanded_ids = Array.prototype.map.call(divs, function(div) { return div.id }); " +
                "  console.log('DBG T1 found expanded: ' + JSON.stringify(expanded_ids)); " +
                "  javascriptBridge.testx('hello test x');";
               // "  javascriptBridge.saveExpandedSections(JSON.stringify(expanded_ids));";


        mWebView.evaluateJavascript(saveStateScript, null);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browser, menu);
        historyBackButton = menu.findItem(R.id.action_back);
        historyForwardButton = menu.findItem(R.id.action_forward);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        final BrowserActivity finalThis = this;
        if (id == android.R.id.home) {
            openDrawer();
        }
        else if (id == R.id.HomeButton) {
            Log.e("WIKIREADER", "DBG A going home");
            mWebView.loadUrl(Settings.shared.getCurrentSite(this).address);
        }
        else if (id == R.id.action_back) {
            Log.e("WIKIREADER", "DBG back pressed");
            if (mWebView.canGoBack()) {
                Log.e("WIKIREADER", "DBG going back in webview");
                mWebView.goBack();
            }
            return true;
        }
        else if (id == R.id.action_forward) {
            mWebView.goForward();
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
        super.onResume();
        mLeftMenuView.setData();
    }
}
