package com.alost.microstep.data.model.database.core;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.alost.microstep.data.model.database.core.PedometerDailyDetailEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PedometerDailyDetail".
*/
public class PedometerDailyDetailEntityDao extends AbstractDao<PedometerDailyDetailEntity, Void> {

    public static final String TABLENAME = "PedometerDailyDetail";

    /**
     * Properties of entity PedometerDailyDetailEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", false, "ID");
        public final static Property StepCount = new Property(1, Integer.class, "stepCount", false, "STEP_COUNT");
        public final static Property StartTime = new Property(2, Long.class, "startTime", false, "START_TIME");
        public final static Property EndTime = new Property(3, Long.class, "endTime", false, "END_TIME");
        public final static Property DelTime = new Property(4, Long.class, "delTime", false, "DEL_TIME");
    };


    public PedometerDailyDetailEntityDao(DaoConfig config) {
        super(config);
    }
    
    public PedometerDailyDetailEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PedometerDailyDetail\" (" + //
                "\"ID\" INTEGER," + // 0: id
                "\"STEP_COUNT\" INTEGER," + // 1: stepCount
                "\"START_TIME\" INTEGER," + // 2: startTime
                "\"END_TIME\" INTEGER," + // 3: endTime
                "\"DEL_TIME\" INTEGER);"); // 4: delTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PedometerDailyDetail\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PedometerDailyDetailEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer stepCount = entity.getStepCount();
        if (stepCount != null) {
            stmt.bindLong(2, stepCount);
        }
 
        Long startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindLong(3, startTime);
        }
 
        Long endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindLong(4, endTime);
        }
 
        Long delTime = entity.getDelTime();
        if (delTime != null) {
            stmt.bindLong(5, delTime);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public PedometerDailyDetailEntity readEntity(Cursor cursor, int offset) {
        PedometerDailyDetailEntity entity = new PedometerDailyDetailEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // stepCount
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // startTime
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // endTime
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4) // delTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PedometerDailyDetailEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setStepCount(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setStartTime(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setEndTime(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setDelTime(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(PedometerDailyDetailEntity entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(PedometerDailyDetailEntity entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}