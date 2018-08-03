package com.liheng.kanpiaopiao.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.liheng.kanpiaopiao.R;
import com.liheng.kanpiaopiao.service.AutoUpdateService;
import com.liheng.kanpiaopiao.util.HttpUtil;
import com.liheng.kanpiaopiao.util.JsonUtil;

import org.json.JSONObject;

public class TicketInfoActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = TicketInfoActivity.class.getSimpleName();

    private ImageView movieImg;
    private TextView movieName,grade,direct,actor,wantSeeNum,lateMovie,price,update;
    private Button switchTicket,refresh;
    String ticketName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_info);
        initView();
        String ticketCode = getIntent().getStringExtra("ticket_code");
        ticketName = getIntent().getStringExtra("ticket_name");
        if (!TextUtils.isEmpty(ticketCode)){
            update.setText("正在更新...");
            queryTicketInfo(ticketCode);
        }else {
            showTicketInfo();
        }
    }

    private void queryTicketInfo(String ticketCode) {
        String address = "http://58.87.124.18:8080/kanpiaopiao/data/ticketinfo"+ticketCode;
        HttpUtil.sendHttpRequest(address, new HttpUtil.HttpCallbackLister() {
            @Override
            public void onFinish(String reponse) {
                JsonUtil.handleTicketInfoResponse(TicketInfoActivity.this,reponse);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showTicketInfo();
                    }
                });
            }

            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,e.toString());
                        update.setText("更新失败");
                    }
                });
            }
        });
    }

    private void showTicketInfo() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //电影名字
        movieName.setText(ticketName);
        grade.setText(prefs.getString("grade",""));
        direct.setText(prefs.getString("direct",""));
        actor.setText(prefs.getString("actor",""));
        wantSeeNum.setText(prefs.getString("want_to_see_num",""));
        lateMovie.setText(prefs.getString("late_movie",""));
        price.setText(prefs.getString("price","")+"元");
        update.setText(prefs.getString("current_time","")+"更新");
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    private void initView() {
        movieImg = findViewById(R.id.img_iv);
        movieName = findViewById(R.id.movie_name_tv);
        grade = findViewById(R.id.grade);
        direct = findViewById(R.id.direct);
        actor = findViewById(R.id.actor);
        wantSeeNum = findViewById(R.id.want_see_num_tv);
        lateMovie = findViewById(R.id.late_moive_tv);
        price = findViewById(R.id.price_tv);
        update = findViewById(R.id.update);
        switchTicket = findViewById(R.id.swich_ticket);
        refresh = findViewById(R.id.refresh);
        switchTicket.setOnClickListener(this);
        refresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.swich_ticket:
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra("from_ticket_activity",true);
                startActivity(intent);
                break;
            case R.id.refresh:
                update.setText("更新中...");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String ticketCode = prefs.getString("ticket_code", "");
                if (!TextUtils.isEmpty(ticketCode)){
                    queryTicketInfo(ticketCode);
                }
                break;
            default:
                break;
        }
    }
}
