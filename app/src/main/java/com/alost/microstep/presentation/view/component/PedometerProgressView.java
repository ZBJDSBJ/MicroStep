package com.alost.microstep.presentation.view.component;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.alost.microstep.R;
import com.alost.microstep.presentation.common.utils.DrawUtil;


/**
 * Created by Alost on 16/11/7.
 */

public class PedometerProgressView extends View {

    private Paint mPaint;
    private ValueAnimator mAnimation;
    private Paint mTextPaint;

    /**
     * 圆环的颜色
     */
    private int roundColor = Color.RED;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor = Color.GREEN;

    /**
     * 字体的颜色
     */
    private int mTextColor = Color.WHITE;

    /**
     * 字体的大小
     */
    private float mFontSize = 60;


    /**
     * 字体的大小
     */
    private float mOtherFontSize = 15;

    /**
     * padding的大小
     */
    private int mPadding = 120;

    /**
     * 圆环的宽度
     */
    private float mRoundWidth = 10;

    /**
     * 圆点的半径
     */
    private float mPointRadius = 10;

    /**
     * 最大进度
     */
    private int max = 360;

    /**
     * 当前进度
     */
    private float mProgress = 0;

    /**
     * 进度开始的角度数
     */
    private int startAngle = -90;

    private float mProgressStart;
    private float mProgressEnd;
    private final int MAX_DURATION = 3000;
    private final int MIN_DURATION = 1000;

    private String mTopText = "Top";
    private String mCenterText = "0";
    private String mBottomText = "Bottom";

    public PedometerProgressView(Context context) {
        this(context, null);
    }

    public PedometerProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PedometerProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PedometerProgressView, defStyleAttr, 0);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.PedometerProgressView_ppvRoundColor:
                    roundColor = array.getColor(attr, Color.RED);
                    break;
                case R.styleable.PedometerProgressView_ppvTextColor:
                    mTextColor = array.getColor(attr, mTextColor);
                    break;
                case R.styleable.PedometerProgressView_ppvRoundProgressColor:
                    roundProgressColor = array.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.PedometerProgressView_ppvCircleWidth:
                    mRoundWidth = array.getDimension(attr, 10);
                    break;
                case R.styleable.PedometerProgressView_ppvRadiusWidth:
                    mPointRadius = array.getDimension(attr, 10);
                    break;
                case R.styleable.PedometerProgressView_ppvCenterTextSize:
                    mFontSize = array.getInt(attr, 10);
                    break;
                case R.styleable.PedometerProgressView_ppvOtherTextSize:
                    mOtherFontSize = array.getInt(attr, 10);
                    break;
                case R.styleable.PedometerProgressView_ppvMax:
                    max = array.getInt(attr, 360);
                    break;
                case R.styleable.PedometerProgressView_ppvStartAngle:
                    startAngle = array.getInt(attr, -90);
                    break;
            }
        }
        array.recycle();

        init();

    }

    private void init() {
        initPaint();

    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(roundColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mFontSize);
        mTextPaint.setColor(mTextColor);
    }

    /**
     * 适配不同分辨率的倍数
     *
     * @param scale
     */
    public void setScale(float scale) {
        mRoundWidth *= scale;
        mFontSize *= scale;
        mOtherFontSize *= scale;
        mPointRadius *= scale;
        mPadding *= scale;
    }

    public void initData(String topText, String bottomText) {

        mTopText = topText;
        mBottomText = bottomText;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        Log.i("zou", "<PedometerProgressView> onDraw getWidth = " + getWidth() + " mProgress = " + mProgress + "max = " + max);

        /**
         * 画最外层的大圆环
         */
        int centre = getWidth() / 2; //获取圆心的x坐标
        int radius = (int) (centre - mRoundWidth / 2 - mPointRadius / 2 - 2); //圆环的半径
        mPaint.setColor(roundColor); //设置圆环的颜色
        mPaint.setStyle(Paint.Style.STROKE); //设置空心
        mPaint.setStrokeWidth(mRoundWidth); //设置圆环的宽度
        canvas.drawCircle(centre, centre, radius, mPaint); //画出圆环

        /**
         * 画圆弧 ，画圆环的进度
         */
        //设置进度是实心还是空心
        mPaint.setStrokeWidth(mRoundWidth); //设置圆环的宽度
        mPaint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限

        if (mProgress != 0)
            canvas.drawArc(oval, startAngle, mProgress * max, false, mPaint);  //根据进度画圆弧

        drawPoint(centre, radius, canvas);


        /**
         * 画中心文字
         */
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mFontSize);
        float textSize = mTextPaint.measureText(mCenterText);
        canvas.drawText(mCenterText, centre - textSize / 2,
                centre + mTextPaint.getTextSize() / 2 - DrawUtil.dip2px(2), mTextPaint);

        /**
         * 画顶部文字
         */
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mOtherFontSize);
        float topTextSize = mTextPaint.measureText(mTopText);
        canvas.drawText(mTopText, centre - topTextSize / 2,
                centre - DrawUtil.dip2px(2) - mPadding, mTextPaint);

        /**
         * 画底部文字
         */
        float bottomTextSize = mTextPaint.measureText(mBottomText);
        canvas.drawText(mBottomText, centre - bottomTextSize / 2,
                centre + mTextPaint.getTextSize() / 2 + DrawUtil.dip2px(12) + mPadding, mTextPaint);

    }

    private void drawPoint(int centre, int radius, Canvas canvas) {
        mPaint.setStrokeWidth(mRoundWidth); //设置圆环的宽度
        mPaint.setColor(roundProgressColor);  //设置进度的颜色
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centre + radius * (float) Math.sin(mProgress * 2 * Math.PI),
                centre - radius * (float) Math.cos(mProgress * 2 * Math.PI), mPointRadius, mPaint);

    }


    public void updateData(int step, float progress) {
        mCenterText = String.valueOf(step);
        mProgress = progress;
        invalidate();
    }

    /**
     * 设置进度
     *
     * @param start  0 - 1
     * @param end    0 - 1
     * @param isAnim 是否开始动画
     * @return
     */
    private int mCurrentNum;
    private int mToatalNum;

    public long setProgress(int step, int target, float start, float end, boolean isAnim) {
        mCurrentNum = step;
        mToatalNum = target;
        mProgressStart = start;
        mProgressEnd = end;
        mProgress = mProgressStart;
        invalidate();

        if (isAnim) {
            return startAnim();
        } else {
            return 0;
        }
    }

    private long startAnim() {
        if (mProgressEnd == mProgressStart) {
            return 0;
        }

        if (mAnimation == null) {
            mAnimation = ValueAnimator.ofFloat(mProgressStart, mProgressEnd);
            mAnimation.setInterpolator(new DecelerateInterpolator());
            mAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mProgress = (float) animation.getAnimatedValue();
                    mCenterText = String.valueOf((int) (mToatalNum * mProgress));
                    invalidate();
                }
            });
            mAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgress = mProgressEnd;
                    mCenterText = String.valueOf(mCurrentNum);
                    invalidate();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            mAnimation.setFloatValues(mProgressStart, mProgressEnd);
        }

        long duration = (long) (MAX_DURATION * (mProgressEnd - mProgressStart));
        duration = Math.max(MIN_DURATION, duration);
        mAnimation.setDuration(duration);
        mAnimation.start();

        return duration;
    }


    public static float getTextHigh(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

}
