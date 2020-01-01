package com.yasoka.eazyscreenrecord.video;

import android.os.Handler;
import java.util.TimerTask;

public abstract class MyTimerTask extends TimerTask {
    private Handler handler = new Handler();

    class C24971 implements Runnable {
        final MyTimerTask myTimerTask;

        C24971(MyTimerTask myTimerTask2) {
            this.myTimerTask = myTimerTask2;
        }

        public void run() {
            this.myTimerTask.performTask();
        }
    }

    public abstract void performTask();

    public void run() {
        this.handler.post(new C24971(this));
    }
}
