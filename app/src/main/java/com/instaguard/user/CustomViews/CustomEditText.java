package com.instaguard.user.CustomViews;

import android.content.Context;
import android.util.AttributeSet;

import com.rengwuxian.materialedittext.MaterialEditText;

public class CustomEditText extends MaterialEditText {
    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
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

    public CustomEditText(Context context, AttributeSet attrs) {
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

    public CustomEditText(Context context) {
        super(context);
        if (!isInEditMode()) {
            init(context, 0);
        }
    }

    private void init(Context ctx, int style) {
        setTypeface(FontManager.getHelvecticaThin(ctx), style);
    }
}
