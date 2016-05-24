package Model;

/**
 * Created by Mikkel on 15/04/2016.
 */
public class Slide {

    private String slideType;
    private String imagePath;

    public Slide(){}

    public Slide(String slideType) {
        this.slideType = slideType;
    }

    public String getSlideType() {
        return slideType;
    }

    public void setSlideType(String slideType) {
        this.slideType = slideType;
    }

    public String getImagePath(){
        return imagePath;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

}
