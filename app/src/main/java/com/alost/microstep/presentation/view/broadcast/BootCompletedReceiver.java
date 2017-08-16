package com.alost.microstep.presentation.view.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alost.microstep.presentation.service.GlobalService;


/**
 * @author Alost
 *         静态广播，开机启动：用于启动我们需要的服务，这样就可以1、再去启动其他广播2、其他服务等
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    //重写onReceive方法
    @Override
    public void onReceive(Context context, Intent intent) {
        GlobalService.startService(context);
        Log.v("zou", "开机自动服务自动启动.....");

    }
}
