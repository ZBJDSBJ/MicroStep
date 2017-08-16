package com.alost.microstep.data.model.pedometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.CountDownTimer;
import android.util.Log;

import com.alost.microstep.data.model.database.IDataBaseModel.PedometerCardDataModel;
import com.alost.microstep.data.model.database.core.PedometerCardEntity;
import com.alost.microstep.data.model.preferences.IPreferencesIds;
import com.alost.microstep.data.model.preferences.PreferencesManager;
import com.alost.microstep.data.repository.IRxPedometerRepository;
import com.alost.microstep.presentation.common.BaseApplication;
import com.alost.microstep.presentation.common.RxBus;
import com.alost.microstep.presentation.common.utils.DateUtils;
import com.alost.microstep.presentation.module.ApplicationModule;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;


/**
 * @author ALost
 *         计步器
 */
public class IRxPedometerRepositoryIml implements SensorEventListener, IRxPedometerRepository {

    private float mStepCounterValue = 0f;   //协处理器的值
    private float FIRST_STEP_COUNT = 0;    //每天打开软件记录的初始步数，用于带count传感器算法的方式
    private int CURRENT_STEP = 0;        //当天的记步数总数

    private PedometerCardDataModel mPedometerCardDataModel;
    private PedometerCardEntity mTodayStepEntity;
    private int TODAY_ENTITY_STEPS = 0;

    private Flowable<PedometerCardEntity> mFlowable;

    public IRxPedometerRepositoryIml() {
    }

    /**
     *  初始化传感器相关数据
     */
    public void initData() {

        /*******初始化步数,传感器灵敏度等：
         * 1、如果当天有数据，则读取数据库中的数据，否则创建数据表
         * *******/
        String systemDate = DateUtils.getStringDateShort();
        mPedometerCardDataModel = ApplicationModule.getInstance().getDataModel().getPedometerCardDataModel();
        mTodayStepEntity = mPedometerCardDataModel.getPedometerCardEntity(systemDate);


        FIRST_STEP_COUNT = 0;
        CURRENT_STEP = 0;
        if (mTodayStepEntity == null) {
            TODAY_ENTITY_STEPS = 0;
            mStepCounterValue = 0f;

            PreferencesManager mPreferencesManager = PreferencesManager.getDefaultSharedPreference(BaseApplication.getAppContext());
            int goalNum = mPreferencesManager.getInt(IPreferencesIds.PEDOMETER_DAILY_GOAL, 5000);

            mTodayStepEntity = new PedometerCardEntity(null, 0.0, systemDate, 0, 0.0, "计步", goalNum, 0, mStepCounterValue);
            mPedometerCardDataModel.insertPedometer(mTodayStepEntity);
        } else {

            TODAY_ENTITY_STEPS = mTodayStepEntity.getStepCount();
            if (mTodayStepEntity.getStepCounterValue() != null) {
                mStepCounterValue = mTodayStepEntity.getStepCounterValue();
            }
        }

        mFlowable = Flowable.just(mTodayStepEntity).subscribeOn(Schedulers.io());

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor == null) {
            return;
        }

        //同步锁,防止并发
        synchronized (this) {

            if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
//                Log.i("zou", "<IRxPedometerRepositoryIml> onSensorChanged TYPE_STEP_COUNTER event.values[0]= " + event.values[0]
//                        + " mStepCounterValue= " + mStepCounterValue);

                // Step Counter，对于长时间计步要准确很多,读取开机的传感器步数
                if (FIRST_STEP_COUNT == 0) {  //记录一个初始值
                    FIRST_STEP_COUNT = event.values[0];

                    /***优化算法,如果存的有值了,并且新获取的传感器的值大于存的值***/
                    int mDelCounterStep;
                    if (mStepCounterValue > 1 && FIRST_STEP_COUNT > mStepCounterValue) {
                        if (mStepCounterValue > FIRST_STEP_COUNT) {
                            mStepCounterValue = FIRST_STEP_COUNT;  //开启重启情况
                        }
                        mDelCounterStep = (int) (FIRST_STEP_COUNT - mStepCounterValue);
//                        FileUtil.writeFileSdcard(Constant.Path.ACCOUNT_DIR + "pedometerShow.txt", " mDelCounterStep: " + String.valueOf(mDelCounterStep));

                        if (mDelCounterStep <= 0) {
                            return;
                        }

                        CURRENT_STEP = mDelCounterStep;
                        mStepCounterValue = FIRST_STEP_COUNT;
                        showSteps(CURRENT_STEP, false, true);
                        mPedometerCardDataModel.updatePedometer(mTodayStepEntity);
                    }

                } else {

                    CURRENT_STEP = (int) (event.values[0] - FIRST_STEP_COUNT);
//                    Log.i("zou", "<IRxPedometerRepositoryIml> onSensorChanged TYPE_STEP_COUNTER event.values[0]= " + event.values[0]
//                            + " CURRENT_STEP = " + CURRENT_STEP);

                    FIRST_STEP_COUNT = event.values[0];
                    mStepCounterValue = FIRST_STEP_COUNT;

                    showSteps(CURRENT_STEP, false, false);

                }

            } else if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                calculateStep(event);

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //存放三轴数据
    private final int valueNum = 4;
    //用于存放计算阈值的波峰波谷差值
    private float[] tempValue = new float[valueNum];
    private int tempCount = 0;
    //是否上升的标志位
    private boolean isDirectionUp = false;
    //持续上升次数
    private int continueUpCount = 0;
    //上一点的持续上升的次数，为了记录波峰的上升次数
    private int continueUpFormerCount = 0;
    //上一点的状态，上升还是下降
    private boolean lastStatus = false;
    //波峰值
    private float peakOfWave = 0;
    //波谷值
    private float valleyOfWave = 0;
    //此次波峰的时间
    private long timeOfThisPeak = 0;
    //上次波峰的时间
    private long timeOfLastPeak = 0;
    //当前的时间
    private long timeOfNow = 0;
    //当前传感器的值
    private float gravityNew = 0;
    //上次传感器的值
    private float gravityOld = 0;
    //动态阈值需要动态的数据，这个值用于这些动态数据的阈值
    private final float initialValue = (float) 1.3;
    //初始阈值
    private float ThreadValue = (float) 2.0;

    private final String TAG = "StepDcretor";

    /**
     * 0-准备计时   1-计时中  2-准备为正常计步计时  3-正常计步中
     */
    private int CountTimeState = 0;
    public static int TEMP_STEP = 0;
    private int lastStep = -1;
    // 加速计的三个维度数值
    public static float[] gravity = new float[3];
    public static float[] linear_acceleration = new float[3];
    //用三个维度算出的平均值
    public static float average = 0;

    private Timer timer;
    // 倒计时3秒，3秒内不会显示计步，用于屏蔽细微波动
    private long duration = 3000;
    private TimeCount time;

    private void calculateStep(SensorEvent event) {
        average = (float) Math.sqrt(Math.pow(event.values[0], 2)
                + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2));

        DetectorNewStep(average);
    }

    /*
     * 检测步子，并开始计步
     * 1.传入sersor中的数据
     * 2.如果检测到了波峰，并且符合时间差以及阈值的条件，则判定为1步
     * 3.符合时间差条件，波峰波谷差值大于initialValue，则将该差值纳入阈值的计算中
     * */
    private void DetectorNewStep(float values) {
        if (gravityOld == 0) {
            gravityOld = values;
        } else {
            if (DetectorPeak(values, gravityOld)) {
                timeOfLastPeak = timeOfThisPeak;
                timeOfNow = System.currentTimeMillis();
                if (timeOfNow - timeOfLastPeak >= 200
                        && (peakOfWave - valleyOfWave >= ThreadValue) && timeOfNow - timeOfLastPeak <= 2000) {
                    timeOfThisPeak = timeOfNow;
                    //更新界面的处理，不涉及到算法
                    CURRENT_STEP++;
                    showSteps(CURRENT_STEP, true, false);
//                    preStep();
                }
                if (timeOfNow - timeOfLastPeak >= 200
                        && (peakOfWave - valleyOfWave >= initialValue)) {
                    timeOfThisPeak = timeOfNow;
                    ThreadValue = Peak_Valley_Thread(peakOfWave - valleyOfWave);
                }
//                Log.i("zou", "<DetectorNewStep> peakOfWave = " + peakOfWave + " valleyOfWave = " + valleyOfWave + " ThreadValue = " + ThreadValue);

            }
        }
        gravityOld = values;
    }


    /*
     * 检测波峰
     * 以下四个条件判断为波峰：
     * 1.目前点为下降的趋势：isDirectionUp为false
     * 2.之前的点为上升的趋势：lastStatus为true
     * 3.到波峰为止，持续上升大于等于2次
     * 4.波峰值大于1.2g,小于2g
     * 记录波谷值
     * 1.观察波形图，可以发现在出现步子的地方，波谷的下一个就是波峰，有比较明显的特征以及差值
     * 2.所以要记录每次的波谷值，为了和下次的波峰做对比
     * */
    private boolean DetectorPeak(float newValue, float oldValue) {
        lastStatus = isDirectionUp;
        if (newValue >= oldValue) {
            isDirectionUp = true;
            continueUpCount++;
        } else {
            continueUpFormerCount = continueUpCount;//记住上升次数，如果下降则清零
            continueUpCount = 0;
            isDirectionUp = false;
        }

        if (oldValue > 11.76) {
//            Log.i("zou", "<DetectorNewStep> newValue = " + newValue + " oldValue = " + oldValue +
//                    "lastStatus = " + lastStatus + "isDirectionUp = " + isDirectionUp);
        }

        if (!isDirectionUp && lastStatus
                && (continueUpFormerCount >= 2 && (oldValue >= 11.76 && oldValue < 25.6))) {
            peakOfWave = oldValue;//算出波峰值
            return true;
        } else if (!lastStatus && isDirectionUp) {
            valleyOfWave = oldValue;
            return false;
        } else {
            return false;
        }
    }

    /*
     * 阈值的计算
     * 1.通过波峰波谷的差值计算阈值
     * 2.记录4个值，存入tempValue[]数组中
     * 3.在将数组传入函数averageValue中计算阈值
     * */
    private float Peak_Valley_Thread(float value) {
        float tempThread = ThreadValue;
        if (tempCount < valueNum) {
            tempValue[tempCount] = value;
            tempCount++;
        } else {
            tempThread = averageValue(tempValue, valueNum);
            for (int i = 1; i < valueNum; i++) {
                tempValue[i - 1] = tempValue[i];
            }
            tempValue[valueNum - 1] = value;
        }
        return tempThread;

    }

    /*
     * 梯度化阈值
     * 1.计算数组的均值
     * 2.通过均值将阈值梯度化在一个范围里
     * */
    private float averageValue(float value[], int n) {
        float ave = 0;
        for (int i = 0; i < n; i++) {
            ave += value[i];
        }
        ave = ave / valueNum;
        if (ave >= 8)
            ave = (float) 4.3;
        else if (ave >= 7 && ave < 8)
            ave = (float) 3.3;
        else if (ave >= 4 && ave < 7)
            ave = (float) 2.3;
        else if (ave >= 3 && ave < 4)
            ave = (float) 2.0;
        else {
            ave = (float) 1.3;
        }

        return ave;
    }


    private class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 如果计时器正常结束，则开始计步
            time.cancel();
            CURRENT_STEP += TEMP_STEP;
            lastStep = -1;
            Log.v(TAG, "计时正常结束");

            timer = new Timer(true);
            TimerTask task = new TimerTask() {
                public void run() {
                    if (lastStep == CURRENT_STEP) {
                        timer.cancel();
                        CountTimeState = 0;
                        lastStep = -1;
                        TEMP_STEP = 0;
                        Log.v(TAG, "停止计步：" + CURRENT_STEP);
                    } else {
                        lastStep = CURRENT_STEP;
                    }
                }
            };
            timer.schedule(task, 0, 3000);  //每3秒钟判断步数是否变化
            CountTimeState = 3;//正常记步
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (lastStep == TEMP_STEP) {
                Log.v(TAG, "onTick 计时停止");
                time.cancel();
                CountTimeState = 0;
                lastStep = -1;
                TEMP_STEP = 0;
            } else {
                lastStep = TEMP_STEP;
            }
        }

    }

    /**
     * 获取行走步数
     *
     * @param isAcc     如果事加速度计步
     * @param isRestart 如果是死掉重启
     */
    private PedometerEvent event = new PedometerEvent();

    private void showSteps(int steps, boolean isAcc, boolean isRestart) {
        int total;
        if (isAcc) {
            total = TODAY_ENTITY_STEPS + steps;

        } else {

            if (!isRestart && steps > 5000 || steps < 0) {
                return;
            }
            total = steps + TODAY_ENTITY_STEPS;
            TODAY_ENTITY_STEPS = total;
        }


//        Log.i("zou", "<IRxPedometerRepositoryIml> showSteps TODAY_ENTITY_STEPS= " + TODAY_ENTITY_STEPS + " total = " + total);

        /**更新步数通知消息*/
        ApplicationModule.getInstance().getPedometerManager().updateNotification(total);
        mTodayStepEntity.setStepCount(total);
        mTodayStepEntity.setStepCounterValue(mStepCounterValue);

        /** 正常写计步数据到数据库*/
        mPedometerCardDataModel.updatePedometer(mTodayStepEntity);
        event.mIsUpdate = true;
        RxBus.getDefault().post(event);
    }


    @Override
    public Flowable<PedometerCardEntity> getPedometerStep() {
        return mFlowable;
    }

    public void onDestroy() {
        FIRST_STEP_COUNT = 0;
        CURRENT_STEP = 0;
        TODAY_ENTITY_STEPS = 0;
        mStepCounterValue = 0;
        mTodayStepEntity = null;
    }

}
