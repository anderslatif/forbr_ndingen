package View;

import Controller.Controller;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 4/21/2016.
 */
public class Layout {

    Stage stage;
    Scene scene;
    Controller controller;
    ArrayList<File> imageList;


    public void initializeLayout(Scene scene, Stage stage){
        this.scene = scene;
        this.stage = stage;
        controller = new Controller();
        imageList = new ArrayList<>();
    }




    public BorderPane getRootLayout(){

        BorderPane borderPane = new BorderPane();

        borderPane.setTop(getMenuBar());

        borderPane.setCenter(getTabPane());

        return borderPane;
    }


    TabPane tabPane;


    // things to consider about tabs... should they be closable or are we required to create a delete button to properly dispose of them in the arraylist

    public TabPane getTabPane(){

        tabPane = new TabPane();
//        tabPane.setTabMinHeight(60);
//        tabPane.setTabMinWidth(185);
        Tab firstTab = new Tab();     // setName() depending on every slides position in the ArrayList

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
        m1_3.setOnAction( e -> savingPresentation());

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
        m4_1.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN));
        m4_1.setOnAction( e -> runPresentation());

        menu4.getItems().addAll(m4_1);

        ///////////////////////////////////////

        menuBar.getMenus().addAll(menu1, menu2, menu3, menu4);

        return menuBar;
    }




    public void newPresentation(){
        // first ask the user if they want to save first
        // if yes, then save the presentation to the database
        // either way the arraylist in the controller needs to be emptied
        // then...
        createEmptyProject();
    }


    public void createEmptyProject(){

    }


    public void addPictureToATab(File file){

        // file:/// with three slashes before the absolute file path helps avoid "MediaException: MEDIA_INACCESSIBLE"
        String imagePath = "file:///" + file.getAbsoluteFile().toString();

        try {
            Image image = new Image(imagePath);

            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            ImageView imageView = new ImageView();
            tab.setContent(imageView);

            imageView.fitWidthProperty().bind(stage.widthProperty());
            imageView.fitHeightProperty().bind(stage.heightProperty());
            imageView.setImage(image);


            // this is no good because it will keep adding images when we change the image in an already open tab
            controller.updateImageForSlideObjectInList(file);

        } catch(Exception e){
            // a pop-up telling the user that the file appears not to be an image
            // we need to properly check if the file is an image ... because?!
            // right now we will never get here
            e.printStackTrace();
        }

    }


    public void addNewTab(){
        Tab tab = new Tab();

        tabPane.getSelectionModel().select(tab);
        int correctingIndexingIssues = tabPane.getSelectionModel().getSelectedIndex() + 2;
        String title = String.valueOf(correctingIndexingIssues);
        tab.setText(title);

        tabPane.getTabs().add(tab);

    }


    public void savingPresentation(){


        for(Tab tab : tabPane.getTabs()){
            System.out.println(tab.getContent());

            // imageList.clear() + imageList.add();
        }

        controller.savePresentation();
    }


    public void runPresentation(){

        BorderPane borderPane = new BorderPane();
        Scene presentationScene = new Scene(borderPane, 400, 650);
        Stage presentationStage = new Stage();
        presentationStage.setScene(presentationScene);
        presentationStage.show();
        borderPane.setOnMouseClicked( e -> presentationStage.close());
        //presentationStage.initStyle(StageStyle.TRANSPARENT);
        // http://stackoverflow.com/questions/23503728/cannot-set-style-once-stage-has-been-set-visible


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
