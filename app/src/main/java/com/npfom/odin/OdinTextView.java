package com.npfom.odin;

import android.widget.TextView;

/*
    Custom textview for displaying messages to be sent and received from the database.
 */
public class OdinTextView implements RequestInterface {
    private TextView textView = null;

    public OdinTextView(TextView tw) {
        textView = tw;
    }

    // Method for modifying the text.
    @Override
    public void process(String str) {
        if (str.startsWith("{\"error\":false")) {
            textView.append("\nReport sent successfully!");
        } else {
            textView.append("\nReport failed!" +
                    "\nError Message: " + str);
        }
    }
}

