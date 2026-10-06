package com.instaguard.user.CustomViews.CustomSwitch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.R;


/**
 * Created by uplogic.
 */
public class ToggleSwitchButton{

    private View view;
    private CustomTextView textView;
    private View separator;

    public ToggleSwitchButton(Context context) {
        this(LayoutInflater.from(context).inflate(R.layout.switch_item_toggle, null));
    }

    public ToggleSwitchButton(View view) {
        this.view = view;
        this.textView = (CustomTextView) view.findViewById(R.id.text_view);
        this.separator = view.findViewById(R.id.separator);
    }

    public View getView() {
        return view;
    }

    public TextView getTextView() {
        return textView;
    }

    public View getSeparator() {
        return separator;
    }

    public void showSeparator(){
        getSeparator().setVisibility(View.VISIBLE);
    }

    public void hideSeparator(){
        getSeparator().setVisibility(View.INVISIBLE);
    }
}
