package Model;

import java.time.LocalDate;

/**
 * Created by Mikkel on 01/05/2016.
 */
public class SlidePicture extends Slide {

    private String date;
    private String imagePath;
    private final String slideType = "slidepicture";

    public SlidePicture(String date, String imagePath) {
        this.date = date;
        this.imagePath = imagePath;

    }

    public SlidePicture(){
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
