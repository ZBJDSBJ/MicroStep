package com.alost.microstep.data.model.database.BaseModelImp;


import com.alost.microstep.data.model.database.IDataBaseModel.PedometerDailyDetailDataModel;
import com.alost.microstep.data.model.database.core.PedometerDailyDetailEntity;
import com.alost.microstep.data.model.database.core.PedometerDailyDetailEntityDao;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Alost on 16/11/3.
 * * 计步每日详情数据库操作实现类
 */

public class PedometerDailyDetailDataModelIml implements PedometerDailyDetailDataModel {

    private PedometerDailyDetailEntityDao mDao;
//    private HashMap<Long, List<PedometerDailyDetailEntity>> mCachePedometerDailyDetails;


    public PedometerDailyDetailDataModelIml(PedometerDailyDetailEntityDao dao) {
        mDao = dao;
//        mCachePedometerDailyDetails = new HashMap<>();
    }

    @Override
    public Long insertDetail(Long id, PedometerDailyDetailEntity detailEntity) {
        return mDao.insert(detailEntity);
    }


    @Override
    public void insertList(Long id, List<PedometerDailyDetailEntity> entityList) {
        if (entityList != null && entityList.size() > 0) {
//            mCachePedometerDailyDetails.clear();
//            mCachePedometerDailyDetails.put(id, entityList);

            for (PedometerDailyDetailEntity dailyDetailEntity : entityList) {
                dailyDetailEntity.setId(id);
            }

            delete(id);
            mDao.insertInTx(entityList);
        }
    }

    @Override
    public List<PedometerDailyDetailEntity> getPedometerDailyDetailEntities(Long id) {
        List list = mDao.queryBuilder().where(PedometerDailyDetailEntityDao.Properties.Id.eq(id)).list();
        if (list.size() <= 0) {
            return null;
        }
        List<PedometerDailyDetailEntity> listCache = (ArrayList<PedometerDailyDetailEntity>) list;

        return listCache;
    }

    @Override
    public void cleanCache() {
//        mCachePedometerDailyDetails.clear();
    }

    @Override
    public void delete(Long resultId) {
        mDao.getDatabase().delete(PedometerDailyDetailEntityDao.TABLENAME, PedometerDailyDetailEntityDao.Properties.Id.columnName + "=" + resultId, null);

    }
}
