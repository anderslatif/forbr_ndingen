package model;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Created by Anders on 5/3/2016.
 */
public class TabNodeHappyHour implements TabNode{

    private VBox vBox;
    private ImageView imageView;
    private SlideHappyHour slideHappyHour;

    public TabNodeHappyHour(VBox vBox, ImageView imageView, SlideHappyHour slideHappyHour){
        this.vBox = vBox;
        this.imageView = imageView;
        this.slideHappyHour = slideHappyHour;
    }

    public Node getNode(){
        return vBox;
    }

    public void setVBox(VBox vBox){
        this.vBox = vBox;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public SlideHappyHour getSlide() {
        return slideHappyHour;
    }

    public void setSlide(SlideHappyHour slideHappyHour) {
        this.slideHappyHour = slideHappyHour;
    }




}
