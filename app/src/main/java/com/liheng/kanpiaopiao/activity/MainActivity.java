package com.liheng.kanpiaopiao.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.liheng.kanpiaopiao.R;
import com.liheng.kanpiaopiao.db.KPPDB;
import com.liheng.kanpiaopiao.model.Area;
import com.liheng.kanpiaopiao.model.Cinema;
import com.liheng.kanpiaopiao.model.Ticket;
import com.liheng.kanpiaopiao.util.HttpUtil;
import com.liheng.kanpiaopiao.util.JsonUtil;

import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int LEVEL_AREA = 0;
    private static final int LEVEL_CINEMA = 1;
    private static final int LEVEL_TICKET = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<>();
    private KPPDB mKppdb;

    private List<Area> areaList;
    private List<Cinema> cinemaList;
    private List<Ticket> ticketList;
    private Area selectedArea;
    private Cinema selectedCinema;
    private Ticket selectedTicket;
    private int currentLevel;

    private boolean isFromTicketActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromTicketActivity = getIntent().getBooleanExtra("from_ticket_activity", false);
        setContentView(R.layout.activity_main);
        titleText = findViewById(R.id.title_text);
        listView = findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        mKppdb = KPPDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_AREA) {
                    //显示电影院
                    selectedArea = areaList.get(position);
                    queryCinema();

                } else if (currentLevel == LEVEL_CINEMA) {
                    //显示电影票
                    selectedCinema = cinemaList.get(position);
                    queryTicket();
                } else if (currentLevel == LEVEL_TICKET) {
                    String ticketCode = ticketList.get(position).getTicketCode();
                    String ticketName = ticketList.get(position).getTicketName();
                    Intent intent = new Intent(MainActivity.this, TicketInfoActivity.class);
                    intent.putExtra("ticket_code", ticketCode);
                    intent.putExtra("ticket_name", ticketName);
                    startActivity(intent);
                    finish();
                }
            }
        });
        queryAreas();
    }

    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_TICKET) {
            queryCinema();
        } else if (currentLevel == LEVEL_CINEMA) {
            queryAreas();
        } else {
            if (isFromTicketActivity) {
                Intent intent = new Intent(this, TicketInfoActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }

    private void queryTicket() {
        ticketList = mKppdb.loadTicket(selectedCinema.getId());
        if (ticketList.size() > 0) {
            dataList.clear();
            for (Ticket ticket : ticketList) {
                dataList.add(ticket.getTicketName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCinema.getCinemaName());
            currentLevel = LEVEL_TICKET;
        } else {
            Toast.makeText(MainActivity.this, selectedCinema.getCinemaName() + "电影票已售空", Toast.LENGTH_SHORT).show();
        }
    }

    private void queryCinema() {
        cinemaList = mKppdb.loadCinema(selectedArea.getId());
        if (cinemaList.size() > 0) {
            dataList.clear();
            for (Cinema cinema : cinemaList) {
                dataList.add(cinema.getCinemaName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedArea.getAreaName());
            currentLevel = LEVEL_CINEMA;
        } else {
            //再说
            Toast.makeText(MainActivity.this, selectedArea.getAreaName() + "没有影院", Toast.LENGTH_SHORT).show();
        }

    }

    private void queryAreas() {
        areaList = mKppdb.loadArea();
        if (areaList.size() > 0) {
            dataList.clear();
            for (Area area : areaList) {
                dataList.add(area.getAreaName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("北京");
            currentLevel = LEVEL_AREA;
        } else {
            //两种地址格式
            queryFromServer();
        }
    }

    private void queryFromServer() {

        String address = "http://58.87.124.18:8080/kanpiaopiao/data/area";
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpUtil.HttpCallbackLister() {
            @Override
            public void onFinish(String reponse) {
                boolean result = false;
                result = JsonUtil.handleAreaResponse(mKppdb, reponse);
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProcessDialog();
                            queryAreas();
                        }
                    });
                }
            }

            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, e.toString());
                        closeProcessDialog();
                        Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void closeProcessDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
}
