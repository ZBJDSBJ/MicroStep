package com.alost.microstep.data.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.alost.microstep.data.model.database.BaseModelImp.PedometerCardDataModelImp;
import com.alost.microstep.data.model.database.BaseModelImp.PedometerDailyDetailDataModelIml;
import com.alost.microstep.data.model.database.IDataBaseModel.PedometerCardDataModel;
import com.alost.microstep.data.model.database.IDataBaseModel.PedometerDailyDetailDataModel;
import com.alost.microstep.data.model.database.core.DaoMaster;
import com.alost.microstep.data.model.database.core.DaoSession;
import com.alost.microstep.data.model.database.update.BDataBaseHelper;


/**
 * Created by Alost
 */
public class BDataBaseModel {

    private Context mContext;

    private DaoSession mDaoSession;
    private SQLiteDatabase mDb;
    private BDataBaseHelper mHelper;
    private DaoMaster mDaoMaster;


    private PedometerCardDataModel mPedometerCardDataModel;
    private PedometerDailyDetailDataModel mPedometerDailyDetailDataModel;

    public BDataBaseModel(Context context) {
        mContext = context;
        setupDatabase(context);
        setupDataModel();
    }

    //创建数据库
    private void setupDatabase(Context context) {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new BDataBaseHelper(context, DBConstants.DB_NAME, null);
        mDb = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();
    }

    private void setupDataModel() {
        mPedometerCardDataModel = new PedometerCardDataModelImp(mDaoSession.getPedometerCardEntityDao());
        mPedometerDailyDetailDataModel = new PedometerDailyDetailDataModelIml(mDaoSession.getPedometerDailyDetailEntityDao());
    }


    public PedometerCardDataModel getPedometerCardDataModel() {
        return mPedometerCardDataModel;
    }

    public PedometerDailyDetailDataModel getPedometerDailyDetailDataModel() {
        return mPedometerDailyDetailDataModel;
    }

}
