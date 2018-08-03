package com.liheng.kanpiaopiao.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ProgressBar;

import com.liheng.kanpiaopiao.model.Area;
import com.liheng.kanpiaopiao.model.Cinema;
import com.liheng.kanpiaopiao.model.Ticket;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

/**
 * Li
 * 2018/8/3.
 */
/*单例模式：使数据库对象全局唯一*/
public class KPPDB {

    /*数据库的版本号*/
    private final int VERSION = 2;
    /*数据库的名字*/
    private final String DB_NAME = "kanpiaopiao";

    private static KPPDB kppdb;
    private SQLiteDatabase db;

    private KPPDB(Context context){
        KPPOpenHelper openHelper = new KPPOpenHelper(context, DB_NAME, null, VERSION);
        db = openHelper.getWritableDatabase();
    }

    public static KPPDB getInstance(Context context){
        if (kppdb == null){
            kppdb = new KPPDB(context);
        }
        return kppdb;
    }

    /**
     *------------------------------------------------存取Area-------------------------------------
     */
    public void saveArea(Area area){
        if (area != null){
            ContentValues values = new ContentValues();
            values.put("area_code",area.getAreaCode());
            values.put("area_name",area.getAreaName());
            db.insert("Area",null,values);
        }
    }

    public List<Area> loadArea(){
        List<Area> areaList = new ArrayList<>();
        Cursor cursor = db.query("Area",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                Area area = new Area();
                area.setId(cursor.getInt(cursor.getColumnIndex("id")));
                area.setAreaCode(cursor.getString(cursor.getColumnIndex("area_code")));
                area.setAreaName(cursor.getString(cursor.getColumnIndex("area_name")));
                areaList.add(area);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return areaList;
    }

    /**
     *------------------------------------------------存取Cinema-------------------------------------
     */
    public void saveCinema(Cinema cinema){
        if (cinema != null){
            ContentValues values = new ContentValues();
            values.put("cinema_code",cinema.getCinemaCode());
            values.put("cinema_name",cinema.getCinemaName());
            values.put("area_id",cinema.getAreaId());
            db.insert("Cinema",null,values);
        }
    }

    public List<Cinema> loadCinema(int areaId){
        List<Cinema> cinemaList = new ArrayList<>();
        Cursor cursor =db.query("Cinema",null,"area_id=?",new String[]{String.valueOf(areaId)},null,null,null);
        if (cursor.moveToFirst()){
            do {
                Cinema cinema = new Cinema();
                cinema.setId(cursor.getInt(cursor.getColumnIndex("id")));
                cinema.setCinemaCode(cursor.getString(cursor.getColumnIndex("cinema_code")));
                cinema.setCinemaName(cursor.getString(cursor.getColumnIndex("cinema_name")));
                cinema.setAreaId(areaId);
                cinemaList.add(cinema);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return cinemaList;
    }


    /**
     *------------------------------------------------存取Ticket-------------------------------------
     */
    public void saveTicket(Ticket ticket){
        if (ticket != null){
            ContentValues values = new ContentValues();
            values.put("ticket_code",ticket.getTicketCode());
            values.put("ticket_name",ticket.getTicketName());
            values.put("cinema_id",ticket.getCinemaId());
            db.insert("Ticket",null,values);
        }
    }

    public List<Ticket> loadTicket(int cinemaId){
        List<Ticket> ticketList = new ArrayList<>();
        Cursor cursor = db.query("Ticket",null,"cinema_id=?",new String[]{String.valueOf(cinemaId)},null,null,null);
        if (cursor.moveToFirst()){
            do {
                Ticket ticket = new Ticket();
                ticket.setId(cursor.getInt(cursor.getColumnIndex("id")));
                ticket.setTicketCode(cursor.getString(cursor.getColumnIndex("ticket_code")));
                ticket.setTicketName(cursor.getString(cursor.getColumnIndex("ticket_name")));
                ticket.setCinemaId(cinemaId);
                ticketList.add(ticket);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return ticketList;
    }
}
