package View;

import Controller.Controller;
import Controller.TabController;
import Model.DatabaseSaveAndGet;
import Model.SlideEvent;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
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
    TabController tabController;

    public void initializeLayout(Scene scene, Stage stage){
        this.scene = scene;
        this.stage = stage;
        controller = new Controller();
        tabController = new TabController(scene, stage);
        newPresentation();


    }


    BorderPane borderPane;

    public BorderPane getRootLayout(){

        borderPane = new BorderPane();

        borderPane.setTop(getMenuBar());

        return borderPane;
    }




    public MenuBar getMenuBar(){

        MenuBar menuBar = new MenuBar();

        ///////////////////////////////////////
        Menu menu1 = new Menu("File");

        MenuItem m1_1 = new MenuItem("_New Presentation");
        m1_1.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
        m1_1.setOnAction( e -> newPresentation());

        MenuItem m1_2 = new MenuItem("_Open");
        m1_2.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
        m1_2.setOnAction( e -> controller.openPresentation());

        MenuItem m1_3 = new MenuItem("_Save");
        m1_3.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
        m1_3.setOnAction( e -> tabController.savingPresentation());

        menu1.getItems().addAll(m1_1, m1_2, m1_3);

        ///////////////////////////////////////
        Menu menu2 = new Menu("Events");

        MenuItem m2_1 = new MenuItem("_Events");

        m2_1.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN));
        m2_1.setOnAction( e -> getEventOverview());


        menu2.getItems().addAll(m2_1);

        ///////////////////////////////////////
        Menu menu3 = new Menu("Add a Slide");

        MenuItem m3_1 = new MenuItem("_Picture slide");
        m3_1.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.SHORTCUT_DOWN));
        m3_1.setOnAction( e -> tabController.addPictureSlide());

        MenuItem m3_2 = new MenuItem("_Bar Tilbud");
        m3_2.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN));
        m3_2.setOnAction( e -> tabController.addHappyHourSlide());


        menu3.getItems().addAll(m3_1, m3_2);

        ///////////////////////////////////////
        Menu menu4 = new Menu("Start Presentation");

        MenuItem m4_1 = new MenuItem("_Go");
        m4_1.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.SHORTCUT_DOWN));
        m4_1.setOnAction( e -> tabController.runPresentation());

        menu4.getItems().addAll(m4_1);

        ///////////////////////////////////////

        menuBar.getMenus().addAll(menu1, menu2, menu3, menu4);

        return menuBar;
    }




    public void newPresentation(){

        borderPane.setCenter(tabController.getTabPane());
    }






    Stage eventStage;

    public void getEventOverview(){

        eventStage = new Stage();
        Scene eventScene = new Scene(getEventBorderPane(), 800, 475);

        eventStage.setScene(eventScene);
        eventStage.show();
    }


    public BorderPane getEventBorderPane(){

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));


        ArrayList<SlideEvent> eventCollection = DatabaseSaveAndGet.loadAllEvents();

        int columnCount = 0;
        int rowCount = 0;
        int index = 1;


        for(SlideEvent slideEvent : eventCollection){

            Label date  = new Label(slideEvent.getDate().toString());
            date.setPadding(new Insets(0, 10, 0, 40));
            Label header = new Label(slideEvent.getHeader());
            header.setPadding(new Insets(0, 10, 0, 0));
            Button button = new Button("Insert Event " + index);

            final int indexNow = index-1; // because the index starts from 1 since the program is used by non-programmers
            // final because this is necessary in a lambda expression
            button.setOnAction( e -> tabController.addEventTab(eventCollection.get(indexNow)));


            gridPane.add(date, columnCount, rowCount);
            columnCount++;
            gridPane.add(header, columnCount, rowCount);
            columnCount++;
            gridPane.add(button, columnCount, rowCount);
            columnCount++;

            index++;
            if(columnCount == 9){
                columnCount = 0;
                rowCount++;
            }

        }

        BorderPane eventOverViewBorderPane = new BorderPane();
        eventOverViewBorderPane.setCenter(gridPane);

        Button addEventButton = new Button("Add a New Event");
        addEventButton.setPadding(new Insets(10, 10, 10, 10));
        addEventButton.setOnAction( e -> createNewEvent());
        eventOverViewBorderPane.setBottom(addEventButton);

        return eventOverViewBorderPane;
    }




    String eventImagePath;

    public void createNewEvent(){

        BorderPane eventBorderPane = new BorderPane();

        Stage newEventStage = new Stage();
        Scene newEventScene = new Scene(eventBorderPane, 800, 475);


        VBox vBox1 = new VBox();
        DatePicker datePicker = new DatePicker();
        TextField headerTextField = new TextField();
        vBox1.getChildren().addAll(datePicker, headerTextField);

        eventBorderPane.setTop(vBox1);

        ImageView imageView = new ImageView();
        eventBorderPane.setCenter(imageView);

        VBox vBox2 = new VBox();
        TextField textTextField = new TextField();
        Button doneButton = new Button("Done");

        doneButton.setOnAction( e -> {
            controller.saveNewSlideEventToDB(new SlideEvent(datePicker.getValue().toString(), headerTextField.getText(), textTextField.getText(), eventImagePath));
            newEventStage.close();
        });

        vBox2.getChildren().addAll(textTextField, doneButton);
        eventBorderPane.setBottom(vBox2);



        newEventScene.setOnDragOver( e -> {
            Dragboard db = e.getDragboard();
            if (db.hasFiles()) {
                e.acceptTransferModes(TransferMode.ANY);
            } else {
                e.consume();
            }
        });


        newEventScene.setOnDragDropped( e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                for (File file : db.getFiles()) {
                    eventImagePath = "file:///" + file.getAbsoluteFile().toString();
                    imageView.setImage(new Image(eventImagePath));
                }
            }
            e.setDropCompleted(success);
            e.consume();
        });






        newEventStage.setScene(newEventScene);
        newEventStage.show();
    }








}
