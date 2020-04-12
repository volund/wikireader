package com.putskul_productions.wikireader;

import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

public class LoggingChromeClient extends WebChromeClient {
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.e("WIKIREADER", "[JAVASCRPIT] " + consoleMessage.message() + " -- From line "
                + consoleMessage.lineNumber() + " of "
                + consoleMessage.sourceId());
        return super.onConsoleMessage(consoleMessage);
    }
}
