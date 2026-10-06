package com.instaguard.user.Helpers.Localizationactivity.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;


import com.instaguard.user.Helpers.Localizationactivity.core.LocalizationActivityDelegate;
import com.instaguard.user.Helpers.Localizationactivity.core.OnLocaleChangedListener;

import java.util.Locale;

public abstract class LocalizationActivity extends AppCompatActivity implements OnLocaleChangedListener {

    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        localizationDelegate.onResume(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(newBase));
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }

    @Override
    public Resources getResources() {
        return localizationDelegate.getResources(super.getResources());
    }

    public final void setLanguage(String language) {
        localizationDelegate.setLanguage(this, language);
    }

    public final void setLanguageBelow23(String language, String country) {
        localizationDelegate.setLanguage(this, language,country);
    }
    public final void setLanguage(Locale locale) {
        localizationDelegate.setLanguage(this, locale);
    }

    public final void setDefaultLanguage(String language) {
        localizationDelegate.setDefaultLanguage(language);
    }

    public final void setDefaultLanguage(Locale locale) {
        localizationDelegate.setDefaultLanguage(locale);
    }

    public final Locale getCurrentLanguage() {
        return localizationDelegate.getLanguage(this);
    }

    // Just override method locale change event
    @Override
    public void onBeforeLocaleChanged() {
    }

    @Override
    public void onAfterLocaleChanged() {
    }

    public static class CustomTFSpan extends TypefaceSpan {

        private Typeface typeface;

        public CustomTFSpan(Typeface typeface) {
            super("");
            this.typeface = typeface;
        }

        private static void applyTypeFace(Paint paint, Typeface tf) {
            paint.setTypeface(tf);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            applyTypeFace(ds, typeface);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            applyTypeFace(paint, typeface);
        }
    }
}
