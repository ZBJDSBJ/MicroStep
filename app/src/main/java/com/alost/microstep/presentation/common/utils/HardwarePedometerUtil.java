package com.alost.microstep.presentation.common.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.Arrays;
import java.util.List;


/**
 * Created by zoubo on 16/2/16.
 * 计步器硬件传感器工具类
 */
public class HardwarePedometerUtil {
    private static List<String> mStepCounterWhiteList = Arrays.asList("mi4", "mi4lte", "nx507j", "nxt-al10");
    private static List<String> mScreenOffWhiteList = Arrays.asList("sm-g9009d");

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean areSensorsPresent(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER);
//        return !((SensorManager) context.getSystemService(Context.SENSOR_SERVICE)).getSensorList(Sensor.TYPE_STEP_COUNTER).isEmpty();
    }

    /**
     * step counter白名单列表
     * 这些手机 必须开启加速度传感器
     */
    public static boolean isThisDeviceInStepCounterWhiteList() {
//        Log.i("zou", "<HardwarePedometerUtil> isThisDeviceInStepCounterWhiteList 品牌版本 = " + Build.MODEL.toLowerCase().replace(" ", ""));

        return mStepCounterWhiteList.contains(Build.MODEL.toLowerCase().replace(" ", ""));
    }

    /**
     * step counter 黑屏时不计步的白名单列表
     * 这些手机 必须开启加速度传感器
     */
    public static boolean isThisDeviceInScreenOffWhiteList() {
//        Log.i("zou", "<HardwarePedometerUtil> isThisDeviceInStepCounterWhiteList 品牌版本 = " + Build.MODEL.toLowerCase().replace(" ", ""));

        return mScreenOffWhiteList.contains(Build.MODEL.toLowerCase().replace(" ", ""));
    }

    /**
     * 是否支持step counter记步传感器
     * 在Android4.4（KITKAT）系统API提供了两种硬件计步传感器的支持
     * 方式：1、有TYPE_STEP_COUNTER；2、版本为4.4(19)以上
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean supportsHardwareStepCounter(Context context) {
//        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
//        Log.i("zou", "sensor.getType() mStepCount = \n " + mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
//                + "\n mStepDetector = " + mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
//                + "\n mAccelerometer = " + mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));

        return (AndroidVersionUtil.isVersionKitKatOrHigher()) && (areSensorsPresent(context));
    }

    /**
     * 是否支持accelerometer传感器
     */
    public static boolean supportsHardwareAccelerometer(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
//        return !((SensorManager) context.getSystemService(Context.SENSOR_SERVICE)).getSensorList(Sensor.TYPE_ACCELEROMETER).isEmpty();
    }

    public static boolean supportsPedometer(Context context) {
        return supportsHardwareStepCounter(context) || supportsHardwareAccelerometer(context);
    }


    public static boolean supportOnlyAccelerometer(Context context) {
        return !supportsHardwareStepCounter(context) && supportsHardwareAccelerometer(context);
    }

}
