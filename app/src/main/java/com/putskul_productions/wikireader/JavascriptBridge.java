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

import android.content.DialogInterface;
import android.webkit.JavascriptInterface;

public class JavascriptBridge {
    String lookup_script = "var selectiondiv = document.getElementById('__wikireader_selectiondiv'); selectiondiv.style.display='block'; setTimeout(function() { selectiondiv.style.display='none'; }, 100);";
    BrowserActivity mContext;
    Dialogs dialogs;

    JavascriptBridge(BrowserActivity context) {
        mContext = context;
        dialogs = new Dialogs(context);
    }

    public void lookup(final String word) {
        final BrowserActivity act = mContext;
        final Runnable onDismiss = new Runnable() {
            @Override
            public void run() {
                act.mWebView.evaluateJavascript(lookup_script, null);
            }
        };
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Language language = App.shared.settings.getCurrentLanguage(act);
                Dictionary dictionary = language.currentDictionary;
                String wordURL = dictionary.urlForWord(word);
                LookupDialog lookupDialog = new LookupDialog(act, wordURL, onDismiss);
                lookupDialog.show();
            }
        });
    }

    @JavascriptInterface
    public void lookupCompositeWord(String word) {
        if (word.contains("'")) {
            final String[] values = StringUtils.splitToArrayAndAdd(word, "'", word);
            dialogs.showListSelection("", values, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    lookup(values[which]);
                }
            });
        }
        else {
            lookup(word);
        }
    }

    @JavascriptInterface
    public void handleLink(String relativeHref, String title) {
        if ((title == null) || title.equals("")) {
            followLink(relativeHref);
        }
        else {
            presentLinkOptions(relativeHref, title);
        }
    }

    void presentLinkOptions(final String relativeHref, String title) {
        String cleanTitle = StringUtils.extractTextFromHtml(title);
        final String[] values = StringUtils.splitToArrayAndAdd(cleanTitle, " ", "Follow '" + cleanTitle + "'");
        dialogs.showListSelection("", values, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which < values.length - 1) {
                    dialog.dismiss();
                    lookupCompositeWord(values[which]);
                }
                else {
                    followLink(relativeHref);
                }
            }
        });
    }

    void followLink(final String link) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContext.mWebView.evaluateJavascript("window.location.href = '" + link + "'", null);
            }
        });
    }
}
