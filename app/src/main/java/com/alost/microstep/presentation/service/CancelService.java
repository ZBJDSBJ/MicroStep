package com.alost.microstep.presentation.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;

import com.alost.microstep.R;


/**
 * @author Alost
 *         4.3以上也设置为后台进程,防止被杀
 */
public class CancelService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        startForeground(GlobalService.GRAY_SERVICE_ID, builder.build());
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                stopForeground(true);
                NotificationManager manager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.cancel(GlobalService.GRAY_SERVICE_ID);
                stopSelf();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
