package com.glitchstacks.musiczone;

public class PromotorConcert {

    private String concertName, concertDescription, concertDate, concertImageURL, concertKey;
    Boolean isSelected;

    public PromotorConcert(String concertName, String concertDescription, String concertDate, String concertImageURL, String concertKey) {
        this.concertName = concertName;
        this.concertDescription = concertDescription;
        this.concertDate = concertDate;
        this.concertKey = concertKey;
        this.concertImageURL = concertImageURL;
        this.isSelected = false;
    }

    public String getConcertImageURL() {
        return concertImageURL;
    }

    public void setConcertImageURL(String concertImageURL) {
        this.concertImageURL = concertImageURL;
    }

    public String getConcertName() {
        return concertName;
    }

    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }

    public String getConcertDescription() {
        return concertDescription;
    }

    public void setConcertDescription(String concertDescription) {
        this.concertDescription = concertDescription;
    }

    public String getConcertDate() {
        return concertDate;
    }

    public void setConcertDate(String concertDate) {
        this.concertDate = concertDate;
    }

    public String getConcertKey() {
        return concertKey;
    }

    public void setConcertKey(String concertKey) {
        this.concertKey = concertKey;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
