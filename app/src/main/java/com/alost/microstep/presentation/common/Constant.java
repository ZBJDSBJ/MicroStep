package com.alost.microstep.presentation.common;

import android.os.Environment;

import com.alost.microstep.R;
import com.alost.microstep.presentation.common.utils.DateUtil;
import com.alost.microstep.presentation.common.utils.DateUtils;

import java.io.File;

/**
 * Created by Alost on 17/3/10.
 * 一些常量
 */
public class Constant {

    public static final int PULL_UP_LOAD_MORE = 0;  //上拉加载更多...
    public static final int LOADING_MORE = 1;  //正在加载更多数据...
    public static final int NO_MORE = 2;   //没有更多数据
    public static final int LOADING_FINISHED = 3;   //加载完

    //广告
    public static final String BAIDU_SDK_APP_KEY = "E7lcrF1PwCFw5loyvYIjG9tRAXs6i3L0";
    public static final String BAIDU_SDK_SPLASH_AD_ID = "r5H0sMlVlX0U3dwkHPBokwjS";
    public static final String BAIDU_SDK_BANNER_AD_ID = "Le7rH3DX5RP9FSWWvRkqfHgS";


    public static final class Path {
        public final static String SDCARD = Environment.getExternalStorageDirectory().getPath();
        public static String sROOT_DIR = SDCARD + File.separator + BaseApplication.getAppContext().getResources().getString(R.string.app_name_en);
        public static String IMAGE_YESTERDAY_FILE_PATH = Constant.Path.sROOT_DIR + File.separator + DateUtil.getDateChange(DateUtils.getStringDateShort(), -1) + ".jpg";
        public static String IMAGE_TODAY_FILE_PATH = Constant.Path.sROOT_DIR + File.separator + DateUtils.getStringDateShort() + ".jpg";

    }

    public static final class Preferences {
        public static final String KEY_LAST_SHOW_AD_TIME = "key_last_show_ad_time";

        public static final String KEY_IS_SHOW_GIRL_KEY = "key_is_show_girl_key";
        public static final String KEY_IS_SHOW_WELFARE_KEY = "key_is_show_welfare_key";
        public static final String KEY_WELFARE_TYPE_KEY = "key_welfare_type_key";


    }
}
