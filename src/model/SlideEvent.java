package model;

import java.time.LocalDate;

/**
 * Created by Mikkel on 02/05/2016.
 */
public class SlideEvent extends Slide {

    private String date;
    private LocalDate localDate;
    private String startTime;
    private String header;
    private String text;
    private String imagePath;

    public SlideEvent(){}

    public SlideEvent(String slideType, String date, LocalDate localDate, String startTime, String header, String text, String imagePath) {
        super(slideType);
        this.date = date;
        this.localDate = localDate;
        this.startTime = startTime;
        this.header = header;
        this.text = text;
        this.imagePath = imagePath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public String getStartTime(){
        return startTime;
    }

    public void setStartTime(String startTime){
        this.startTime = startTime;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
