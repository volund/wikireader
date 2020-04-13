package com.putskul_productions.wikireader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.MutableContextWrapper;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Dialogs {
    Context mContext;

    Dialogs(Context pContext) {
        mContext = pContext;
    }

    void showOkDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.show();
    }

    void showOkCancelDialog(String title, int icon,  DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setIcon(icon);
        builder.setPositiveButton("OK", onClickListener);
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    void showDoubleInputDialog(String title, final String inputHint1, final String inputHint2, final DoubleInputListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText input1 = new EditText(mContext);
        final EditText input2 = new EditText(mContext);
        input1.setHint(inputHint1);
        input2.setHint(inputHint2);
        layout.addView(input1);
        layout.addView(input2);

        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value1 = input1.getText().toString();
                String value2 = input2.getText().toString();
                listener.onOk(value1, value2);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    void showListSelection(String title, String[] items, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setItems(items, listener);
        builder.show();
    }

    public interface DoubleInputListener {
        void onOk(String value1, String value2);
    }
}
