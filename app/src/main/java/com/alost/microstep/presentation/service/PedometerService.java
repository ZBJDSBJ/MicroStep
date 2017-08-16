package com.alost.microstep.presentation.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import com.alost.microstep.data.model.pedometer.IRxPedometerRepositoryIml;
import com.alost.microstep.presentation.common.utils.HardwarePedometerUtil;
import com.alost.microstep.presentation.module.ApplicationModule;
import com.alost.microstep.presentation.module.MicroStepApplication;


/**
 * @author Alost
 *         计步器服务，启动条件：
 *         1、有TYPE_STEP_COUNTER； 2、版本为4.4(19)以上
 */
public class PedometerService extends Service {
    private Context mContext;
    private SensorManager mSensorManager;  // 传感器服务
    private IRxPedometerRepositoryIml mPedometerRepositoryIml;  // 传感器监听对象

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = MicroStepApplication.getAppContext();

        //1、先初始化 插入今天的计步到数据库
        mPedometerRepositoryIml = ApplicationModule.getInstance().getPedometerRepository();
        mPedometerRepositoryIml.initData();

        //2、再初始化,从数据库读取今天计步数据
        ApplicationModule.getInstance().getPedometerManager().initData();

        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if (HardwarePedometerUtil.supportsHardwareStepCounter(mContext) && !HardwarePedometerUtil.isThisDeviceInScreenOffWhiteList()) {
            if (HardwarePedometerUtil.isThisDeviceInStepCounterWhiteList()) {
                mSensorManager.registerListener(new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {

                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {

                    }
                }, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
            }

            //
            mSensorManager.registerListener(mPedometerRepositoryIml, mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                    SensorManager.SENSOR_DELAY_UI);  //步行检测传感器

        } else if (HardwarePedometerUtil.supportsHardwareAccelerometer(mContext)) {
            mSensorManager.registerListener(mPedometerRepositoryIml, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_UI);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i("zou", "<PedometerService> onStartCommand");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.i("zou", "<PedometerService> onDestroy");

        if (mPedometerRepositoryIml != null) {
            mPedometerRepositoryIml.onDestroy();
            mSensorManager.unregisterListener(mPedometerRepositoryIml);
            mPedometerRepositoryIml = null;
        }

        mSensorManager = null;
        mContext = null;

    }

}
