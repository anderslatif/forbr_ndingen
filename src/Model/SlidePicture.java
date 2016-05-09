package Model;

import java.time.LocalDate;

/**
 * Created by Mikkel on 01/05/2016.
 */
public class SlidePicture extends Slide {

<<<<<<< HEAD
    private String date;
    private String imagePath;
    private final String slideType = "slidepicture";

    public SlidePicture(String date, String imagePath) {
        this.date = date;
=======
    private String imagePath;
    private final String slideType = "slidepicture";

    public SlidePicture(String imagePath) {
>>>>>>> cb7d35c528241e4ca63f55aa0f019c640f0faddf
        this.imagePath = imagePath;

    }

    public SlidePicture(){
    }

<<<<<<< HEAD
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

=======
>>>>>>> cb7d35c528241e4ca63f55aa0f019c640f0faddf
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
