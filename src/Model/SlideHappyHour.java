package Model;

/**
 * Created by Anders on 5/3/2016.
 */
public class SlideHappyHour extends Slide {

    private String header;
    private String text;
    private String imagePath;

    public SlideHappyHour(String slideType, String header, String text, String imagePath){
        super();
        this.header = header;
        this.text = text;
        this.imagePath = imagePath;
    }

    public SlideHappyHour(){}



    public String getHeader(){
        return header;
    }

    public void setHeader(String header){
        this.header = header;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }


    public String getImagePath(){
        return imagePath;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }



}
