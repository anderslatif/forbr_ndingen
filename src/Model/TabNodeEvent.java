package Model;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Created by Anders on 5/3/2016.
 */
public class TabNodeEvent implements TabNode{

    VBox vBox;
    SlideEvent slideEvent;

    public TabNodeEvent(VBox vBox, SlideEvent slideEvent){
        this.vBox = vBox;
        this.slideEvent = slideEvent;
    }

    public TabNodeEvent(SlideEvent slideEvent){
        this.slideEvent = slideEvent;
    }


    public VBox getVBox(){
        return vBox;
    }

    public void setVBox(VBox vBox){
        this.vBox = vBox;
    }


    public SlideEvent getSlideEvent(){
        return slideEvent;
    }

    public void setSlideEvent(SlideEvent slideEvent){
        this.slideEvent = slideEvent;
    }




}
