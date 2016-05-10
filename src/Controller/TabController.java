package Controller;

import Model.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
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
    ArrayList<TabNode> tabCollection;

    public TabController(Scene scene, Stage stage){
        this.scene = scene;
        this.stage = stage;
        tabCollection = new ArrayList<>();
        controller = new Controller();
    }




    public TabPane getTabPane(){

        tabCollection.clear();

        tabPane = new TabPane();

        initializeTabController(tabPane);

        return tabPane;
    }



    public void initializeTabController(TabPane tabPane){

        Tab firstTab = new Tab();

        Label label = new Label("This tab is Empty \nPlease select a slide type.");
        label.setPadding(new Insets(100, 100, 100, 100));
        label.setStyle("-fx-font: 20 arial;");

        firstTab.setContent(label);

        int compensatingForStartAtMinusOne = tabPane.getSelectionModel().getSelectedIndex() + 2;
        String title = String.valueOf(compensatingForStartAtMinusOne);
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



    public void addPictureToATab(File file){


        // file:/// with three slashes before the absolute file path helps avoid "MediaException: MEDIA_INACCESSIBLE"
        String imagePath = "file:///" + file.getAbsoluteFile().toString();
        Image image = new Image(imagePath);

        Tab tab = tabPane.getSelectionModel().getSelectedItem();

        if(tab.getContent() instanceof javafx.scene.image.ImageView){
            ImageView currentImageView = (ImageView) tab.getContent();
            currentImageView.setImage(image);

            for(TabNode tabNode : tabCollection){

                if(tabNode instanceof TabNodePicture){

                    TabNodePicture tabNodePicture = (TabNodePicture) tabNode;

                    if(tabNodePicture.getImageNode() == currentImageView){
                        SlidePicture slidePicture = tabNodePicture.getSlide();
                        slidePicture.setImagePath(imagePath);
                    }
                }

            }


        } else if(tab.getContent() instanceof javafx.scene.layout.VBox) {

            VBox vBox = (VBox) tab.getContent();

            System.out.println("dette er en SlideHappy");

            ImageView currentImageView = null;

            for (Node node : vBox.getChildren()) {

                if(node instanceof javafx.scene.image.ImageView){
                    currentImageView = (ImageView) node;
                    currentImageView.setImage(image);
                } else if (node instanceof  javafx.scene.layout.VBox){

                    VBox childVBox = (VBox) node;

                    for (Node noodle : childVBox.getChildren()){

                        if(noodle instanceof javafx.scene.image.ImageView){
                            currentImageView = (ImageView) noodle;
                            currentImageView.setImage(image);
                        }
                    }

                }
            }
            for (TabNode tabNode : tabCollection) {

                if (tabNode instanceof TabNodeHappyHour) {

                    TabNodeHappyHour tabNodeHappyHour = (TabNodeHappyHour) tabNode;

                    if (tabNodeHappyHour.getImageView() == currentImageView) {
                        SlideHappyHour slideHappyHour = tabNodeHappyHour.getSlide();
                        slideHappyHour.setImagePath(imagePath);
                    }

                } else if(tabNode instanceof TabNodeEvent){

                    TabNodeEvent tabNodeEvent = (TabNodeEvent) tabNode;

                    if (tabNodeEvent.getImageView() == currentImageView){
                        SlideEvent slideEvent = tabNodeEvent.getSlide();
                        slideEvent.setImagePath(imagePath);
                    }


                }


            }


        }


    }


    public void addPictureTab(){

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

            SlidePicture slidePicture = new SlidePicture();
            slidePicture.setSlideType("SlidePicture");
            TabNodePicture tabNodePicture = new TabNodePicture(imageView, slidePicture);
            tabCollection.add(tabNodePicture);


        } else {

            Tab tab = tabPane.getSelectionModel().getSelectedItem();

            ImageView imageView = new ImageView();
            imageView.fitWidthProperty().bind(stage.widthProperty());
            imageView.fitHeightProperty().bind(stage.heightProperty());

            tab.setContent(imageView);

            SlidePicture slidePicture = new SlidePicture();
            slidePicture.setSlideType("SlidePicture");
            TabNodePicture tabNodePicture = new TabNodePicture(imageView, slidePicture);
            tabCollection.add(tabNodePicture);

        }

    }


    public void addEventTab(SlideEvent slideEvent){

        Tab tab;

        slideEvent.setSlideType("SlideEvent");

        if(tabCollection.size()>0){
            tab = new Tab();
            tabPane.getTabs().add(tab);
        } else{
            tab = tabPane.getSelectionModel().getSelectedItem();
        }


        tabPane.getSelectionModel().select(tab);
        int correctingIndexingIssues = tabPane.getSelectionModel().getSelectedIndex() + 1;
        String title = String.valueOf(correctingIndexingIssues);
        tab.setText(title);



        VBox vBox = new VBox();
        vBox.getStyleClass().add("eventSlidePane");

        TextField headerLabel = new TextField(slideEvent.getHeader());
        headerLabel.setOpacity(0.4);
        headerLabel.getStyleClass().add("header");
        headerLabel.textProperty().addListener( e -> slideEvent.setHeader(headerLabel.getText()));


        Image image;
        if (slideEvent.getImagePath().equals("null")){
            image = new Image("Empty.png");
        } else {
            image = new Image(slideEvent.getImagePath());
        }

        ImageView imageView = new ImageView(image);
        imageView.fitHeightProperty().bind(vBox.heightProperty().divide(4));
        imageView.fitWidthProperty().bind(vBox.widthProperty());

        VBox filler1 = new VBox();
        filler1.setPadding(new Insets(20, 0, 0, 0));
        filler1.getChildren().addAll(headerLabel, imageView);


        VBox filler2 = new VBox();
        filler2.setPadding(new Insets(40, 0, 0, 0));

        TextArea textTextArea = new TextArea(slideEvent.getText());
        textTextArea.getStyleClass().add("text_area");
        textTextArea.setOpacity(0.3);
        textTextArea.textProperty().addListener( e -> slideEvent.setText(textTextArea.getText()));

        filler2.getChildren().add(textTextArea);


        vBox.getChildren().addAll(filler1, filler2);

        tab.setContent(vBox);


        TabNodeEvent tabNodeEvent = new TabNodeEvent(vBox, imageView, slideEvent);
        tabCollection.add(tabNodeEvent);

    }


    public void addHappyHourTab(){

        Tab tab;

        if(tabCollection.size()>0){
            tab = new Tab();
            tabPane.getTabs().add(tab);
        } else{
            tab = tabPane.getSelectionModel().getSelectedItem();
        }

        tabPane.getSelectionModel().select(tab);
        int correctingIndexingIssues = tabPane.getSelectionModel().getSelectedIndex() + 1;
        String title = String.valueOf(correctingIndexingIssues);
        tab.setText(title);


        VBox vBox = new VBox();
        vBox.getStyleClass().add("happyHour");

        Image image = new Image("cocktail.png");

        TextField headerTextField = new TextField();
        headerTextField.getStyleClass().add("header");
        headerTextField.setPromptText("Type the header here...");
        headerTextField.setOpacity(0.6);

        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.fitHeightProperty().bind(vBox.heightProperty().divide(3));
        imageView.fitWidthProperty().bind(vBox.widthProperty());

        TextArea textTextArea =  new TextArea();
        textTextArea.setPromptText("Type more text here...");
        textTextArea.setOpacity(0.6);
        textTextArea.getStyleClass().add("text_area");

        SlideHappyHour slideHappyHour = new SlideHappyHour();
        slideHappyHour.setSlideType("SlideHappyHour");
        headerTextField.textProperty().addListener( e -> slideHappyHour.setHeader(headerTextField.getText()));
        textTextArea.textProperty().addListener( e -> slideHappyHour.setText(textTextArea.getText()));


        vBox.getChildren().addAll(headerTextField, imageView, textTextArea);



        TabNodeHappyHour tabNodeHappyHour = new TabNodeHappyHour(vBox, imageView, slideHappyHour);
        tabCollection.add(tabNodeHappyHour);

        tab.setContent(vBox);


    }


    public void savingPresentation(String chosenDate){

        ArrayList<TabNode> presentation = new ArrayList();


        for(Tab tab : tabPane.getTabs()){

            for (TabNode tabNode : tabCollection){

                if(tab.getContent() == tabNode.getNode()){

                    presentation.add(tabNode);
                }

            }
        }

        controller.savePresentation(presentation, chosenDate);
        tabCollection.clear();
    }




}
