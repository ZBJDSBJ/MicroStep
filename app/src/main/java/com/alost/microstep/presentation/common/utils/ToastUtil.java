package com.alost.microstep.presentation.common.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.alost.microstep.presentation.common.BaseApplication;


/**
 * Created by Alost on 17/1/12.
 */

public class ToastUtil {

    public static final void showToast(Context context, String textDesc) {
        Toast.makeText(context, textDesc, Toast.LENGTH_SHORT).show();
    }

    public static final void showToast(Context context, int text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(String showString) {
        Toast toast = Toast.makeText(BaseApplication.getAppContext(), showString + "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
