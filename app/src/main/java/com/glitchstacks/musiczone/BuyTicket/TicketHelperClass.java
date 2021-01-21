package com.glitchstacks.musiczone.BuyTicket;

public class TicketHelperClass {

    private String image,title, desc, date, time, area, amount;

    public TicketHelperClass(String image, String title, String desc, String date, String time, String area, String amount) {
        this.image = image;
        this.title = title;
        this.desc = desc;
        this.time = time;
        this.area = area;
        this.date = date;
        this.amount = amount;
    }

    public String getAmount() { return amount; }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getTime() {
        return time;
    }

    public String getArea() {
        return area;
    }

    public String getDate(){return date;}



}
