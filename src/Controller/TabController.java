package Controller;

import Model.SlidePicture;
import Model.TabNode;
import Model.TabNodePicture;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Anders on 4/26/2016.
 */
public class TabController {

    Stage stage;
    Scene scene;
    TabPane tabPane;
    Controller controller;
    ArrayList<TabNode> tabCollection = new ArrayList<>();

    public TabController(Scene scene, Stage stage){
        this.scene = scene;
        this.stage = stage;
        controller = new Controller();
    }

    public void initializeTabController(TabPane tabPane){

        Tab firstTab = new Tab();

        tabPane.getSelectionModel().select(firstTab);
        int indexZeroGoAway = tabPane.getSelectionModel().getSelectedIndex() + 2;
        String title = String.valueOf(indexZeroGoAway);
        firstTab.setText(title);
        tabPane.getTabs().addAll(firstTab);



        tabPane.getSelectionModel().selectedItemProperty().addListener( (ov, oldTab, newTab) -> {
            int tabText = 1;
            for(Tab tab : tabPane.getTabs()){
                tab.setText(String.valueOf(tabText));
                tabText++;
            }
        });


        tabPane.setOnDragOver( e -> {
            Dragboard db = e.getDragboard();
            if (db.hasFiles()) {
                e.acceptTransferModes(TransferMode.ANY);
            } else {
                e.consume();
            }
        });


        tabPane.setOnDragDropped( e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                for (File file : db.getFiles()) {
                    addPictureToATab(file);
                }
            }
            e.setDropCompleted(success);
            e.consume();
        });


    }



    public TabPane getTabPane(){

        tabPane = new TabPane();

        initializeTabController(tabPane);

        return tabPane;
    }


/*

In the method addPictureToATab(File file), find your TabNodePicture in the ArrayList and set the image/file path.

 */

    public void addPictureToATab(File file){


            // file:/// with three slashes before the absolute file path helps avoid "MediaException: MEDIA_INACCESSIBLE"
            String imagePath = "file:///" + file.getAbsoluteFile().toString();
            Image image = new Image(imagePath);

            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            ImageView currentImageView = (ImageView) tab.getContent();


            currentImageView.setImage(image);



            for(TabNode tabNode : tabCollection){

                if(tabNode instanceof TabNodePicture){

                    TabNodePicture tabNodePicture = (TabNodePicture) tabNode;

                    if(tabNodePicture.getImageNode() == currentImageView){
                        SlidePicture slidePicture = tabNodePicture.getSlidePicture();
                        slidePicture.setImagePath(imagePath);
                    }
                }

            }
            tab.setContent(currentImageView);






    }


    public void addNewTab(){


        if(tabCollection.size()>0){
            Tab tab = new Tab();

            tabPane.getSelectionModel().select(tab);
            int correctingIndexingIssues = tabPane.getSelectionModel().getSelectedIndex() + 2;
            String title = String.valueOf(correctingIndexingIssues);
            tab.setText(title);

            tabPane.getTabs().add(tab);
            ImageView imageView = new ImageView();
            imageView.fitWidthProperty().bind(stage.widthProperty());
            imageView.fitHeightProperty().bind(stage.heightProperty());

            tab.setContent(imageView);

            TabNodePicture tabNodePicture = new TabNodePicture(imageView);
            tabCollection.add(tabNodePicture);


        } else {

            Tab tab = tabPane.getSelectionModel().getSelectedItem();

            ImageView imageView = new ImageView();
            imageView.fitWidthProperty().bind(stage.widthProperty());
            imageView.fitHeightProperty().bind(stage.heightProperty());

            tab.setContent(imageView);

            TabNodePicture tabNodePicture = new TabNodePicture(imageView);
            tabCollection.add(tabNodePicture);

        }

    }


    public void savingPresentation(){

        ArrayList presentation = new ArrayList();

        for(Tab tab : tabPane.getTabs()){
            System.out.println(tab.getContent());

            for (TabNode tabNode : tabCollection){
                if(tab.getContent() == tabNode){
                    presentation.add(tabNode);
                }
            }
        }

        controller.savePresentation(presentation);
    }


    public void runPresentation(){

        BorderPane borderPane = new BorderPane();
        Scene presentationScene = new Scene(borderPane, 400, 650);
        Stage presentationStage = new Stage();
        presentationStage.setScene(presentationScene);
        presentationStage.show();
        borderPane.setOnMouseClicked( e -> presentationStage.close());


        for(Tab tab : tabPane.getTabs()){

            if(tab.getContent() != null){

                ImageView imageView = (ImageView) tab.getContent();
                imageView.fitHeightProperty().bind(presentationStage.heightProperty());
                imageView.fitWidthProperty().bind(presentationStage.widthProperty());

                borderPane.setCenter(imageView);

            }
        }

    }



}
