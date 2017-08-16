package com.alost.microstep.presentation.view.iview;


import com.alost.microstep.data.model.database.core.PedometerCardEntity;

/**
 * Created by Alost
 * 计步器view接口
 */
public interface IPedometerView {
    void onReaderPedometer(PedometerCardEntity cardEntity);

}
