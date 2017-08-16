package com.alost.microstep.presentation.module;


import com.alost.microstep.presentation.common.BaseApplication;

/**
 * Created by zoubo
 */
public class MicroStepApplication extends BaseApplication {


    public MicroStepApplication() {
        super();
    }


    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationModule.initSingleton().onCreateMainProcess();

    }


}
