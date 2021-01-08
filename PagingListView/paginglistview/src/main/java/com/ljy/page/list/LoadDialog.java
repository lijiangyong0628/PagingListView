package com.ljy.page.list;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ljy.page.library.R;

public class LoadDialog extends Dialog {

    public Context context;
    ProgressBar progressBar;
    TextView msgTv;
    View cancelBtn;
    private boolean canCancel = false;
    private String message = "";

    public LoadDialog(@NonNull Context context) {
        this(context, R.style.Dialog);
    }

    public LoadDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected LoadDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.layout_dialog_load, null);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        progressBar = findViewById(R.id.progressBar);
        msgTv = findViewById(R.id.msgTv);
        cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                cancelBtn.setVisibility(canCancel ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    public void setMessage(String message) {
        this.message = message;
        if (msgTv != null) {
            if (TextUtils.isEmpty(message)) {
                msgTv.setText("");
            } else {
                msgTv.setText(message);
            }
        }
    }

    public void setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}