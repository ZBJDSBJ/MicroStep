package com.alost.microstep;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.alost.microstep.data.model.database.core.PedometerCardEntity;
import com.alost.microstep.presentation.presenter.PedometerPresenter;
import com.alost.microstep.presentation.view.activity.BaseActivity;
import com.alost.microstep.presentation.view.component.PedometerProgressView;
import com.alost.microstep.presentation.view.iview.IPedometerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements IPedometerView {

    @BindView(R.id.circle1)
    ImageView mCircle1;
    @BindView(R.id.circle2)
    ImageView mCircle2;
    @BindView(R.id.roundProgressBar)
    PedometerProgressView mPedometerProgressView;


    private PedometerPresenter mPedometerPresenter;
    private boolean mIsFirst = true;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mPedometerPresenter = new PedometerPresenter(this);
        mPedometerPresenter.resume();

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.zoom_animation);

        mCircle1.startAnimation(animation);
        mCircle1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.zoom_animation);

                mCircle2.startAnimation(animation);
            }
        }, 300);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPedometerPresenter = null;
    }

    @Override
    public void onReaderPedometer(PedometerCardEntity cardEntity) {
        if (cardEntity == null) {
            return;
        }

        updateTitleView(cardEntity.getStepCount(), cardEntity.getTargetStepCount());
    }

    private void updateTitleView(int step, int targetStep) {
        int progress = (int) (step * 100 / (float) targetStep);
        float end = progress / 100.0f;

        if (mIsFirst) {
            mIsFirst = false;
            mPedometerProgressView.setProgress(step, targetStep, 0, end > 1 ? 1 : end, true);
            mPedometerProgressView.initData("今日步数", "目标" + targetStep + "步");
        } else {
            mPedometerProgressView.updateData(step, end > 1 ? 1 : end);
        }
    }
}
