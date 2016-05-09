package Model;

import java.time.LocalDate;

/**
 * Created by Mikkel on 01/05/2016.
 */
public class SlidePicture extends Slide {

    private String imagePath;
    private final String slideType = "slidepicture";

    public SlidePicture(String imagePath) {
        this.imagePath = imagePath;

    }

    public SlidePicture(){
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
