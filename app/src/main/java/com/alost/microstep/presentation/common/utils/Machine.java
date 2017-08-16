package com.alost.microstep.presentation.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * @zoubo
 */
// CHECKSTYLE:OFF
public class Machine {
    private static boolean sCheckTablet = false;
    private static boolean sIsTablet = false;


    // 判断当前设备是否为平板
    private static boolean isPad() {
        if (DrawUtil.sDensity >= 1.5 || DrawUtil.sDensity <= 0) {
            return false;
        }
        if (DrawUtil.sWidthPixels < DrawUtil.sHeightPixels) {
            if (DrawUtil.sWidthPixels > 480 && DrawUtil.sHeightPixels > 800) {
                return true;
            }
        } else {
            if (DrawUtil.sWidthPixels > 800 && DrawUtil.sHeightPixels > 480) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTablet(Context context) {
        if (sCheckTablet == true) {
            return sIsTablet;
        }
        sCheckTablet = true;
        sIsTablet = isPad();
        return sIsTablet;
    }


    public static String getAndroidId(Context context) {
        String androidId = null;
        if (context != null) {
            androidId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        return androidId;
    }

    public static String getVersionCode(Context context) {
        String versionCode = null;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionCode = pInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * <br>功能简述:获取versionCode
     */
    public static String getVersionName(Context context) {
        String versionCode = null;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionCode = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getLanguage(Context context) {
        String language = context.getResources().getConfiguration().locale.getLanguage();
        return language;
    }

    public static String getIMSI(Context context) {
        String imsi = null;
        try {
            if (context != null) {
                // 从系统服务上获取了当前网络的MCC(移动国家号)，进而确定所处的国家和地区
                TelephonyManager manager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                imsi = manager.getSubscriberId();
                imsi = (imsi == null) ? "000" : imsi;
            }
        } catch (Throwable e) {
        }

        return imsi;
    }

    public static String getIMEI(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                    .getDeviceId();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String getPhoneNumber(Context context) {
        String phone = null;
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            phone = tm.getLine1Number();//手机号码
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return phone;
    }


}
