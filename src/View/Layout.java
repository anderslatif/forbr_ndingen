package View;

import Controller.Controller;
import Controller.TabController;
import Model.DatabaseSaveAndGet;
import Model.Slide;
import Model.SlideEvent;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.time.LocalDate;
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
        m1_1.setOnAction( e -> savePresentationConfirmation());

        MenuItem m1_2 = new MenuItem("_Open");
        m1_2.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
        m1_2.setOnAction( e -> pickADate("Open"));

        MenuItem m1_3 = new MenuItem("_Save");
        m1_3.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
        m1_3.setOnAction( e -> pickADate("Save"));

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
        m3_1.setOnAction( e -> tabController.addPictureTab());

        MenuItem m3_2 = new MenuItem("_Bar Tilbud");
        m3_2.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN));
        m3_2.setOnAction( e -> tabController.addHappyHourTab());


        menu3.getItems().addAll(m3_1, m3_2);

        ///////////////////////////////////////
        Menu menu4 = new Menu("About");

        MenuItem m4_1 = new MenuItem("About");
        m4_1.setOnAction( e -> showAbout());


        menu4.getItems().addAll(m4_1);

        ///////////////////////////////////////

        menuBar.getMenus().addAll(menu1, menu2, menu3, menu4);

        return menuBar;
    }




    public void newPresentation(){

        borderPane.setCenter(tabController.getTabPane());
    }


    public void openLoadedPresentation(ArrayList<Slide> presentation){

        borderPane.setCenter(tabController.getTabPane());
        // her skal nok loopes igennem ArrayList presentation for at sortere dem og indlæse dem som TabNodes

    }


    Stage eventStage;

    public void getEventOverview(){

        eventStage = new Stage();
        Scene eventScene = new Scene(getEventBorderPane());

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

            if (columnCount>0){     // the spacing will be larger if the event isn't the first in the row
                date.setPadding(new Insets(0, 20, 0, 50));
            } else{
                date.setPadding(new Insets(0, 20, 0, 20));
            }
            Label header = new Label(slideEvent.getHeader());
            header.setPadding(new Insets(0, 10, 0, 0));
            Button button1 = new Button("Delete");
            Button button2 = new Button("Insert Event " + index);

            final int indexNow = index-1; // because the index starts from 1 since the program is used by non-programmers
            // final because this is necessary in a lambda expression
            //todo button1.setOnAction( e -> controller.deleteEvent());
            button2.setOnAction( e -> tabController.addEventTab(eventCollection.get(indexNow)));


            gridPane.add(date, columnCount, rowCount);
            columnCount++;
            gridPane.add(header, columnCount, rowCount);
            columnCount++;
            gridPane.add(button1, columnCount, rowCount);
            columnCount++;
            gridPane.add(button2, columnCount, rowCount);
            columnCount++;


            index++;
            if(columnCount == 12){
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
            controller.saveNewSlideEventToDB(new SlideEvent("SlideEvent", datePicker.getValue().toString(), headerTextField.getText(), textTextField.getText(), eventImagePath));
            newEventStage.close();
            eventStage.close();
            getEventOverview();
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


    public void showAbout(){

        VBox vBox = new VBox();

        Stage aboutStage = new Stage();
        Scene aboutScene = new Scene(vBox);

        Label label = new Label("This program has been created by: \n\nAnders, Dennis and Mikkel\n\nDat15A, Førsteårsprojekt - 2016");
        label.setPadding(new Insets(20, 20, 20, 20));
        vBox.getChildren().add(label);

        aboutStage.setScene(aboutScene);
        aboutStage.show();
    }


    public void savePresentationConfirmation(){

        GridPane gridPane = new GridPane();
        Stage savePresentationStage = new Stage();
        Scene savePresentationScene = new Scene(gridPane);

        Label label = new Label("Do you want to save your current presentation?");
        label.setStyle("-fx-font-size: 16px");
        label.setPadding(new Insets(20, 30, 10, 45));
        gridPane.add(label, 0, 0);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(20, 20, 20, 20));
        Button button1 = new Button("Yes");
        Button button2 = new Button("No");
        hBox.getChildren().addAll(button1, button2);

        gridPane.add(hBox, 0, 1);

        hBox.setHgrow(button1, Priority.ALWAYS);
        hBox.setHgrow(button2, Priority.ALWAYS);
        button1.setMaxWidth(Double.MAX_VALUE);
        button2.setMaxWidth(Double.MAX_VALUE);
        hBox.setPrefWidth(400);

        Button button3 = new Button("Cancel");
        button3.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(button3, 0, 2);

        button1.setOnAction( e -> {
            pickADate("Save");
            savePresentationStage.close();
            newPresentation();
        });
        button2.setOnAction( e -> {
            savePresentationStage.close();
            newPresentation();
        });
        button3.setOnAction( e -> savePresentationStage.close());

        savePresentationStage.setScene(savePresentationScene);
        savePresentationStage.show();
    }

    public void pickADate(String buttonText){

        DatePicker datePicker = new DatePicker();

        Label label = new Label("Choose date:");

        Button saveBut = new Button();
        saveBut.setText(buttonText);
        saveBut.setMinWidth(85);
        Button cancelBut = new Button("Cancel");
        cancelBut.setMinWidth(85);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(0,5,0,5));
        hBox.getChildren().addAll(saveBut, cancelBut);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(5,5,5,5));
        vBox.getChildren().addAll(label, datePicker, hBox);

        Stage saveStage = new Stage();
        Scene saveScene = new Scene(vBox, 190, 80);

        saveStage.setScene(saveScene);
        saveStage.show();

        // Button Actions
        cancelBut.setOnAction( e -> saveStage.close());

        saveBut.setOnAction( e -> {

            if(datePicker.getValue() != null) {

                if (buttonText.equals("Save")) {

                    tabController.savingPresentation(datePicker.getValue().toString());
                }

                if (buttonText.equals("Open")) {

                    ArrayList<Slide> presentation = DatabaseSaveAndGet.openPresentation(datePicker.getValue().toString());
                    openLoadedPresentation(presentation);
                }

                saveStage.close();

            }
        });
    }




}
