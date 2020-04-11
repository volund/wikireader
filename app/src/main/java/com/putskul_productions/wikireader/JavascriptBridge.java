package com.putskul_productions.wikireader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavascriptBridge {
    BrowserActivity mContext;

    /** Instantiate the interface and set the context */
    JavascriptBridge(BrowserActivity context) {
        mContext = context;
    }

    @JavascriptInterface
    public void lookupWord(String word) {
        String cleanWord = word;

        String[] prefixes = {"L'", "l'", "D'", "d'", "C'", "c'", "Un'", "un'", "dell'", "Dell'", "Nell'", "nell'"};
        for (String prefix : prefixes) {
            if (word.startsWith(prefix)) {
                cleanWord = word.replaceFirst(prefix, "");
                break;
            }
        }

        lookupWithoutCleaning(cleanWord);
    }

    public void lookupWithoutCleaning(final String word) {
        final BrowserActivity act = mContext;
        final String lookupWord = word;
        final Runnable onDismiss = new Runnable() {
            @Override
            public void run() {
                //act.mWebView.evaluateJavascript("var selectiondiv = document.getElementById('__wikireader_selectiondiv'); selectiondiv.style.filter = 'alpha(opacity=0)'; selectiondiv.style.display='block'; var op = 1; var timer = setInterval(function () {  if (op <= 0.1){    clearInterval(timer);    selectiondiv.style.display = 'none';  } selectiondiv.style.opacity = op; selectiondiv.style.filter = 'alpha(opacity=' + op * 100 + \")\"; op -= op * 0.1; }, 20);", null);
                //act.mWebView.evaluateJavascript("var selectiondiv = document.getElementById('__wikireader_selectiondiv'); selectiondiv.style.display='block'; setTimeout(function() { selectiondiv.style.display='none'; setTimeout(function() { selectiondiv.style.display='block'; setTimeout(function() { selectiondiv.style.display='none'; }, 100); }, 100);}, 100);", null);
                act.mWebView.evaluateJavascript("var selectiondiv = document.getElementById('__wikireader_selectiondiv'); selectiondiv.style.display='block'; setTimeout(function() { selectiondiv.style.display='none'; }, 100);", null);
            }
        };
        mContext.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Language language = Settings.shared.getCurrentLanguage(act);
                Dictionary dictionary = language.currentDictionary;
                String wordURL = dictionary.urlForWord(lookupWord);
                LookupDialog lookupDialog = new LookupDialog(act, wordURL, onDismiss);
                lookupDialog.show();
            }
        });
    }

    @JavascriptInterface
    public void lookupCompositeWord(String word) {
        if (word.contains("'")) {
            ArrayList<String> options = new ArrayList<String>();
            for (String op: word.split("'")) {
                options.add(op);
            }
            String firstOp = options.remove(0);
            options.add(word);
            options.add(firstOp);

            final String [] values = options.toArray(new String[options.size()]);

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setItems(values, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    lookupWithoutCleaning(values[which]);
                }
            });
            builder.show();

        }
        else {
            lookupWithoutCleaning(word);
        }
    }

    @JavascriptInterface
    public void handleLink(String address, String title) {
        //Log.e("WIKIREADER", "Handling URL " + address + " for " + title);

        ArrayList<String> options = new ArrayList<String>();
        for (String op: title.split(" ")) {
            options.add(op);
        }
        options.add("Follow '" + title + "'");

        final String [] values = options.toArray(new String[options.size()]);

        final String relativeHref = address;

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setItems(values, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which < values.length - 1) {
                    lookupWord(values[which]);
                } else {
                    //Log.e("WIKIREADER", "following " + relativeHref);
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContext.mWebView.evaluateJavascript("window.location.href = '" + relativeHref + "'", null);
                        }
                    });
                }
            }
        });
        builder.show();
    }

    @JavascriptInterface
    public void saveExpandedSections(String expandedSections) {
        Log.e("WIKIREADER", "saving sections " + expandedSections);
        Settings.shared.setExpandedSections(mContext, expandedSections);

    }

}