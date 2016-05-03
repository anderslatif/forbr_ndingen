package Model;

import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 * Created by Anders on 5/3/2016.
 */
public class TabNodeHappyHour implements TabNode{

    TextField headerTextField;
    ImageView imageView;
    TextField textTextField;
    SlideHappyHour slideHappyHour;

    public TabNodeHappyHour(TextField headerTextField, ImageView imageView, TextField textTextField, SlideHappyHour slideHappyHour){
        this.headerTextField = headerTextField;
        this.imageView = imageView;
        this.textTextField = textTextField;
        this.slideHappyHour = slideHappyHour;
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

    public TextField getTextTextField() {
        return textTextField;
    }

    public void setTextTextField(TextField textTextField) {
        this.textTextField = textTextField;
    }

    public SlideHappyHour getSlideHappyHour() {
        return slideHappyHour;
    }

    public void setSlideHappyHour(SlideHappyHour slideHappyHour) {
        this.slideHappyHour = slideHappyHour;
    }




}
