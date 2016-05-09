package Model;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Created by Anders on 5/3/2016.
 */
public class TabNodeHappyHour implements TabNode{

    VBox vBox;
    TextField headerTextField;
    ImageView imageView;
    TextArea textTextArea;
    SlideHappyHour slideHappyHour;

    public TabNodeHappyHour(VBox vBox, TextField headerTextField, ImageView imageView, TextArea textTextField, SlideHappyHour slideHappyHour){
        this.vBox = vBox;
        this.headerTextField = headerTextField;
        this.imageView = imageView;
        this.textTextArea = textTextField;
        this.slideHappyHour = slideHappyHour;
    }

    public Node getNode(){
        return vBox;
    }

    public VBox getVBox(){
        return vBox;
    }

    public void setVBox(VBox vBox){
        this.vBox = vBox;
    }

    public TextField getHeaderTextField() {
        return headerTextField;
    }

    public void setHeaderTextField(TextField headerTextField) {
        this.headerTextField = headerTextField;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextArea getTextTextArea() {
        return textTextArea;
    }

    public void setTextTextArea(TextArea textTextArea) {
        this.textTextArea = textTextArea;
    }

    public SlideHappyHour getSlide() {
        return slideHappyHour;
    }

    public void setSlide(SlideHappyHour slideHappyHour) {
        this.slideHappyHour = slideHappyHour;
    }




}
