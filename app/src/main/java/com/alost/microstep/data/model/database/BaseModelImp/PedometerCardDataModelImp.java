package com.alost.microstep.data.model.database.BaseModelImp;


import com.alost.microstep.data.model.database.IDataBaseModel.PedometerCardDataModel;
import com.alost.microstep.data.model.database.core.PedometerCardEntity;
import com.alost.microstep.data.model.database.core.PedometerCardEntityDao;

import java.util.List;


/**
 * Created by Alost
 * 计步数据库操作类
 */
public class PedometerCardDataModelImp implements PedometerCardDataModel {

    private PedometerCardEntityDao mPedometerDao;

    public PedometerCardDataModelImp(PedometerCardEntityDao dao) {
        mPedometerDao = dao;
    }

    @Override
    public PedometerCardEntity getPedometerCardEntity(String date) {

        List<PedometerCardEntity> list = mPedometerDao.queryBuilder().where(PedometerCardEntityDao.Properties.Date.eq(date)).build().list();
        if (list == null || list.size() <= 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public void updatePedometer(PedometerCardEntity pedometerCardEntity) {

        mPedometerDao.updateInTx(pedometerCardEntity);
    }

    @Override
    public Long insertPedometer(PedometerCardEntity pedometerCardEntity) {
        return mPedometerDao.insert(pedometerCardEntity);
    }

    @Override
    public void deletePedometer(PedometerCardEntity pedometerCardEntity) {
        mPedometerDao.delete(pedometerCardEntity);
    }

    @Override
    public void deletePedometer(String date) {
        mPedometerDao.getDatabase().delete(PedometerCardEntityDao.TABLENAME,
                PedometerCardEntityDao.Properties.Date.columnName + "=" + "'" + date + "'", null);
    }

    @Override
    public List<PedometerCardEntity> getPedometerCardEntities() {
        return mPedometerDao.loadAll();
    }
}
