package View;

import Controller.Controller;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 4/15/2016.
 */
public class Root {

    Stage stage;
    Scene scene;
    static Controller controller;

    public void initializeLayout(Scene scene, Stage stage){
        this.scene = scene;
        this.stage = stage;
        controller = new Controller();
    }




    public BorderPane getRootLayout(){

        BorderPane borderPane = new BorderPane();

        borderPane.setTop(getMenuBar());

        borderPane.setCenter(getTabPane());

        return borderPane;
    }


    TabPane tabPane;
    List<Tab> tabsList;


    // things to consider about tabs... should they be closable or are we required to create a delete button to properly dispose of them in the arraylist

    public TabPane getTabPane(){

        tabPane = new TabPane();
//        tabPane.setTabMinHeight(60);
//        tabPane.setTabMinWidth(185);
        Tab firstTab = new Tab();     // setName() depending on every slides position in the ArrayList

        int indexZeroGoAway = tabPane.getSelectionModel().getSelectedIndex() + 2;
        String title = String.valueOf(indexZeroGoAway);
        firstTab.setText(title);

        firstTab.setContent(createEmptyProject());
        tabPane.getTabs().addAll(firstTab);

        tabsList = new ArrayList<Tab>();
        tabsList.add(firstTab);

        // tabPane.getSelectionModel().selectedItemProperty().addListener( (ov, oldTab, newTab) -> System.out.println("ov: " + ov + "\noldtab: " + oldTab + "\nnewTab:  " + newTab));



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
                    addPictureToAnchorPane(file);
                }
            }
            e.setDropCompleted(success);
            e.consume();
        });


        return tabPane;
    }



    public MenuBar getMenuBar(){

        MenuBar menuBar = new MenuBar();

        ///////////////////////////////////////
        Menu menu1 = new Menu("File");

        MenuItem m1_1 = new MenuItem("_New Presentation");
        m1_1.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));  //todo need to change this to reflect Mac's command key
        m1_1.setOnAction( e -> newPresentation());

        MenuItem m1_2 = new MenuItem("_Open");
        m1_2.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        m1_2.setOnAction( e -> controller.openPresentation());

        MenuItem m1_3 = new MenuItem("_Save");
        m1_3.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        m1_3.setOnAction( e -> controller.savePresentation());

        menu1.getItems().addAll(m1_1, m1_2, m1_3);

        ///////////////////////////////////////
        Menu menu2 = new Menu("Events");

        MenuItem m2_1 = new MenuItem("_Events");
        // m2_1.setOnAction( e -> method partly in controller and the rest in view that shows us all the events in a new scene and lets us create new ones, select from the list etc.);

        menu2.getItems().addAll(m2_1);

        ///////////////////////////////////////
        Menu menu3 = new Menu("Add a Slide");

        MenuItem m3_1 = new MenuItem("Event slide");
        m3_1.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
        //m3_1.setOnAction( e -> controller.newSlide());
        m3_1.setOnAction( e -> addNewTab());


        menu3.getItems().addAll(m3_1);

        ///////////////////////////////////////
        Menu menu4 = new Menu("Start Presentation");

        MenuItem m4_1 = new MenuItem("_Go");

        menu4.getItems().addAll(m4_1);

        ///////////////////////////////////////

        menuBar.getMenus().addAll(menu1, menu2, menu3, menu4);

        return menuBar;
    }


    AnchorPane anchorPane;  // keeping it here for reference.. can be moved up later

    public AnchorPane createEmptyProject(){
        AnchorPane anchorPane = new AnchorPane();

        return anchorPane;
    }


    public void newPresentation(){
        // first ask the user if they want to save first
        // if yes, then save the presentation to the database
        // either way the arraylist in the controller needs to be emptied
        // then...
        createEmptyProject();
    }


    public void addPictureToAnchorPane(File file){

        // file:/// with three slashes before the absolute file path helps avoid "MediaException: MEDIA_INACCESSIBLE"
        String imagePath = "file:///" + file.getAbsoluteFile().toString();

        try {
            Image image = new Image(imagePath);

            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            AnchorPane anchorPane = (AnchorPane) tab.getContent();
            ImageView imageView = new ImageView();
            anchorPane.getChildren().add(imageView);

            imageView.fitWidthProperty().bind(stage.widthProperty());
            imageView.fitHeightProperty().bind(stage.heightProperty());
            imageView.setImage(image);

            controller.updateImageForSlideObjectInList(file);

        } catch(Exception e){
            // a pop-up telling the user that the file appears not to be an image
            // we need to properly check if the file is an image ... because?!
            // but right now we will never get here
            e.printStackTrace();
        }

    }



    public void addNewTab(){
        Tab tab = new Tab();

        tabPane.getSelectionModel().select(tab);
        int correctingIndexingIssues = tabPane.getSelectionModel().getSelectedIndex() + 2;
        String title = String.valueOf(correctingIndexingIssues);
        tab.setText(title);

        AnchorPane anchorPane = new AnchorPane();
//        ImageView imageView = new ImageView();
//        anchorPane.getChildren().add(imageView);
        tab.setContent(anchorPane);

        tabPane.getTabs().add(tab);
        tabsList.add(tab);

    }








}
