package com.instaguard.user.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatButton;

@SuppressLint("AppCompatCustomView")
public class CustomButton extends AppCompatButton {
    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            int style = 0;
            String i = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "textStyle");
            if (i != null) {
                style = Integer.parseInt(i.replace("0x", ""));
            }
            init(context, style);
        }
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            int style = 0;
            String i = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "textStyle");
            if (i != null) {
                style = Integer.parseInt(i.replace("0x", ""));
            }
            init(context, style);
        }
    }

    public CustomButton(Context context) {
        super(context);
        if (!isInEditMode()) {
            init(context, 0);
        }
    }

    private void init(Context ctx, int style) {
        setTypeface(FontManager.getHelvecticaThin(ctx), style);
    }
}
