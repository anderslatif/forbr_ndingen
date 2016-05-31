package view;

import controller.Controller;
import controller.TabController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.embed.swing.SwingNode;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.util.Callback;
import model.DatabaseSaveAndGet;
import model.PortForwardingL;
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

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Anders, Dennis, Mikkel on 4/21/2016.
 */
public class Layout {

    private Stage stage;
    private Scene scene;
    private Controller controller;
    private TabController tabController;
    private Login login = new Login();
    public static boolean newPresentation = false;

    /**
     * Transfers the variables scene and stage from Main and initializes the Controller and TabController objects.
     * @param scene
     * @param stage
     * @param layout
     */
    public void initializeLayout(Scene scene, Stage stage, Layout layout){
        this.scene = scene;
        this.stage = stage;
        controller = new Controller();
        tabController = new TabController(scene, stage, layout);
        newPresentation();
        UserMessage.borderPane = borderPane;
    }


    BorderPane borderPane;

    /**
     * Builds the BorderPane that the main window of the program runs on.
     * @return BorderPane
     */
    public BorderPane getRootLayout(){

        borderPane = new BorderPane();

        borderPane.setTop(getMenuBar());

        return borderPane;
    }


    //used to disable and enable MenuItems when logging in and out
    public final BooleanProperty loginState = new SimpleBooleanProperty();

    /**
     * Is called at program launch and initializes the MenuBar.
     * @return
     */
    public MenuBar getMenuBar(){
        MenuBar menuBar = new MenuBar();


        ///////////////////////////////////////
        Menu menu1 = new Menu("_File");

        MenuItem m1_1 = new MenuItem("_New Presentation");
        m1_1.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
        m1_1.setOnAction( e -> {
            if(tabController.justSaved){
                newPresentation();
            } else {
                savePresentationConfirmation("newPresentation");
            }
        });
        m1_1.disableProperty().bind(loginState); //binds the booleanProperty for disabled to loginState

        MenuItem m1_2 = new MenuItem("Open");
        m1_2.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
        m1_2.setOnAction( e -> pickADate("Open"));
        m1_2.disableProperty().bind(loginState);

        MenuItem m1_3 = new MenuItem("_Save");
        m1_3.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
        m1_3.setOnAction( e -> pickADate("Save"));
        m1_3.disableProperty().bind(loginState);

        menu1.getItems().addAll(m1_1, m1_2, m1_3);

        ///////////////////////////////////////
        Menu menu2 = new Menu("_Add a Slide");

        MenuItem m2_1 = new MenuItem("Picture slide");
        m2_1.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.SHORTCUT_DOWN));
        m2_1.setOnAction( e -> tabController.addPictureTab());
        m2_1.disableProperty().bind(loginState);

        MenuItem m2_2 = new MenuItem("Bar Slide");
        m2_2.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN));
        m2_2.setOnAction( e -> tabController.addHappyHourTab());
        m2_2.disableProperty().bind(loginState);

        MenuItem m2_3 = new MenuItem("Events");
        m2_3.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN));
        m2_3.setOnAction( e -> getEventOverview());
        m2_3.disableProperty().bind(loginState);


        menu2.getItems().addAll(m2_1, m2_2, m2_3);

        ///////////////////////////////////////
        Menu menu3 = new Menu("_About");

        MenuItem m3_1 = new MenuItem("User Manual");
        m3_1.setOnAction( e -> showUserManual());

        MenuItem m3_2 = new MenuItem("Analyze ratio");
        m3_2.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN));
        m3_2.setOnAction( e -> analyzeRatio());


        menu3.getItems().addAll(m3_1, m3_2);

        ///////////////////////////////////////
        Menu menu4 = new Menu("User");


        MenuItem m4_1 = new MenuItem("Log in");
        m4_1.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN));
        m4_1.setOnAction( event -> login.userStage(this, "login"));

        MenuItem m4_2 = new MenuItem("_Lock");
        m4_2.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN));
        m4_2.setOnAction(event -> loginState.setValue(true));

        MenuItem m4_3 = new MenuItem("_Change username and password");
        m4_3.setOnAction(event -> login.userStage(this, "edit"));
        m4_3.disableProperty().bind(loginState);

        menu4.getItems().addAll(m4_1, m4_2, m4_3);
        /////////////////////////////////////////

        menuBar.getMenus().addAll(menu1, menu2, menu3, menu4);

        //loginState.setValue(true);

        return menuBar;
    }


    public void newPresentation(){

        borderPane.setCenter(tabController.getNewTabPane());
    }



    Stage eventStage;

    /**
     * The event overview shows all the events and enables you to insert them into your presentation
     */
    public void getEventOverview(){

        eventStage = new Stage();
        Scene eventScene = new Scene(getEventBorderPane(), 900, 250);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        eventStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 300);

        eventStage.setScene(eventScene);
        eventStage.show();
    }

    /**
     * Gets the event from the database and adds them to a grid. Is called in getEventOverview() above.
     * @return
     */
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
        TreeItem treeItem2 = new TreeItem("Eventoversigten");
        TreeItem treeItem3 = new TreeItem("Event Slides");
        TreeItem treeItem4 = new TreeItem("Picture Slides");
        TreeItem treeItem5 = new TreeItem("Bar Tilbud Slides");
        TreeItem treeItem6 = new TreeItem("Ratio");
        TreeItem treeItem7 = new TreeItem("Om Præsentationer");
        TreeItem treeItem8 = new TreeItem("Raspberry Pi");
        TreeItem treeItem9 = new TreeItem("Andet");


        root.getChildren().addAll(treeItem1, treeItem2, treeItem3, treeItem4, treeItem5, treeItem6, treeItem7, treeItem8, treeItem9);

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
                display += "Vi håber ikke, at det er for besværligt. Vi anbefaler først at oprette events, når al information og billedet er klar. ";
                textArea.setText(display);
            } else if(selectedItem.getValue().equals("Picture Slides")){
                display = "Picture slides er simpelthen ethvert billede der droppes på programmet.\n\n";
                display += "Billedet vil fylde hele skærmen, så vælg et billede der har den korrekte ratio. Bredden skal være Højden divideret med 1,5.";
                textArea.setText(display);
            } else if(selectedItem.getValue().equals("Bar Tilbud Slides")){
                display = "Et bartilbud slide består af en header, et billede og noget tekst.\n\n";
                display += "Det er muligt ikke at tilføje et billede til et bar tilbud slide og den sorte baggrund vil så vises i stedet.\n\n";
                display += "Alle slides må selvfølgelig gerne bruges til andre formål end deres navne. ";
                display += "Faktisk er Bartilbud slidet oplagt at bruge til andre ting med samme format.";
                textArea.setText(display);
            } else if(selectedItem.getValue().equals("Ratio")){
                display = "Display programmet forventes at køre på aspect ratio 16:9. \n\nPicture slides billedet bør opfylde dette størrelsesforhold. " +
                        "For Event slides og Bar slides burde de være 3 gange mindre høje: dvs. (16 divideret med 3) divideret med 9.\n\n\n" +
                        "Det er ret besværligt at holde styr på. Derfor er det muligt at læggge et billede på og vælge Analyze ratio i menupunktet About.\n\n" +
                        "Analyze ratio vil fortælle præcist hvor mange pixels i den ene eller den anden retning der er for meget.";
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

    /**
     * Checks if the presentation is saved. Ends with closing the program.
     */
    public void savePresentationBeforeClosingAll(){
        if(tabController.justSaved){
            System.exit(0);
        } else {
            savePresentationConfirmation("closeAll");
        }
    }

    /**
     * Ensures that the user does not accidentally close an open presentation without saving.
     * @param request
     */
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

                newPresentation = true;
                pickADate("Save");
                savePresentationStage.close();


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
                });
                button2.setOnAction(e -> {
                    savePresentationStage.close();
                    System.exit(0);
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

    /**
     * Lets the user pick a date, which is used when saving and loading presentations.
     * @param buttonText
     */
    public void pickADate(String buttonText){

        if(buttonText.equals("Save") && tabController.getTabCollectionSize() == 0){
            UserMessage.setBottomLabelMessage("You have nothing to save.", "Error");
            return;
        }

        DatePicker datePicker = new DatePicker();

        // We get the dates of all the existing presentations and colorcode them differently
        // Then we colorcode today's date differently if there is a presentation made for today
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(DatePicker param) {
                        return new DateCell(){
                            @Override
                            public void updateItem(LocalDate item, boolean empty){
                                for(LocalDate localDate : DatabaseSaveAndGet.getPresentationDates()){
                                    super.updateItem(item, empty);
                                    if(item.equals(localDate)){
                                        if(localDate.equals(LocalDate.now())){
                                            setStyle("-fx-background-color: #007a00;");
                                        } else {
                                            setStyle("-fx-background-color: #00cc00;");
                                        }
                                    }
                                }
                            }
                        };
                    }
                };
        datePicker.setDayCellFactory(dayCellFactory);

        Label warningLabel = new Label();
        warningLabel.setFont(Font.font("bold"));
        warningLabel.setTextFill(Color.RED);

        Label label = new Label("Choose a date:");

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
        saveStage.initModality(Modality.WINDOW_MODAL);
        saveStage.initOwner(stage.getScene().getWindow());
        saveStage.show();

        if(buttonText.equals("Open") && tabController.justSaved == false){
            saveStage.close();
            savePresentationConfirmation("saveAndOpen");
        }

        // Button Actions
        cancelBut.setOnAction( e -> {
            PortForwardingL.closeConnection();
            saveStage.close();
        });

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
                    tabController.savingPresentation(datePicker.getValue().toString(), this);
                }

                if(buttonText.equals("Open") || buttonText.equals("Open...")) {
                    newPresentation();
                    ArrayList<Slide> presentation = DatabaseSaveAndGet.openPresentation(datePicker.getValue().toString());

                    tabController.openPresentation(presentation);
                }

                if(buttonText.equals("SaveAndOpen")){
                    tabController.savingPresentation(datePicker.getValue().toString(), this);
                    pickADate("Open");
                }

                saveStage.close();

            } else {
                    warningLabel.setText("Please use the calendar\nto select a valid date");
                    FadeTransition fadeTransition = new FadeTransition(Duration.millis(10000), warningLabel);
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0.0);
                    fadeTransition.play();

            }
        });
    }


    /**
     * Analyzes if the chosen picture has the right size ratio to be shown on an info screen.
     */
    public void analyzeRatio(){

        TabPane tabPane = tabController.getTabPane();

        Tab tab = tabPane.getSelectionModel().getSelectedItem();



        if(tab.getContent() instanceof javafx.scene.image.ImageView){
            ImageView currentImageView = (ImageView) tab.getContent();

            Image image;
            if(currentImageView.getImage() == null){
                return;
            } else {
                image = currentImageView.getImage();
            }

            float width = (float) image.getWidth();
            float height = (float) image.getHeight();
            float ratio = height / width;
            float perfectRatio = 16/9f;


            if(ratio > 1.777777777777776 && ratio < 1.7777777777778){
                UserMessage.setBottomLabelMessage("Perfect Ratio!", "Info");
            } else if(ratio < 1.77777){
                float difference = width - (height / perfectRatio);
                if(difference <= 1){
                    UserMessage.setBottomLabelMessage("Your image is " + difference + " pixel too wide.", "Info");
                } else {
                    UserMessage.setBottomLabelMessage("Your image is " + difference + " pixels too wide.", "Info");
                }
            } else if(ratio > 1.77777){
                float difference = height - (width * perfectRatio);
                if(difference <= 1){
                    UserMessage.setBottomLabelMessage("Your image is " + difference + " pixel too high.", "Info");
                } else {
                    UserMessage.setBottomLabelMessage("Your image is " + difference + " pixels too high.", "Info");
                }
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


                    float width = (float) image.getWidth();
                    float height = (float) image.getHeight();
                    float ratio = height / width;
                    float theThirdOfHeight = 16/3f;
                    float perfectRatio = theThirdOfHeight/9f;


                    if(ratio > 0.590 && ratio < 0.599){
                        UserMessage.setBottomLabelMessage("Perfect ratio.", "Info");
                    } else if(ratio < 0.599){
                        float difference = width - (height / perfectRatio);
                        if(difference <= 1){
                            UserMessage.setBottomLabelMessage("Your image is " + difference + " pixel too wide.", "Info");
                        } else {
                            UserMessage.setBottomLabelMessage("Your image is " + difference + " pixels too wide.", "Info");
                        }
                    } else if(ratio > 0.599){
                        float difference = height - (width * perfectRatio);
                        if(difference <= 1){
                            UserMessage.setBottomLabelMessage("Your image is " + difference + " pixel too high.", "Info");
                        } else {
                            UserMessage.setBottomLabelMessage("Your image is " + difference + " pixels too high.", "Info");
                        }
                    }


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

                            float width = (float) image.getWidth();
                            float height = (float) image.getHeight();
                            float ratio = height / width;
                            float theThirdOfHeight = 16/3f;
                            float perfectRatio = theThirdOfHeight/9f;


                            if(ratio > 0.590 && ratio < 0.599){
                                UserMessage.setBottomLabelMessage("Perfect ratio.", "Info");
                            } else if(ratio < 0.599){
                                float difference = width - (height / perfectRatio);
                                if(difference <= 1){
                                    UserMessage.setBottomLabelMessage("Your image is " + difference + " pixel too wide.", "Info");
                                } else {
                                    UserMessage.setBottomLabelMessage("Your image is " + difference + " pixels too wide.", "Info");
                                }
                            } else if(ratio > 0.599){
                                float difference = height - (width * perfectRatio);
                                if(difference <= 1){
                                    UserMessage.setBottomLabelMessage("Your image is " + difference + " pixel too high.", "Info");
                                } else {
                                    UserMessage.setBottomLabelMessage("Your image is " + difference + " pixels too high.", "Info");
                                }
                            }

                        }
                    }

                }
            }

        }

    }

}
