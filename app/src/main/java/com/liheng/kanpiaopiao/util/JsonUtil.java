package com.liheng.kanpiaopiao.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.liheng.kanpiaopiao.db.KPPDB;
import com.liheng.kanpiaopiao.model.Area;
import com.liheng.kanpiaopiao.model.Cinema;
import com.liheng.kanpiaopiao.model.Ticket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Li
 * 2018/8/3.
 */

public class JsonUtil {

    public static boolean handleAreaResponse(KPPDB kppdb, String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonOneNode = jsonArray.getJSONObject(i);
                    Area area = new Area();
                    area.setAreaCode(jsonOneNode.getString("areaCode"));
                    area.setAreaName(jsonOneNode.getString("areaName"));
                    kppdb.saveArea(area);
                    JSONArray cinemaArray = jsonOneNode.getJSONArray("cinema");
                    if (cinemaArray != null) {
                        for (int j = 0; j < cinemaArray.length(); j++) {
                            JSONObject jsonTwoNode = cinemaArray.getJSONObject(j);
                            Cinema cinema = new Cinema();
                            cinema.setCinemaCode(jsonTwoNode.getString("cinemaCode"));
                            cinema.setCinemaName(jsonTwoNode.getString("cinemaName"));
                            cinema.setAreaId(jsonOneNode.getInt("id"));
                            kppdb.saveCinema(cinema);
                            JSONArray ticketArray = jsonTwoNode.getJSONArray("ticket");
                            if (ticketArray != null) {
                                for (int k = 0; k < ticketArray.length(); k++) {
                                    JSONObject jsonThreeNode = ticketArray.getJSONObject(k);
                                    Ticket ticket = new Ticket();
                                    ticket.setTicketCode(jsonThreeNode.getString("ticketCode"));
                                    ticket.setTicketName(jsonThreeNode.getString("ticketName"));
                                    ticket.setCinemaId(jsonTwoNode.getInt("id"));
                                    kppdb.saveTicket(ticket);
                                }
                            }
                        }
                    }
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static boolean handleTicketInfoResponse(Context context,String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String ticketCode = jsonObject.getString("ticketCode");
                String direct = jsonObject.getString("direct");
                String actor = jsonObject.getString("actor");
                String publishTime = jsonObject.getString("publishTime");
                String grade = jsonObject.getString("grade");
                String remainNum = jsonObject.getString("remainNum");
                String wantToSeeNum = jsonObject.getString("wantToSeeNum");
                String price = jsonObject.getString("price");
                String lateMovie = jsonObject.getString("lateMovie");
                String img = jsonObject.getString("img");
                saveTicketInfo(context,ticketCode,direct,actor,publishTime,grade,remainNum,wantToSeeNum,price,lateMovie,img);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private static void saveTicketInfo(Context context, String ticketCode, String direct, String actor, String publishTime, String grade, String remainNum, String wantToSeeNum, String price, String lateMovie, String img) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH时mm分ss秒", Locale.CHINA);
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString("current_time",sdf.format(new Date()));
        edit.putString("ticket_code",ticketCode);
        edit.putString("direct",direct);
        edit.putString("actor",actor);
        edit.putString("publish_time",publishTime);
        edit.putString("grade",grade);
        edit.putString("remain_num",remainNum);
        edit.putString("want_to_see_num",wantToSeeNum);
        edit.putString("price",price);
        edit.putString("late_movie",lateMovie);
        edit.putString("img",img);
        edit.putBoolean("ticket_selected",true);
        edit.apply();
    }


}
