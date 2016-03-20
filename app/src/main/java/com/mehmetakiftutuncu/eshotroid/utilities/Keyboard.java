package com.mehmetakiftutuncu.eshotroid.utilities;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Keyboard {
    public static void toggleKeyboard(Context context, View view, boolean show) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (show) {
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } else {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
