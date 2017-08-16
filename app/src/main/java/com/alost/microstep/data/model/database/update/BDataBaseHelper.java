package com.alost.microstep.data.model.database.update;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alost.microstep.data.model.database.core.DaoMaster;

import java.lang.reflect.Method;


/**
 * Created by Alost
 */
public class BDataBaseHelper extends DaoMaster.OpenHelper {


    private Context mContext;

    public BDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        //做一些初始化的数据插入
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteOldDBAndCreateNew(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onHandleUpgrade(db, oldVersion, newVersion);
    }

    public void deleteOldDBAndCreateNew(SQLiteDatabase db) {

        DaoMaster.dropAllTables(db, true);
        onCreate(db);
    }

    private void onHandleUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Method[] methods = getClass().getDeclaredMethods();
        while (oldVersion < newVersion) {
            Method upgrade = null;
            for (Method method : methods) {
                if (method.isAnnotationPresent(DatabaseVersion.class)) {
                    DatabaseVersion databaseVersion = method
                            .getAnnotation(DatabaseVersion.class);
                    if (oldVersion == databaseVersion.old()) {
                        upgrade = method;
                        break;
                    }
                }
            }

            if (null != upgrade) {
                try {
                    upgrade.invoke(this, db);
                    Log.d("DatabaseUpgrade", "upgrade oldVersion: " + oldVersion
                            + ", newVersion : " + newVersion);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("DatabaseUpgrade", "找不到升级数据库版本的方法, oldVersion: "
                        + oldVersion + ", newVersion : " + newVersion);
            }
            oldVersion++;
        }
    }

}
