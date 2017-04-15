package com.example.yanhang.wifibrowser;

/**
 * Created by yanhang on 4/15/17.
 */

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.ArrayAdapter;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Scanner {
    private final WifiManager wifi_manager_;
    private final Handler handler_;
    private PeriodicScan periodic_scan_;

    public Scanner(@NonNull final WifiManager wifi_manager,
                   @NonNull Handler handler){
        this.wifi_manager_ = wifi_manager;
        this.handler_ = handler;
        this.periodic_scan_ = new PeriodicScan(this, handler_);
    }

    public void performWiFiScan(){
        try{
            if (!wifi_manager_.isWifiEnabled()){
                wifi_manager_.setWifiEnabled(true);
            }
            wifi_manager_.startScan();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
