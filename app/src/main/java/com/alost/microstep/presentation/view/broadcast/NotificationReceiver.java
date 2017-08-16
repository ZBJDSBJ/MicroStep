package com.alost.microstep.presentation.view.broadcast;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alost.microstep.MainActivity;
import com.alost.microstep.presentation.common.BaseApplication;
import com.alost.microstep.presentation.common.utils.PublicUtils;


/**
 * 通知栏点击通知监听
 */
public class NotificationReceiver extends BroadcastReceiver {
    private Context mContext;
    public static final String KEY_NOTIFICATION_ENTRANCE = "key_notification_entrance";
    public static final String VALUE_NOTIFICATION_PEDOMETER = "notification_pedometer";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("zou", "<NotificationReceiver> onReceive " + intent.getStringExtra(KEY_NOTIFICATION_ENTRANCE));

        // 跳转之前要处理的逻辑
        setPendingIntent(intent);
    }

    /**
     * 设置通知栏跳转的activity
     */
    private void setPendingIntent(Intent intent) {
        mContext = BaseApplication.getAppContext();
        String entrance = intent.getStringExtra(KEY_NOTIFICATION_ENTRANCE);
        if (entrance == null) {
            entrance = "";
        }

        //默认是主的acitivity
        String topClassName = MainActivity.class.getName();
        try {
            topClassName = PublicUtils.getTopActivityClassName(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (topClassName.contains(mContext.getPackageName())) {
            Intent newIntent = new Intent();
            ComponentName componentName = new ComponentName("com.alost.alina", topClassName);
            newIntent.setComponent(componentName);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(newIntent);
        } else if (VALUE_NOTIFICATION_PEDOMETER.equals(entrance)) {
            MainActivity.newInstance(mContext);
        }
    }

}