package Model;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Created by Anders on 5/3/2016.
 */
public class TabNodeEvent implements TabNode{

    VBox vBox;
    ImageView imageView;
    SlideEvent slideEvent;

    public TabNodeEvent(VBox vBox, ImageView imageView, SlideEvent slideEvent){
        this.vBox = vBox;
        this.imageView = imageView;
        this.slideEvent = slideEvent;
    }



    public VBox getVBox(){
        return vBox;
    }

    public void setVBox(VBox vBox){
        this.vBox = vBox;
    }


    public ImageView getImageView(){
        return imageView;
    }

    public void setImageView(ImageView imageView){
        this.imageView = imageView;
    }


    public SlideEvent getSlideEvent(){
        return slideEvent;
    }

    public void setSlideEvent(SlideEvent slideEvent){
        this.slideEvent = slideEvent;
    }




}
