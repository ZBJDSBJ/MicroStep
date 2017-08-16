package com.alost.microstep.presentation.common.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * @author Alost
 */
public class PublicUtils {

    public static String getTopActivityClassName(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = manager.getRunningTasks(10);
        String MY_PKG_NAME = context.getPackageName();
        int i = 0;
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME) || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                Log.i("zou", "<NotificationReceiver> " + info.topActivity.getPackageName() + " info.baseActivity.getPackageName()=" + info.baseActivity.getPackageName());
                break;
            }
            i++;
        }
        ActivityManager.RunningTaskInfo info;
        if (list.size() == 1) {
            info = list.get(0);
        } else {
            info = list.get(i);
        }
        String className = info.topActivity.getClassName();
        Log.i("zou", "<NotificationReceiver> className =" + className);

        return className;
    }

}
