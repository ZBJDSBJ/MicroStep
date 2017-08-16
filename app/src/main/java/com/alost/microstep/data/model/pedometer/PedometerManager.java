package com.alost.microstep.data.model.pedometer;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.alost.microstep.R;
import com.alost.microstep.data.model.database.IDataBaseModel.PedometerCardDataModel;
import com.alost.microstep.data.model.database.IDataBaseModel.PedometerDailyDetailDataModel;
import com.alost.microstep.data.model.database.core.PedometerCardEntity;
import com.alost.microstep.data.model.database.core.PedometerDailyDetailEntity;
import com.alost.microstep.presentation.common.utils.DateUtils;
import com.alost.microstep.presentation.common.utils.HardwarePedometerUtil;
import com.alost.microstep.presentation.module.ApplicationModule;
import com.alost.microstep.presentation.service.PedometerService;
import com.alost.microstep.presentation.view.broadcast.NotificationReceiver;


/**
 * Created by Alost on 17/1/5.
 * * 计步器管理类：1、通知管理   2、记步服务管理
 */

public class PedometerManager {

    private Context mContext;
    private Intent mService;
    private boolean mIsServiceRunning = false;

    private PedometerCardDataModel mPedometerCardDataModel = null;
    private PedometerCardEntity mTodayStepEntity = null;
    private String mInitTodayDate;
    private int mTargetStepCount;

    private PedometerDailyDetailDataModel mPedometerDailyDetailDataModel = null;


    /**
     * 计步器Notification管理
     */
    private NotificationManager mNotificationManager = null;
    private NotificationCompat.Builder mBuilder = null;
    private final static int mSNotifyId = 1100;  //通知ID


    private long mStartTime;
    private int mStartStep;
    private int mEndStep;

    public PedometerManager(Context context) {
        mContext = context;
        mService = new Intent(mContext, PedometerService.class);
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    /**
     * Service的检查与启动:1、登录并且卡片存在
     * 1、有TYPE_STEP_COUNTER； 2、版本为4.4(19)以上
     */
    public void startPedometerService() {
        if (HardwarePedometerUtil.supportsPedometer(mContext)) {
            mContext.startService(mService);
            mIsServiceRunning = true;
        }
    }

    /**
     * 1、停止记步服务；2、删除当天数据
     */
    public void stopPedometerService() {
        mIsServiceRunning = false;
        mContext.stopService(mService);
    }

    /**
     * Service的检查与启动:登录并且卡片存在
     */
    public void checkServiceStart() {

        if (HardwarePedometerUtil.supportsPedometer(mContext)) {

            ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if ("com.alost.alina.presentation.service.PedometerService".equals(service.service.getClassName())) {
                    mIsServiceRunning = true;
                }
            }

//            Log.i("zou", "<PedometerManager> checkServiceStart mIsServiceRunning = " + mIsServiceRunning);

            if (!mIsServiceRunning) {
                mContext.startService(mService);
            }
        }

    }

    /**
     * 服务启动后:初始化数据
     * !!!注意:这里的一切初始化都是在上面完成服务启动的条件下才会调用的
     */
    public void initData() {
        //初始化时的起始时间:记录日期变化的计步临时日期
        mInitTodayDate = DateUtils.getStringDateShort();

        mPedometerCardDataModel = ApplicationModule.getInstance().getDataModel().getPedometerCardDataModel();
        mTodayStepEntity = mPedometerCardDataModel.getPedometerCardEntity(mInitTodayDate);

        mPedometerDailyDetailDataModel = ApplicationModule.getInstance().getDataModel().getPedometerDailyDetailDataModel();
        mStartTime = System.currentTimeMillis();

        /***如果上一步的初始化(没有在今天的数据库插入一条计步数据),那么就初始化插入一条数据!!!***/
        if (mTodayStepEntity == null) {
            //从卡片列表拿初始化数据插入今天的列表
            mTodayStepEntity = new PedometerCardEntity(null, 0.0, mInitTodayDate, 0, 0.0, "计步", 5000, 0, 0f);
            mPedometerCardDataModel.insertPedometer(mTodayStepEntity);
        }

        mTargetStepCount = mTodayStepEntity.getTargetStepCount();
        mStartStep = mTodayStepEntity.getStepCount();
        mEndStep = mStartStep;
//        Log.i("zou", "<PedometerManager> initData mTodayStepEntity.getStepCount() = " + mTodayStepEntity.getStepCount());

        createNotification(mTodayStepEntity.getStepCount());

    }


    /**
     * 每分钟判断步数是否变化,是则存数据库
     */
    public void updateStepDailyDetailToFile() {
        mTodayStepEntity = mPedometerCardDataModel.getPedometerCardEntity(mInitTodayDate);
//        Log.i("zou", "<PedometerManager> updateStepDailyDetailToFile = " + mTodayStepEntity.getStepCount());
        mEndStep = mTodayStepEntity.getStepCount();

        if (mTodayStepEntity == null || !mIsServiceRunning) {
            return;
        }

        Long id = mTodayStepEntity.getId();
        long endTime = System.currentTimeMillis();
        long delTime = endTime - mStartTime;
        int delSteps = mEndStep - mStartStep;

//        Log.i("zou", "<PedometerManager> updateStepDailyDetailToFile \n id= " + id + " delTime = " + delTime + " mStartStep = " + mStartStep + " mEndStep = " + mEndStep);

        if (delSteps > 1) {
            PedometerDailyDetailEntity detailEntity = new PedometerDailyDetailEntity();
            detailEntity.setId(id);
            detailEntity.setStartTime(mStartTime);
            detailEntity.setEndTime(endTime);
            detailEntity.setDelTime(delTime);
            detailEntity.setStepCount(delSteps);
            mPedometerDailyDetailDataModel.insertDetail(id, detailEntity);

            mStartStep = mEndStep;
        }

        mStartTime = endTime;

    }

    /**
     * 初始化通知
     */
    private void createNotification(int steps) {

        //获取系统通知管理器实例
        mBuilder = new NotificationCompat.Builder(mContext);

        Intent intent = new Intent(mContext, NotificationReceiver.class);
        intent.putExtra(NotificationReceiver.KEY_NOTIFICATION_ENTRANCE, NotificationReceiver.VALUE_NOTIFICATION_PEDOMETER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("今天你走了" + steps + "步")
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setShowWhen(false)
                .setOngoing(true);  //设为true,notification将无法通过左右滑动的方式清除 可用于添加常驻通知，必须调用cancle方法来清除

        Notification notification = mBuilder.build();
        notification.icon = R.mipmap.ic_launcher;
        //在通知栏上点击此通知后自动清除此通知
        notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR; //FLAG_ONGOING_EVENT 在顶部常驻，可以调用下面的清除方法去除  FLAG_AUTO_CANCEL  点击和清理可以去调

        if (mNotificationManager != null) {
            mNotificationManager.notify(mSNotifyId, notification);
        }
    }

    /**
     * 更新通知栏
     */
    public void updateNotification(int steps) {

        mBuilder.setContentTitle("今天你走了" + steps + "步");
        mNotificationManager.notify(mSNotifyId, mBuilder.build());
    }


    /***
     * 系统时间变化监听广播,当发生天数改变时，插入一条新数据到数据库
     */
    public void updateByDateChanged() {
        //如果卡片不存在,  或者服务未启动,不更新
        if (!mIsServiceRunning) {
            return;
        }

        String systemDate = DateUtils.getStringDateShort();

        //1、第二天创建新卡片情况；2、手动修改日期情况
        if (mTodayStepEntity == null || !systemDate.equals(mTodayStepEntity.getDate()) || !mInitTodayDate.equals(systemDate)) {
            mInitTodayDate = systemDate;

            //系统日期变化后，如果数据库没有数据,则重新初始化
            ApplicationModule.getInstance().getPedometerRepository().initData();
            mPedometerCardDataModel = ApplicationModule.getInstance().getDataModel().getPedometerCardDataModel();
            mTodayStepEntity = mPedometerCardDataModel.getPedometerCardEntity(systemDate);
            mTargetStepCount = mTodayStepEntity.getTargetStepCount();
            mStartStep = mTodayStepEntity.getStepCount();
            mEndStep = mStartStep;

//            Log.i("zou", "<PedometerManager> updateByDateChanged systemDate = " + systemDate + "mTodayStepEntity" + mTodayStepEntity.getDate());

            //更新通知栏步数
            updateNotification(mPedometerCardDataModel.getPedometerCardEntity(systemDate).getStepCount());
        }
    }
}
