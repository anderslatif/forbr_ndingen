package Model;

import java.time.LocalDate;

/**
 * Created by Mikkel on 02/05/2016.
 */
public class SlideEvent extends Slide {

    private LocalDate date;
    private String header;
    private String textLabel;
    private String imagePath;
    private final String slideType = "slideevent";

    public SlideEvent(){}

    public SlideEvent(LocalDate date, String header, String textLabel, String imagePath) {
        this.date = date;
        this.header = header;
        this.textLabel = textLabel;
        this.imagePath = imagePath;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getTextLabel() {
        return textLabel;
    }

    public void setTextLabel(String textLabel) {
        this.textLabel = textLabel;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSlideType() {
        return slideType;
    }
}
