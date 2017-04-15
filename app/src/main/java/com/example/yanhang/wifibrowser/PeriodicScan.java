package com.example.yanhang.wifibrowser;

/**
 * Created by yanhang on 4/15/17.
 */

import android.os.Handler;
import android.support.annotation.NonNull;

public class PeriodicScan implements Runnable{
    static final int DELAY_INITIAL = 1;
    int DELAY_INTERVAL = 3000;

    private final Scanner scanner_;
    private final Handler handler_;
    private boolean is_running_ = false;

    PeriodicScan(@NonNull Scanner scanner, @NonNull Handler handler){
        this.scanner_ = scanner;
        this.handler_ = handler;
    }

    void start(){
        nextRun(DELAY_INITIAL);
    }

    void stop(){
        handler_.removeCallbacks(this);
        is_running_ = false;
    }

    private void nextRun(int delay_initial){
        stop();
        handler_.postDelayed(this, delay_initial);
        is_running_ = true;
    }

    @Override
    public void run(){
        scanner_.performWiFiScan();
        nextRun(DELAY_INTERVAL);
    }

    public boolean isRunning(){
        return is_running_;
    }
}
