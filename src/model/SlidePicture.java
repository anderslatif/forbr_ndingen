package model;

/**
 * Created by Mikkel on 01/05/2016.
 */
public class SlidePicture extends Slide {


    private String imagePath;

    public SlidePicture(){
    }


    public SlidePicture(String imagePath) {
        this.imagePath = imagePath;
    }

    public SlidePicture(String slideType, String imagePath){
        super(slideType);
        this.imagePath = imagePath;
    }



    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
