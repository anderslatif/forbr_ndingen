package view;

import controller.Controller;
import controller.TabController;
import model.DatabaseSaveAndGet;
import model.Slide;
import model.SlideEvent;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Anders on 4/21/2016.
 */
public class Layout {

    Stage stage;
    Scene scene;
    Controller controller;
    TabController tabController;

    public void initializeLayout(Scene scene, Stage stage, Layout layout){
        this.scene = scene;
        this.stage = stage;
        controller = new Controller();
        tabController = new TabController(scene, stage, layout);
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
        m1_1.setOnAction( e -> {
            if(tabController.justSaved){
                newPresentation();
            } else {
                savePresentationConfirmation("newPresentation");
            }
        });

        MenuItem m1_2 = new MenuItem("_Open");
        m1_2.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
        m1_2.setOnAction( e -> pickADate("Open"));

        MenuItem m1_3 = new MenuItem("_Save");
        m1_3.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
        m1_3.setOnAction( e -> pickADate("Save"));

        menu1.getItems().addAll(m1_1, m1_2, m1_3);

        ///////////////////////////////////////
        //Menu menu2 = new Menu("Events");


        ///////////////////////////////////////
        Menu menu3 = new Menu("Add a Slide");

        MenuItem m3_1 = new MenuItem("_Picture slide");
        m3_1.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.SHORTCUT_DOWN));
        m3_1.setOnAction( e -> tabController.addPictureTab());

        MenuItem m3_2 = new MenuItem("_Bar Tilbud");
        m3_2.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN));
        m3_2.setOnAction( e -> tabController.addHappyHourTab());

        MenuItem m3_3 = new MenuItem("_Events");
        m3_3.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN));
        m3_3.setOnAction( e -> getEventOverview());

        menu3.getItems().addAll(m3_1, m3_2, m3_3);

        ///////////////////////////////////////
        Menu menu4 = new Menu("About");

        MenuItem m4_1 = new MenuItem("User Manual");
        m4_1.setOnAction( e -> showUserManual());

        MenuItem m4_2 = new MenuItem("Analyze ratio");
        m4_2.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN));
        m4_2.setOnAction( e -> analyzeRatio());


        menu4.getItems().addAll(m4_1, m4_2);

        ///////////////////////////////////////

        menuBar.getMenus().addAll(menu1, menu3, menu4);

        return menuBar;
    }



    public void setBottomLabelMessage(String message){

        Label bottomLabel = new Label();
        bottomLabel.setMaxHeight(10);
        borderPane.setBottom(bottomLabel);

        bottomLabel.setTextFill(Color.RED);
        bottomLabel.setMaxWidth(Double.MAX_VALUE);
        bottomLabel.setAlignment(Pos.CENTER);
        bottomLabel.getStyleClass().add("bottomLabel");

        bottomLabel.setText(message);

        borderPane.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                borderPane.setBottom(null);
            }
        });
    }



    public void newPresentation(){

        borderPane.setCenter(tabController.getNewTabPane());
    }



    Stage eventStage;

    public void getEventOverview(){

        eventStage = new Stage();
        Scene eventScene = new Scene(getEventBorderPane(), 1100, 450);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        eventStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 500);

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
            header.setMaxWidth(80);
            Button button1 = new Button("Delete");
            Button button2 = new Button("Insert Event " + index);

            final int indexNow = index-1; // because the index starts from 1 since the program is used by non-programmers
            // final because this is necessary in a lambda expression
            button1.setOnAction( e -> {
                controller.deleteEvent(eventCollection.get(indexNow));
                eventStage.close();
                getEventOverview();
            });
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

        HBox hBoxDateTime = new HBox();
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select the date");
        TextField timePicker = new TextField();
        timePicker.setPromptText("Event starts at... hh:mm");
        Label popUpLabel = new Label();
        popUpLabel.setFont(Font.font("bold"));
        popUpLabel.setTextFill(Color.RED);
        hBoxDateTime.getChildren().addAll(datePicker, timePicker, popUpLabel);

        TextField headerTextField = new TextField();
        headerTextField.setPromptText("Add the header...");
        vBox1.getChildren().addAll(hBoxDateTime, headerTextField);

        eventBorderPane.setTop(vBox1);

        StackPane stackPane = new StackPane();
        ImageView imageView = new ImageView();
        imageView.fitHeightProperty().bind(newEventScene.heightProperty().subtract(110));
        imageView.fitWidthProperty().bind(newEventScene.widthProperty());

        Label label = new Label("Drop image here.");
        stackPane.getChildren().addAll(imageView, label);
        eventBorderPane.setCenter(stackPane);

        //eventBorderPane.setCenter(imageView);

        VBox vBox2 = new VBox();

        TextField textTextField = new TextField();
        textTextField.setPromptText("Write some text...");
        Button doneButton = new Button("Done");

        doneButton.setOnAction( e -> {
            if(datePicker.getValue() == null){
                popUpLabel.setText("You must select a date.");
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(8000), popUpLabel);
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.play();
            } else if(imageView.getImage() == null){
                popUpLabel.setText("You must add an image.");
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(8000), popUpLabel);
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.play();
            } else {
                controller.saveNewSlideEventToDB(new SlideEvent("SlideEvent", datePicker.getValue().toString(),
                        datePicker.getValue(), timePicker.getText(), headerTextField.getText(), textTextField.getText(), eventImagePath));
                newEventStage.close();
                eventStage.close();
                getEventOverview();
            }
        });
        HBox hBox1 = new HBox();

        hBox1.getChildren().addAll(doneButton);
        vBox2.getChildren().addAll(textTextField, hBox1);
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

    public void showUserManual(){

        HBox hBox = new HBox();

        Stage userManualStage = new Stage();
        Scene userManualScene = new Scene(hBox);

        TreeItem root = new TreeItem();
        root.setExpanded(true);


        TreeItem treeItem1 = new TreeItem("Velkommen");
        TreeItem treeItem2 = new TreeItem("Event Oversigten");
        TreeItem treeItem3 = new TreeItem("Event Slides");
        TreeItem treeItem4 = new TreeItem("Picture Slides");
        TreeItem treeItem5 = new TreeItem("Bar Tilbud Slides");
        TreeItem treeItem6 = new TreeItem("Om Præsentationer");
        TreeItem treeItem7 = new TreeItem("Raspberry Pi");
        TreeItem treeItem8 = new TreeItem("Andet");


        root.getChildren().addAll(treeItem1, treeItem2, treeItem3, treeItem4, treeItem5, treeItem6, treeItem7, treeItem8);

        TreeView<String> treeView = new TreeView<>(root);
        treeView.setShowRoot(false);

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        treeView.setOnMouseClicked( e -> {
            TreeItem selectedItem = treeView.getSelectionModel().getSelectedItem();
            String display = "";

            if(selectedItem.getValue().equals("Velkommen")){
                display = "Velkommen!\n\n";
                display += "This program has been created by: \n\nAnders, Dennis and Mikkel\n\nDat15A, Førsteårsprojekt - 2016\n\nFor Forbrændingen.";
                textArea.setText(display);
            } else if(selectedItem.getValue().equals("Event Oversigten")){
                display = "Event Oversigten er separat fra oprettelse af de andre typer slides.\n\n";
                display += "Grunden til det er, at når man opretter et standard slide, så skal man arbejde på det fra starten af, ";
                display += "men events skal kunne genbruges, så længe de er relevante. Derfor kan de indsættes fra en oversigt.\n\n";
                display += "Event oversigten viser events i kronologisk rækkefølge. Ingen events før dags dato vises.\n\n";
                display += "Til sidst kan det nævnes, at man i event oversigten kan klikke \"Add a new Event\" knappen for at oprette et Event Slide.\n\n";
                display += "Vælg næste punkt i manualen for at lære mere om det.";
                textArea.setText(display);
            } else if(selectedItem.getValue().equals("Event Slides")){
                display = "Event Slides består af den dato eventet foregår, et start tidspunkt, en header, noget tekst og et billede.\n\n";
                display += "For at oprette et Event, skal man som minimum udfylde dato og indsætte et billede. ";
                display += "Hvis intet billede ønskes, så indsæt en png fil med usynlig baggrund.\n\n";
                display += "Vær dog opmærksom på, at hvis et event bliver oprettet uden komplet information skal informationen tilføjes senere i præsentationen hver eneste gang.\n";
                display += "Alternativet er at slette eventet i oversigten og genoprette det.\n\n";
                display += "Vi håber ikke, at det ikke er for besværligt. Vi anbefaler først at oprette events, når al information og billedet er klar. ";
                textArea.setText(display);
            } else if(selectedItem.getValue().equals("Picture Slides")){
                display = "Picture slides er simpelthen ethvert billede der droppes på programmet.\n\n";
                display += "Billedet vil fylde hele skærmen, så vælg et billede der har den korrekte ratio. Bredden skal være Højden divideret med 1,5.";
                textArea.setText(display);
            } else if(selectedItem.getValue().equals("Bar Tilbud Slides")){
                display = "Et bar tilbud slide består af en header, et billede og noget tekst.\n\n";
                display += "Det er muligt ikke at tilføje et billede til et bar tilbud slide og den sorte baggrund vil så vises i stedet.\n\n";
                display += "Alle slides må selvfølgelig gerne bruges til andre formål en deres navne. ";
                display += "Faktisk er Bar Tilbud slidet oplagt at bruge til andre ting med samme format.";
                textArea.setText(display);
            } else if(selectedItem.getValue().equals("Om Præsentationer")){
                display = "En præsentation er en samling af slides, der gemmes under en bestemt dato.\n\n";
                display += "Forsøger man at gemme på en dato hvor der allerede er gemt en præsentation, så overskrives den gamle præsentation. ";
                display += "Før overskrivningen kommer der en advarsel, som man kan tage stilling til.\n\n";
                display += "Det er muligt at loade en præsentation, ændre lidt på den eller ej og gemme den under en ny dato. ";
                display += "Hvis et præsentationsformat ofte går igen, så kan man belejligt gemme det under en dato man kan huske og hente det derfra.\n\n";
                display += "Det er blot vigtigt at huske på, at TV'erne kun viser den præsentation gemt under dags dato.";
                textArea.setText(display);
            } else if(selectedItem.getValue().equals("Raspberry Pi")){
                display = "Vigtige terminal linjer....";
                textArea.setText(display);
            } else if(selectedItem.getValue().equals("Andet")){
                display = "Den font der kan ses i dette program og displayes på TV'erne er Forbrændingens egen: Contribute Playtype.\n\n";
                display += "Filerne gemmes __________.\n\n";
                display += "MySQL serveren køres på Raspberry Pi'en.";
                textArea.setText(display);
            }
        });


        hBox.getChildren().addAll(treeView, textArea);

        userManualStage.setResizable(false);
        userManualStage.setScene(userManualScene);
        userManualStage.show();
    }


    public void savePresentationBeforeClosingAll(){
        if(tabController.justSaved){
            stage.close();
        } else {
            savePresentationConfirmation("closeAll");
        }
    }

    public void savePresentationConfirmation(String request){

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

        if(request.equals("newPresentation")){
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
        } else if(request.equals("closeAll")) {
                button1.setOnAction(e -> {
                    pickADate("Save");
                    savePresentationStage.close();
                    stage.close();
                });
                button2.setOnAction(e -> {
                    savePresentationStage.close();
                    stage.close();
                });
                button3.setOnAction(e -> savePresentationStage.close());
            } else if(request.equals("saveAndOpen")){
                button1.setOnAction(e -> {
                    pickADate("SaveAndOpen");
                    savePresentationStage.close();
                });
                button2.setOnAction(e -> {
                    pickADate("Open...");
                    savePresentationStage.close();
                });
                button3.setOnAction(e -> savePresentationStage.close());
        }


        savePresentationStage.setScene(savePresentationScene);
        savePresentationStage.show();
    }

    public void pickADate(String buttonText){

        if(buttonText.equals("Save") && tabController.getTabCollectionSize() == 0){
            setBottomLabelMessage("You have nothing to save.");
            return;
        }


        DatePicker datePicker = new DatePicker();

        Label warningLabel = new Label();
        warningLabel.setFont(Font.font("bold"));
        warningLabel.setTextFill(Color.RED);

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
        vBox.getChildren().addAll(warningLabel, label, datePicker, hBox);

        Stage saveStage = new Stage();
        Scene saveScene = new Scene(vBox, 190, 120);

        saveStage.setScene(saveScene);
        saveStage.show();

        if(buttonText.equals("Open") && tabController.justSaved == false){
            saveStage.close();
            savePresentationConfirmation("saveAndOpen");
        }

        // Button Actions
        cancelBut.setOnAction( e -> saveStage.close());

        datePicker.setOnAction( e -> {
            if(buttonText.equals("Save")){
                if(DatabaseSaveAndGet.checkIfSlidesAreAlreadySavedOnThisDate(datePicker.getValue().toString())){
                    warningLabel.setText("Overwrite Existing Presentation?");
                    FadeTransition fadeTransition = new FadeTransition(Duration.millis(10000), warningLabel);
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0.0);
                    fadeTransition.play();
                }
            }
        });

        saveBut.setOnAction( e -> {

            if(datePicker.getValue() != null) {

                if(buttonText.equals("Save")) {
                    tabController.savingPresentation(datePicker.getValue().toString());
                }

                if(buttonText.equals("Open") || buttonText.equals("Open...")) {
                    newPresentation();
                    ArrayList<Slide> presentation = DatabaseSaveAndGet.openPresentation(datePicker.getValue().toString());
                    tabController.openPresentation(presentation);
                }

                if(buttonText.equals("SaveAndOpen")){
                    tabController.savingPresentation(datePicker.getValue().toString());
                    pickADate("Open");
                }

                saveStage.close();

            }
        });
    }




    public void analyzeRatio(){  // this is pure nonsense

        TabPane tabPane = tabController.getTabPane();

        Tab tab = tabPane.getSelectionModel().getSelectedItem();



        if(tab.getContent() instanceof javafx.scene.image.ImageView){
            ImageView currentImageView = (ImageView) tab.getContent();

            Image image;
            if(currentImageView.getImage() == null){
                return;
            } else {
            }
            image = currentImageView.getImage();

            double ratio = image.getHeight() / image.getWidth();

            if(ratio == 1.5){
                System.out.println("perfect ratio");
            } else if(ratio < 1.5){
                ratio = ratio - 1.5;
                System.out.println("Your image is " + ratio + " too wide");
            } else if(ratio > 1.5){
                ratio = ratio - 1.5;
                System.out.println("Your image is " + ratio + " too high");
            }


        } else if(tab.getContent() instanceof javafx.scene.layout.VBox) {

            VBox vBox = (VBox) tab.getContent();

            ImageView currentImageView = null;

            for (Node node : vBox.getChildren()) {

                if(node instanceof javafx.scene.image.ImageView){
                    currentImageView = (ImageView) node;
                    Image image;
                    if(currentImageView.getImage() == null){
                        return;
                    } else {
                        image = currentImageView.getImage();
                    }


                    double prefHeight = vBox.getHeight() / 4;
                    double prefWidth = vBox.getWidth();
                    double actualHeight = image.getHeight();
                    double actualWidth = image.getWidth();
                    double differenceHeight = prefHeight - actualHeight;
                    double differenceWidth = prefWidth - actualWidth;

                    System.out.println("The height is " + differenceHeight + " off the mark.");
                    System.out.println("Your width is " + differenceWidth + " off the mark.");


                } else if (node instanceof  javafx.scene.layout.VBox){

                    VBox childVBox = (VBox) node;

                    for (Node noodle : childVBox.getChildren()){

                        if(noodle instanceof javafx.scene.image.ImageView){
                            currentImageView = (ImageView) noodle;
                            Image image;
                            if(currentImageView.getImage() == null){
                                return;
                            } else {
                                image = currentImageView.getImage();
                            }

                        }
                    }

                }
            }

        }



    }





}
