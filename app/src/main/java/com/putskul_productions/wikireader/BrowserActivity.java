package com.putskul_productions.wikireader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class BrowserActivity extends AppCompatActivity {

    WebView mWebView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mWebView = findViewById(R.id.webview);

        final BrowserActivity finalThis = this;

        WebSettings webViewSettings = mWebView.getSettings();
        webViewSettings.setJavaScriptEnabled(true);
        Log.e("WIKIREADER", "DEFAULT font size " + webViewSettings.getDefaultFontSize());
        webViewSettings.setDefaultFontSize(21);
        mWebView.addJavascriptInterface(new JavascriptBridge(this), "javascriptBridge");

        mWebView.setWebViewClient(new WebViewClient() {
            private int       webViewPreviousState;
            private final int PAGE_STARTED    = 0x1;
            private final int PAGE_REDIRECTED = 0x2;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                webViewPreviousState = PAGE_REDIRECTED;
                Log.e("WIKIREADER", "DBG should load |" + urlNewString + "|");
                mWebView.loadUrl(urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webViewPreviousState = PAGE_STARTED;
                Log.e("WIKIREADER", "DBG started loading!");
            }

            @Override
            public void onPageFinished(WebView view, String url) {


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

                    Log.e("WIKIREADER", "DBG finished loading!");

                    Settings.shared.setLastVisitedURL(finalThis, url);
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

        String url = Settings.shared.lastVisitedURL(this);
        mWebView.loadUrl(url);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        final BrowserActivity finalThis = this;
        if (id == R.id.action_back) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            }
            else {
                //mWebView.loadUrl("https://it.wikipedia.org/");
                mWebView.loadUrl("https://valerianeglia.wordpress.com");
            }
            return true;
        }
        else if (id == R.id.action_forward) {
            mWebView.goForward();
        }
        else if (id == R.id.action_languages) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final String[] sourceLangs = Settings.shared.sourceLanguages();
            builder.setItems(sourceLangs, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String sourceLang = sourceLangs[which];
                    final String[] dictLangs = Settings.shared.languageMap.get(sourceLang);


                    AlertDialog.Builder builder = new AlertDialog.Builder(finalThis);
                    builder.setItems(dictLangs, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String dictLang = dictLangs[which];
                            Settings.shared.setSourceLanguage(finalThis, sourceLang);
                            Settings.shared.setDictionaryLanguage(finalThis, dictLang);
                            mWebView.clearHistory();
                            mWebView.loadUrl(Settings.shared.lastVisitedURL(finalThis));
                        }
                    });
                    builder.show();
                }
            });
            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
