package com.alost.microstep.data.model.database.IDataBaseModel;


import com.alost.microstep.data.model.database.core.PedometerCardEntity;

import java.util.List;


/**
 * Created by Alost
 */
public interface PedometerCardDataModel {

    PedometerCardEntity getPedometerCardEntity(String date);

    void updatePedometer(PedometerCardEntity pedometerCardEntity);

    Long insertPedometer(PedometerCardEntity pedometerCardEntity);

    void deletePedometer(PedometerCardEntity pedometerCardEntity);

    void deletePedometer(String date);

    List<PedometerCardEntity> getPedometerCardEntities();

}
