package com.glitchstacks.musiczone.BuyTicket;

public class TicketHelperClass {

    private String image,title, desc, date, time, area, amount, phoneNumber, price, ticketKey, concertKey;

    Boolean isSelected;

    public TicketHelperClass(String image, String title, String desc, String date, String time, String area, String amount, String phoneNumber,String ticketKey, String price) {
        this.image = image;
        this.title = title;
        this.desc = desc;
        this.time = time;
        this.area = area;
        this.date = date;
        this.amount = amount;
        this.isSelected = false;
        this.phoneNumber = phoneNumber;
        this.price = price;
        this.ticketKey = ticketKey;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTicketKey() {
        return ticketKey;
    }

    public void setTicketKey(String ticketKey) {
        this.ticketKey = ticketKey;
    }

}
