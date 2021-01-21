package com.glitchstacks.musiczone.PostConcert;

import java.io.Serializable;

public class Area implements Serializable {

    private String areaPrice;
    private String areaName;

    public Area(String areaPrice, String areaName){
        this.areaName = areaName;
        this.areaPrice = areaPrice;
    }

    public String getAreaPrice(){return areaPrice;}
    public String getAreaName(){return areaName;}

}
