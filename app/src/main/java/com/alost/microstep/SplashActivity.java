package com.alost.microstep;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alost.microstep.data.model.preferences.PreferencesManager;
import com.alost.microstep.presentation.view.activity.BaseActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 欢迎页面
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.iv_background)
    ImageView mIvBackground;
    @BindView(R.id.iv_logo)
    ImageView mIvLogo;
    @BindView(R.id.view_ad)
    RelativeLayout mViewAd;
    @BindView(R.id.tv_count_down)
    TextView mTvCountDown;
    @BindView(R.id.activity_splash)
    RelativeLayout mActivitySplash;
    private Context mContext;
    private MyCountDownTimer mCountDownTimer;
    private PreferencesManager mPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        mContext = this;

        initView();

    }

    private void initView(){
        Glide.with(mContext).load(R.drawable.girl)
                .animate(animator).into(mIvBackground);

        mTvCountDown.setVisibility(View.VISIBLE);
        if (mCountDownTimer == null) {
            mCountDownTimer = new MyCountDownTimer(4000, 1000);
        }
        mCountDownTimer.start();
    }


    ViewPropertyAnimation.Animator animator = new ViewPropertyAnimation.Animator() {
        @Override
        public void animate(View view) {
            view.setAlpha(0f);
            ObjectAnimator objAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            objAnimator.setDuration(1200);
            objAnimator.start();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        animator = null;

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }


    @OnClick(R.id.tv_count_down)
    public void onClick() {
        MainActivity.newInstance(mContext);
        finish();
    }

    /**
     * 倒计时
     */
    private class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTvCountDown.setText("跳过 " + millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            mTvCountDown.setText("跳过 " + 0 + "s");

            MainActivity.newInstance(mContext);
            finish();
        }
    }


}
