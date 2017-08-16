package com.alost.microstep.presentation.presenter;


import com.alost.microstep.data.model.database.core.PedometerCardEntity;
import com.alost.microstep.data.model.pedometer.PedometerEvent;
import com.alost.microstep.data.repository.IRxPedometerRepository;
import com.alost.microstep.presentation.common.RxBus;
import com.alost.microstep.presentation.module.ApplicationModule;
import com.alost.microstep.presentation.view.iview.IPedometerView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Alost
 * 计步presenter
 */
public class PedometerPresenter implements Presenter {

    private IPedometerView mView;
    private IRxPedometerRepository mIRxPedometerRepository;

    private Disposable mDisposable;

    public PedometerPresenter(IPedometerView view) {
        mView = view;
        mIRxPedometerRepository = ApplicationModule.getInstance().getPedometerRepository();
    }

    @Override
    public void resume() {
        //第一次初始化数据:包括退出应用后再进入的数据初始化
        updateLoadData();
        mDisposable = RxBus.getDefault().register(PedometerEvent.class, new Consumer<PedometerEvent>() {
            @Override
            public void accept(PedometerEvent event) throws Exception {
                if (event.mIsUpdate) {
                    updateLoadData();
                }
            }
        }, AndroidSchedulers.mainThread(), BackpressureStrategy.BUFFER);
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        RxBus.getDefault().unRegister(mDisposable);
    }


    /**
     * 更新某天的步数
     */
    private void updateLoadData() {
        mIRxPedometerRepository.getPedometerStep()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PedometerCardEntity>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(PedometerCardEntity cardEntity) {
                        if (mView != null) {
                            mView.onReaderPedometer(cardEntity);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
//                        Log.i("zou", "<PedometerPresenter> onComplete ");
                    }
                });
    }


}
