package com.instaguard.user.CustomViews;

import android.content.Context;
import android.graphics.Typeface;

import com.instaguard.user.R;


public class FontManager {
    private static Typeface tfHelvectica;
    private static Typeface tfHelvecticaItalic;
    private static Typeface tftitleFont;

    static Typeface getHelvecticaThin(Context ctx) {
        if (tfHelvectica == null) {
            tfHelvectica = Typeface.createFromAsset(ctx.getAssets(), ctx.getString(R.string.App_Font));
        }
        return tfHelvectica;
    }
//    robotoslab_regular

    public static Typeface getHelvecticaThinItalic(Context ctx) {
        if (tfHelvecticaItalic == null) {
            tfHelvecticaItalic = Typeface.createFromAsset(ctx.getAssets(), ctx.getString(R.string.App_Font));
        }
        return tfHelvecticaItalic;
    }

    public static Typeface getTitleFont(Context ctx) {
        if (tftitleFont == null) {
            tftitleFont = Typeface.createFromAsset(ctx.getAssets(), ctx.getString(R.string.App_Font));
        }
        return tftitleFont;
    }
}
