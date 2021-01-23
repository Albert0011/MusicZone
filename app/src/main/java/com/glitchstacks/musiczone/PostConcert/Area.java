package com.glitchstacks.musiczone.PostConcert;

import java.io.Serializable;

public class Area implements Serializable {

    private String areaPrice;
    private String areaName;
    private String ticketAmount;

    public Area(String areaPrice, String areaName, String ticketAmount){
        this.areaName = areaName;
        this.areaPrice = areaPrice;
        this.ticketAmount = ticketAmount;
    }

    public String getAreaPrice(){return areaPrice;}
    public String getAreaName(){return areaName;}
    public String getTicketAmount(){return ticketAmount;}

}
