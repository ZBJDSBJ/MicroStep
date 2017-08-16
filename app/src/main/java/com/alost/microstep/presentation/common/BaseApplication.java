package com.alost.microstep.presentation.common;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.alost.microstep.presentation.common.utils.DrawUtil;


/**
 * application
 */
public class BaseApplication extends Application {

    /**
     * 异步线程，用于处理一般比较短暂的耗时操作，如数据库读写操作等<br>
     */
    protected static final HandlerThread SHORT_TASK_WORKER_THREAD = new HandlerThread(
            "Short-Task-Worker-Thread");

    static {
        SHORT_TASK_WORKER_THREAD.start();
    }

    protected final static Handler SHORT_TASK_HANDLER = new Handler(
            SHORT_TASK_WORKER_THREAD.getLooper());
    protected final static Handler MAIN_LOOPER_HANDLER = new Handler(
            Looper.getMainLooper());

    protected static BaseApplication sInstance;


    public BaseApplication() {
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DrawUtil.resetDensity(this);

        /**
         * 仅仅是缓存Application的Context，不耗时
         */

    }


    public static Context getAppContext() {
        return sInstance;
    }


    /**
     * 提交一个Runable到短时任务线程执行<br>
     * <p>
     * <strong>NOTE:</strong>
     * 只充许提交比较短暂的耗时操作，如数据库读写操作等，像网络请求这类可能耗时较长的<i>不能</i>提交，<br>
     * 以免占用线程影响其他的重要数据库操作。
     * </p>
     *
     * @param r
     * @see #postRunOnShortTaskThread(Runnable, long)
     * @see #removeFromShortTaskThread(Runnable)
     */
    public static void postRunOnShortTaskThread(Runnable r) {
        postRunnableByHandler(SHORT_TASK_HANDLER, r);
    }

    /**
     * 提交一个Runable到短时任务线程执行<br>
     * <p>
     * <strong>NOTE:</strong>
     * 只充许提交比较短暂的耗时操作，如数据库读写操作等，像网络请求这类可能耗时较长的<i>不能</i>提交，<br>
     * 以免占用线程影响其他的重要数据库操作。
     * </p>
     *
     * @param r
     * @param delayMillis 延迟指定的毫秒数执行.
     * @see #postRunOnShortTaskThread(Runnable)
     * @see #removeFromShortTaskThread(Runnable)
     */
    public static void postRunOnShortTaskThread(Runnable r, long delayMillis) {
        postRunnableByHandler(SHORT_TASK_HANDLER, r, delayMillis);
    }


    /**
     * 从短时任务线程移除一个先前post进去的Runable<b>
     *
     * @param r
     * @see #postRunOnShortTaskThread(Runnable)
     * @see #postRunOnShortTaskThread(Runnable, long)
     */
    public static void removeFromShortTaskThread(Runnable r) {
        removeRunnableFromHandler(SHORT_TASK_HANDLER, r);
    }

    /**
     * 提交一个Runable到UI线程执行<br>
     *
     * @param r
     * @see #removeFromUiThread(Runnable)
     */
    public static void postRunOnUiThread(Runnable r) {
        postRunnableByHandler(MAIN_LOOPER_HANDLER, r);
    }

    /**
     * 提交一个Runable到UI线程执行<br>
     *
     * @param r
     * @param delayMillis 延迟指定的毫秒数执行.
     * @see #postRunOnUiThread(Runnable)
     * @see #removeFromUiThread(Runnable)
     */
    public static void postRunOnUiThread(Runnable r, long delayMillis) {
        postRunnableByHandler(MAIN_LOOPER_HANDLER, r, delayMillis);
    }

    /**
     * 从UI线程移除一个先前post进去的Runable<b>
     *
     * @param r
     * @see #postRunOnUiThread(Runnable)
     */
    public static void removeFromUiThread(Runnable r) {
        removeRunnableFromHandler(MAIN_LOOPER_HANDLER, r);
    }

    /**
     * 是否运行在UI线程<br>
     *
     * @return
     */
    public static boolean isRunOnUiThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    private static void postRunnableByHandler(Handler handler, Runnable r) {
        handler.post(r);
    }

    private static void postRunnableByHandler(Handler handler, Runnable r,
                                              long delayMillis) {
        if (delayMillis <= 0) {
            postRunnableByHandler(handler, r);
        } else {
            handler.postDelayed(r, delayMillis);
        }
    }

    private static void removeRunnableFromHandler(Handler handler, Runnable r) {
        handler.removeCallbacks(r);
    }
}
