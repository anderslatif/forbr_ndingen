package Model;

/**
 * Created by Mikkel on 02/05/2016.
 */
public class SlideEvent extends Slide {

    private String date;
    private String header;
    private String text;
    private String imagePath;

    public SlideEvent(){}

    public SlideEvent(String slideType, String date, String header, String text, String imagePath) {
        super();
        this.date = date;
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
