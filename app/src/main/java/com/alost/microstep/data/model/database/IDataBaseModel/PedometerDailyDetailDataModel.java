package com.alost.microstep.data.model.database.IDataBaseModel;


import com.alost.microstep.data.model.database.core.PedometerDailyDetailEntity;

import java.util.List;

/**
 * Created by zoubo on 16/11/3.
 * 计步每日详情接口
 */
public interface PedometerDailyDetailDataModel {

    Long insertDetail(Long id, PedometerDailyDetailEntity detailEntity);

    void insertList(Long id, List<PedometerDailyDetailEntity> entityList);


    List<PedometerDailyDetailEntity> getPedometerDailyDetailEntities(Long id);

    void cleanCache();

    void delete(Long resultId);

}
