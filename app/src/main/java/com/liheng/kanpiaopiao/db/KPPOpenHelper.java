package com.liheng.kanpiaopiao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Li
 * 2018/8/3.
 */

public class KPPOpenHelper extends SQLiteOpenHelper{

    //创表Area
    public static final String TABLE_AREA = "create table Area(" +
            "id integer primary key autoincrement, " +
            "area_code text, " +
            "area_name text)";

    public static final String TABLE_CINEMA = "create table Cinema(" +
            "id integer primary key autoincrement, " +
            "cinema_code text, " +
            "cinema_name text, " +
            "area_id integer)";

    public static final String TABLE_TICKET = "create table Ticket(" +
            "id integer primary key autoincrement, " +
            "ticket_code text, " +
            "ticket_name text, " +
            "cinema_id integer)";

    public KPPOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_AREA);
        db.execSQL(TABLE_CINEMA);
        db.execSQL(TABLE_TICKET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Area");
        db.execSQL("drop table if exists Cinema");
        db.execSQL("drop table if exists Ticket");
        onCreate(db);
    }
}
