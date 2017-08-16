package com.alost.microstep.data.repository;


import com.alost.microstep.data.model.database.core.PedometerCardEntity;

import io.reactivex.Flowable;

/**
 * Created by Alost
 * 计步
 */
public interface IRxPedometerRepository extends IRepository {

    Flowable<PedometerCardEntity> getPedometerStep();

}
