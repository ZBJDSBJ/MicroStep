/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alost.microstep.presentation.module;

import android.content.Context;

import com.alost.microstep.data.model.database.BDataBaseModel;
import com.alost.microstep.data.model.pedometer.IRxPedometerRepositoryIml;
import com.alost.microstep.data.model.pedometer.PedometerManager;
import com.alost.microstep.presentation.common.BaseApplication;
import com.alost.microstep.presentation.service.GlobalService;


public class ApplicationModule {

    private final Context mContext;

    private volatile static ApplicationModule sInstance = null;

    private BDataBaseModel mDatabaseModel;
    private PedometerManager mPedometerManager;// 计步器管理类:通知和定时器等
    private IRxPedometerRepositoryIml mPedometerRepository;

    /**
     * 初始化单例,在程序启动时调用<br>
     */
    public static ApplicationModule initSingleton() {
        if (sInstance == null) {
            synchronized (ApplicationModule.class) {
                if (sInstance == null) {
                    sInstance = new ApplicationModule();
                }
            }
        }

        return sInstance;
    }

    private ApplicationModule() {
        mContext = BaseApplication.getAppContext();
    }


    /**
     * 初始化的统一操作
     */
    void onCreateMainProcess() {

        mDatabaseModel = new BDataBaseModel(mContext);
        mPedometerManager = new PedometerManager(mContext);
        mPedometerRepository = new IRxPedometerRepositoryIml();

        //开启计步服务
        ApplicationModule.getInstance().getPedometerManager().startPedometerService();
        GlobalService.checkServiceStart(mContext);

    }


    public static ApplicationModule getInstance() {
        return sInstance;
    }

    public BDataBaseModel getDataModel() {
        return mDatabaseModel;
    }


    public PedometerManager getPedometerManager() {
        return mPedometerManager;
    }

    public IRxPedometerRepositoryIml getPedometerRepository() {
        return mPedometerRepository;
    }

}
