package com.alost.microstep.presentation.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.alost.microstep.R;
import com.alost.microstep.presentation.view.broadcast.GlobalBroadcastReceiver;


/**
 * @author Alost
 *         管理全局的服务
 */
public class GlobalService extends Service {

    public final static int GRAY_SERVICE_ID = 1001;

    public static void startService(Context context) {
        Intent service = new Intent(context, GlobalService.class);
        context.startService(service);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //进程保活
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            startForeground(GRAY_SERVICE_ID, builder.build());
            startService(new Intent(this, CancelService.class));
        } else {
            startForeground(GRAY_SERVICE_ID, new Notification());
        }

        GlobalBroadcastReceiver mGlobalBroadcastReceiver = new GlobalBroadcastReceiver();
        mGlobalBroadcastReceiver.initData();
    }

    /**
     * Service的检查与启动
     */
    public static void checkServiceStart(Context context) {

        boolean isRunning = false;
//        Log.i("zou", "<GlobalService> checkServiceStart isRunning = " + isRunning);
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.alost.alina.presentation.service.GlobalService".equals(service.service.getClassName())) {
                isRunning = true;
                break;
            }
        }

        if (!isRunning) {
            GlobalService.startService(context);
        }
    }
}
