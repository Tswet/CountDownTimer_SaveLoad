package com.gmail.mtswetkov.countdowntimer;


import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public abstract class MyCountDownTimer {

    public void setmMillisInFuture(int mMillisInFuture) {
        this.mMillisInFuture = mMillisInFuture;
    }

    /**
     * Millis since epoch when alarm should stop.
     */
    private int mMillisInFuture;

    /**
     * The interval in millis that the user receives callbacks
     */
    private final int mCountdownInterval;

    private int mStopTimeInFuture;

    /**
     * boolean representing if the timer was cancelled
     */
    private boolean mCancelled = false;

    /**
     * @param millisInFuture The number of millis in the future from the call
     *   to {@link #start()} until the countdown is done and {@link #onFinish()}
     *   is called.
     * @param countDownInterval The interval aint the way to receive
     *   {@link #onTick(int)} callbacks.
     */
    public MyCountDownTimer(int millisInFuture, int countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }

    /**
     * Cancel the countdown.
     */
    public synchronized final void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }

    /**
     * Start the countdown.
     */
    public synchronized  MyCountDownTimer start() {
        mCancelled = false;
        if (mMillisInFuture <= 0) {
            onFinish();
            return this;
        }
        mStopTimeInFuture =  (int)SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }


    /**
     * Callback fired on regular interval.
     * @param millisUntilFinished The amount of time until finished.
     */
    public abstract void onTick(int millisUntilFinished);

    /**
     * Callback fired when the time is up.
     */
    public abstract void onFinish();


    private static final int MSG = 1;


    // handles counting down
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (MyCountDownTimer.this) {
                if (mCancelled) {
                    return;
                }

                 int millisLeft = (int) (mStopTimeInFuture - (int)SystemClock.elapsedRealtime());

                if (millisLeft <= 0) {
                    //sendMessageDelayed(obtainMessage(MSG), 0);
                    onFinish();
                } else {
                    int lastTickStart = (int)SystemClock.elapsedRealtime();
                    onTick(millisLeft);

                    // take into account user's onTick taking time to execute
                    int lastTickDuration = (int)SystemClock.elapsedRealtime() - lastTickStart;
                    int delay;

                    if (millisLeft < mCountdownInterval) {
                        // just delay until done
                        delay = millisLeft - lastTickDuration;

                        // special case: user's onTick took more than interval to
                        // complete, trigger onFinish without delay
                        if (delay < 0) delay = 0;
                    } else {
                        delay = mCountdownInterval - lastTickDuration;

                        // special case: user's onTick took more than interval to
                        // complete, skip to next interval
                        while (delay < 0) delay += mCountdownInterval;
                    }

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };
}

