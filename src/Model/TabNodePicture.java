package Model;

import javafx.scene.image.ImageView;

/**
 * Created by Mikkel on 01/05/2016.
 */
public class TabNodePicture implements TabNode {

    private ImageView imageNode;
    private SlidePicture slidePicture;

    public TabNodePicture(ImageView imageNode, SlidePicture slidePicture) {
        this.imageNode = imageNode;
        this.slidePicture = slidePicture;
    }

    public TabNodePicture(ImageView imageNode) {
        this.imageNode = imageNode;
    }

    public ImageView getImageNode() {
        return imageNode;
    }

    public void setImageNode(ImageView imageNode) {
        this.imageNode = imageNode;
    }

    public SlidePicture getSlidePicture() {
        return slidePicture;
    }

    public void setSlidePicture(SlidePicture slidePicture) {
        this.slidePicture = slidePicture;
    }
}

