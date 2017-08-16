package com.alost.microstep.presentation.view.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.PowerManager;

import com.alost.microstep.data.model.pedometer.IRxPedometerRepositoryIml;
import com.alost.microstep.presentation.common.BaseApplication;
import com.alost.microstep.presentation.common.utils.HardwarePedometerUtil;
import com.alost.microstep.presentation.module.ApplicationModule;


/**
 * @author Alost
 *         全局广播
 *         注意：ACTION_TIME_TICK不能用于静态注册，原因为：
 *         <p>
 *         1.提高系统效率：这两个事件是Android的基本事件，如果大多数程序监听，会大大的拖慢整个系统，所以android不鼓励我们在后台监听这两个事件。
 *         2.因为有序广播的优先级问题。以上这些广播中，静态注册时，系统的优先级大于应用，并且系统阻止了广播的向下传播。又因在Android 的广播机制中，动态注册的优先级是要高于静态注册优先级的。故用动态注册代替静态注册。
 *         3.系统安全问题。
 *         <p>
 *         4.不能静态注册的广播还有以下几个：
 *         <p>
 *         android.intent.action.SCREEN_ON
 *         android.intent.action.SCREEN_OFF
 *         android.intent.action.BATTERY_CHANGED
 *         android.intent.action.CONFIGURATION_CHANGED
 */
public class GlobalBroadcastReceiver {

    private Context mContext;

    //监听时间变化的广播 这个receiver只能动态创建
    private CheckServiceReceiver mCheckServiceReceiver;
    private IntentFilter mTimeFilter;

    private ScreenChangeReceiver mScreenChangeReceiver;


    public GlobalBroadcastReceiver() {
        mContext = BaseApplication.getAppContext();
    }

    public void initData() {
        mTimeFilter = new IntentFilter();
        mTimeFilter.addAction(Intent.ACTION_TIME_TICK);
        mTimeFilter.addAction(Intent.ACTION_DATE_CHANGED); //每天变化的action
        mTimeFilter.addAction(Intent.ACTION_TIME_CHANGED);
        mCheckServiceReceiver = new CheckServiceReceiver();
        mContext.registerReceiver(mCheckServiceReceiver, mTimeFilter);


        IntentFilter screenFilter = new IntentFilter();
        screenFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenFilter.addAction(Intent.ACTION_SCREEN_OFF); //每天变化的action
        screenFilter.addAction(Intent.ACTION_USER_PRESENT);
        screenFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mScreenChangeReceiver = new ScreenChangeReceiver();
        mContext.registerReceiver(mScreenChangeReceiver, screenFilter);

        initOnlyAcclPowerManager();

    }

    public void clean() {
        try {
            if (mCheckServiceReceiver != null)
                mContext.unregisterReceiver(mCheckServiceReceiver);
            if (mScreenChangeReceiver != null)
                mContext.unregisterReceiver(mScreenChangeReceiver);
            mContext = null;
            mTimeFilter = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 全局广播检测service是否挂掉
     */
    public class CheckServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//            Log.i("zou", "<GlobalBroadcastReceiver> action = " + action);

            //检查计步服务是否开启,保证在满足条件下开启计步服务
            ApplicationModule.getInstance().getPedometerManager().checkServiceStart();

            //每分钟存计步详情数据
            ApplicationModule.getInstance().getPedometerManager().updateStepDailyDetailToFile();

            //更新每日计步器
            ApplicationModule.getInstance().getPedometerManager().updateByDateChanged();
        }
    }


    public class ScreenChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();

            if (Intent.ACTION_SCREEN_ON.equals(action)) {
//                Log.d("zou", "<GlobalBroadcastReceiver> screen on");

                acquirePowerManager(true);
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
//                Log.d("zou", "<GlobalBroadcastReceiver> screen off");

                acquirePowerManager(false);

            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
//                Log.d("zou", "<GlobalBroadcastReceiver> screen unlock");
            } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
//                Log.i("zou", "<GlobalBroadcastReceiver> receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
            }
        }
    }


    private PowerManager mPowerManager = null; // 电源管理服务
    private PowerManager.WakeLock mWakeLock = null;  // 屏幕灯
    private SensorManager mSensorManager = null;  // 传感器服务
    private IRxPedometerRepositoryIml mPedometerRepositoryIml = null;  // 传感器监听对象

    private void initOnlyAcclPowerManager() {
        if (HardwarePedometerUtil.supportOnlyAccelerometer(mContext)) {
            // 电源管理服务  PARTIAL_WAKE_LOCK : CPU 运转，屏幕和键盘灯关闭
            mPowerManager = (PowerManager) BaseApplication.getAppContext().getSystemService(Context.POWER_SERVICE);
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Accel");
            mPedometerRepositoryIml = ApplicationModule.getInstance().getPedometerRepository();
            mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        }
    }

    private void acquirePowerManager(boolean isScreenOn) {
        if (mPowerManager == null || mWakeLock == null
                || mPedometerRepositoryIml == null || mSensorManager == null) {
            initOnlyAcclPowerManager();
        }

        if (mWakeLock != null) {
            if (isScreenOn) {
                try {
                    mWakeLock.release();
                } catch (Throwable throwable) {
//                    Log.i("zou", "<GlobalBroadcastReceiver> mWakeLock.release failed");
                }
            } else {
                mWakeLock.acquire();

                if (mSensorManager != null) {//取消监听后重写监听，以保持后台运行
                    mSensorManager.unregisterListener(mPedometerRepositoryIml);
                    mSensorManager.registerListener(mPedometerRepositoryIml, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                            SensorManager.SENSOR_DELAY_UI);
                }
            }
        }

    }

}