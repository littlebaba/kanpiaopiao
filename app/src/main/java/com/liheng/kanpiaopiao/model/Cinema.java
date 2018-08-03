package com.liheng.kanpiaopiao.model;

import android.content.Intent;

import java.io.Serializable;

/**
 * Li
 * 2018/8/3.
 */

public class Cinema implements Serializable{

    private int id;
    private String cinemaCode;
    private String cinemaName;
    private int areaId;

    public Cinema() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCinemaCode() {
        return cinemaCode;
    }

    public void setCinemaCode(String cinemaCode) {
        this.cinemaCode = cinemaCode;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }
}
