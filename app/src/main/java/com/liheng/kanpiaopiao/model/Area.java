package com.liheng.kanpiaopiao.model;

import java.io.Serializable;

/**
 * Li
 * 2018/8/3.
 */

public class Area implements Serializable{

    private int id;
    private String areaCode;
    private String areaName;

    public Area() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
