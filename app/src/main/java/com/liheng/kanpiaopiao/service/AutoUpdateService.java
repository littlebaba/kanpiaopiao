package com.liheng.kanpiaopiao.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.liheng.kanpiaopiao.activity.TicketInfoActivity;
import com.liheng.kanpiaopiao.receiver.AutoUpdateReceiver;
import com.liheng.kanpiaopiao.util.HttpUtil;
import com.liheng.kanpiaopiao.util.JsonUtil;

public class AutoUpdateService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                updateTicket();
            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int fiveMinute = 1000*60*5;
        long triggerAtTime = SystemClock.elapsedRealtime() + fiveMinute;
        Intent i = new Intent(this,AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateTicket() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String ticketCode = prefs.getString("ticket_code", "");
        String address = "http://58.87.124.18:8080/kanpiaopiao/data/ticketinfo"+ticketCode;
        HttpUtil.sendHttpRequest(address, new HttpUtil.HttpCallbackLister() {
            @Override
            public void onFinish(String reponse) {
                JsonUtil.handleTicketInfoResponse(AutoUpdateService.this,reponse);

            }

            @Override
            public void onError(final Exception e) {
                e.printStackTrace();
            }
        });
    }
}
