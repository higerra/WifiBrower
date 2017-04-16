package com.example.yanhang.wifibrowser;

/**
 * Created by yanhang on 4/15/17.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PeriodicScan implements Runnable{
    private int scan_interval_ = 3000;

    private final MainActivity parent_;
    private final Runnable receive_callback_;

    private Handler handler_ = new Handler();
    private AtomicBoolean is_running_ = new AtomicBoolean(false);

    private WifiManager wifi_manager_;
    ArrayList<ArrayList<String> > scan_results_ = new ArrayList<>();
    BroadcastReceiver scan_receiver_ = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!is_running_.get()){
                return;
            }
            List<ScanResult> results = wifi_manager_.getScanResults();
            ArrayList<String> current_record = new ArrayList<>();
            for(ScanResult res: results){
                String str = String.valueOf(res.timestamp) + '\t' +
                        res.SSID + '\t' + res.BSSID + '\t' + String.valueOf(res.level);
                current_record.add(str);
            }
            scan_results_.add(current_record);
            if(receive_callback_ != null){
                parent_.runOnUiThread(receive_callback_);
            }
        }
    };

    PeriodicScan(@NonNull MainActivity parent, Runnable receive_callback, int interval){
        this.parent_ = parent;
        this.receive_callback_ = receive_callback;
        this.scan_interval_ = interval;

        wifi_manager_ = (WifiManager)parent.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    PeriodicScan(@NonNull MainActivity parent, Runnable receive_callback){
        this(parent, receive_callback, 3000);
    }

    PeriodicScan(@NonNull MainActivity parent){
        this(parent, null, 3000);
    }

    BroadcastReceiver getBroadcastReceiver(){
        return scan_receiver_;
    }

    WifiManager getWifiManager(){
        return wifi_manager_;
    }

    public int getRecordCount(){
        return scan_results_.size();
    }

    public ArrayList<String> getLatestScanResult(){
        if(scan_results_.isEmpty()){
            return null;
        }
        return scan_results_.get(scan_results_.size() - 1);
    }

    public void saveResultToFile(String path){
        terminate();

    }

    public void terminate(){
        handler_.removeCallbacks(null);
        is_running_.set(false);
    }

    public void start(){
        is_running_.set(true);
        run();
    }
    @Override
    public void run(){
        if(!wifi_manager_.isWifiEnabled()){
            wifi_manager_.setWifiEnabled(true);
        }
        wifi_manager_.startScan();
        if(is_running_.get()) {
            handler_.postDelayed(this, scan_interval_);
        }
    }

    public boolean isRunning(){
        return is_running_.get();
    }
}
