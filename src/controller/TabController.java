package controller;

import javafx.event.EventHandler;
import model.*;
import view.Layout;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import view.UserMessage;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Anders, Mikkel on 4/26/2016.
 */
public class TabController {

    private Stage stage;
    private Scene scene;
    private Layout layout;
    private TabPane tabPane;
    private Controller controller;
    private ArrayList<TabNode> tabCollection;
    public boolean justSaved = true;


    /**
     * Creates a new Controller just as we do in Layout.
     * After the program starts we check whether the database is local or on a Raspberry Pi.
     * @param scene
     * @param stage
     * @param layout
     */
    public TabController(Scene scene, Stage stage, Layout layout){
        this.scene = scene;
        this.stage = stage;
        this.layout = layout;
        tabCollection = new ArrayList<>();
        controller = new Controller();
        DatabaseConnection.chooseDatabase();
    }

    /**
     * Is used for checking whether there is anything to save, if this method returns zero.
     * @return
     */
    public int getTabCollectionSize(){

        return tabCollection.size();
    }


    public TabPane getNewTabPane(){

        tabCollection.clear();
        justSaved = true;

        tabPane = new TabPane();
        tabPane.setTabMinWidth(40);

        initializeTabController(tabPane);

        return tabPane;
    }

    public TabPane getTabPane(){
        return tabPane;
    }

    /**
     * Creates an empty tab, along with the Dragboard.
     * @param tabPane
     */
    public void initializeTabController(TabPane tabPane){

        tabPane.setTabMinHeight(20);

        Tab firstTab = new Tab();

        Label label = new Label("New Presentation!\n\nThis tab is Empty \nPlease select a slide type.");
        label.setPadding(new Insets(100, 100, 100, 100));
        label.getStyleClass().add("emptyTabLabel");

        firstTab.setContent(label);

        //TabPane indexing starts at -1. This is remedied here by adding 2 to the index.
        int compensatingForStartAtMinusOne = tabPane.getSelectionModel().getSelectedIndex() + 2;
        String title = String.valueOf(compensatingForStartAtMinusOne);
        firstTab.setText(title);
        tabPane.getTabs().addAll(firstTab);

        //Enables the dynamic numbers on the tabs.
        tabPane.getSelectionModel().selectedItemProperty().addListener( (ov, oldTab, newTab) -> {
            int tabText = 1;
            for(Tab tab : tabPane.getTabs()){
                tab.setText(String.valueOf(tabText));
                tabText++;
            }
            if(tabPane.getTabs().size() == 0){
                layout.newPresentation();
            }
        });

        //Dragboard
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

    /**
     * Firstly checks the type of file that is taken as a parameter.
     * Secondly allows dropping an image on the TabPane. Adds the image to the ImageView and sets the imagePath in the TabNode.
     *
     * @param file
     */
    public void addPictureToATab(File file){

        justSaved = false;

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

                    if(tabNodePicture.getNode() == currentImageView){
                        SlidePicture slidePicture = tabNodePicture.getSlide();
                        slidePicture.setImagePath(imagePath);
                    }
                }
            }


        } else if(tab.getContent() instanceof javafx.scene.layout.VBox) {

            VBox vBox = (VBox) tab.getContent();

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

        justSaved = false;

        Tab tab;
        if(tabCollection.size()>0){
            tab = new Tab();
            tabPane.getTabs().add(tab);
            int correctingIndexingIssues = tabPane.getTabs().size();
            String title = String.valueOf(correctingIndexingIssues);
            tab.setText(title);
        } else {
            tab = tabPane.getSelectionModel().getSelectedItem();
            tabPane.getSelectionModel().select(tab);
            int correctingIndexingIssues = tabPane.getSelectionModel().getSelectedIndex() + 1;
            String title = String.valueOf(correctingIndexingIssues);
            tab.setText(title);
        }


        tabPane.getSelectionModel().select(tab);

        ImageView imageView = new ImageView();
        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());

        tab.setContent(imageView);

        SlidePicture slidePicture = new SlidePicture();
        slidePicture.setSlideType("SlidePicture");
        TabNodePicture tabNodePicture = new TabNodePicture(imageView, slidePicture);
        tabCollection.add(tabNodePicture);

    }


    public void addPictureTab(SlidePicture slidePictureToCreate){


        Tab tab;
        if(tabCollection.size()>0){
            tab = new Tab();
            tabPane.getTabs().add(tab);
            int correctingIndexingIssues = tabPane.getTabs().size()+ 1;
            String title = String.valueOf(correctingIndexingIssues);
            tab.setText(title);
        } else {
            tab = tabPane.getSelectionModel().getSelectedItem();
            tabPane.getSelectionModel().select(tab);
            int correctingIndexingIssues = tabPane.getSelectionModel().getSelectedIndex() + 2;
            String title = String.valueOf(correctingIndexingIssues);
            tab.setText(title);
        }

        tabPane.getSelectionModel().select(tab);

        Image image;


        if(slidePictureToCreate.getImagePath().equals("null")){
            image = new Image("Empty.png");
            slidePictureToCreate.setImagePath("file:///Empty.png");
        } else {
            image = new Image(slidePictureToCreate.getImagePath());
        }

        ImageView imageView = new ImageView(image);
        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());

        tab.setContent(imageView);


        TabNodePicture tabNodePicture = new TabNodePicture(imageView, slidePictureToCreate);
        tabCollection.add(tabNodePicture);

    }



    public void addEventTab(SlideEvent slideEvent){

        justSaved = false;

        slideEvent.setSlideType("SlideEvent");

        Tab tab;
        if(tabCollection.size()>0){
            tab = new Tab();
            tabPane.getTabs().add(tab);
            int correctingIndexingIssues = tabPane.getTabs().size();
            String title = String.valueOf(correctingIndexingIssues);
            tab.setText(title);
        } else {
            tab = tabPane.getSelectionModel().getSelectedItem();
            tabPane.getSelectionModel().select(tab);
            int correctingIndexingIssues = tabPane.getSelectionModel().getSelectedIndex() + 1;
            String title = String.valueOf(correctingIndexingIssues);
            tab.setText(title);
        }

        tabPane.getSelectionModel().select(tab);

        VBox vBox = new VBox();
        vBox.getStyleClass().add("eventSlide");

        TextField headerLabel;
        if(slideEvent.getHeader().equals("null")){
            headerLabel = new TextField();
        } else {
            headerLabel = new TextField(slideEvent.getHeader());
        }
        headerLabel.setOpacity(0.8);
        headerLabel.getStyleClass().add("header");
        headerLabel.textProperty().addListener( e -> {
            slideEvent.setHeader(headerLabel.getText());
            justSaved = false;
        });

        TextField startTimeLabel;
        if(slideEvent.getStartTime().equals("null")){
            startTimeLabel = new TextField();
        } else {
            startTimeLabel = new TextField(slideEvent.getStartTime());
        }
        startTimeLabel.setOpacity(0.8);
        startTimeLabel.getStyleClass().add("text_area");
        startTimeLabel.textProperty().addListener( e -> {
            slideEvent.setStartTime(startTimeLabel.getText());
            justSaved = false;
        });

        Image image;
        if (slideEvent.getImagePath().equals("null")){
            image = new Image("Empty.png");
        } else {
            image = new Image(slideEvent.getImagePath());
        }

        ImageView imageView = new ImageView(image);
        imageView.fitHeightProperty().bind(vBox.heightProperty().divide(3));
        imageView.fitWidthProperty().bind(vBox.widthProperty());

        VBox filler1 = new VBox();
        filler1.setPadding(new Insets(20, 0, 0, 0));
        filler1.getChildren().addAll(headerLabel, startTimeLabel, imageView);


        VBox filler2 = new VBox();
        filler2.setPadding(new Insets(40, 0, 0, 0));

        TextArea textTextArea;
        if(slideEvent.getText().equals("null")){
            textTextArea = new TextArea();
        } else {
            textTextArea = new TextArea(slideEvent.getText());
        }
        textTextArea.setMaxWidth(scene.getWidth());
        textTextArea.setWrapText(true);
        textTextArea.getStyleClass().add("text_area");
        textTextArea.setOpacity(0.8);
        textTextArea.textProperty().addListener( e -> {
            slideEvent.setText(textTextArea.getText());
            justSaved = false;
        });

        filler2.getChildren().add(textTextArea);


        vBox.getChildren().addAll(filler1, filler2);

        tab.setContent(vBox);


        TabNodeEvent tabNodeEvent = new TabNodeEvent(vBox, imageView, slideEvent);
        tabCollection.add(tabNodeEvent);

    }


    public void addHappyHourTab(){

        justSaved = false;

        Tab tab;
        if(tabCollection.size()>0){
            tab = new Tab();
            tabPane.getTabs().add(tab);
            int correctingIndexingIssues = tabPane.getTabs().size()+ 1;
            String title = String.valueOf(correctingIndexingIssues);
            tab.setText(title);
        } else {
            tab = tabPane.getSelectionModel().getSelectedItem();
            tabPane.getSelectionModel().select(tab);
            int correctingIndexingIssues = tabPane.getSelectionModel().getSelectedIndex() + 2;
            String title = String.valueOf(correctingIndexingIssues);
            tab.setText(title);
        }

        tabPane.getSelectionModel().select(tab);
        int correctingIndexingIssues = tabPane.getSelectionModel().getSelectedIndex() + 1;
        String title = String.valueOf(correctingIndexingIssues);
        tab.setText(title);


        VBox vBox = new VBox();
        vBox.getStyleClass().add("happyHourSlide");

        Image image = new Image("dropimage.png");

        TextField headerTextField = new TextField();
        headerTextField.getStyleClass().add("header");
        headerTextField.setPromptText("Type the header here...");
        headerTextField.setOpacity(0.8);

        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.fitHeightProperty().bind(vBox.heightProperty().divide(3));
        imageView.fitWidthProperty().bind(scene.widthProperty());

        TextArea textTextArea =  new TextArea();
        textTextArea.setPromptText("Type more text here...");
        textTextArea.setOpacity(0.8);
        textTextArea.getStyleClass().add("text_area");

        SlideHappyHour slideHappyHour = new SlideHappyHour();
        slideHappyHour.setSlideType("SlideHappyHour");
        headerTextField.textProperty().addListener( e -> {
            slideHappyHour.setHeader(headerTextField.getText());
            justSaved = false;
        });
        textTextArea.textProperty().addListener( e -> {
            slideHappyHour.setText(textTextArea.getText());
            justSaved = false;
        });


        vBox.getChildren().addAll(headerTextField, imageView, textTextArea);



        TabNodeHappyHour tabNodeHappyHour = new TabNodeHappyHour(vBox, imageView, slideHappyHour);
        tabCollection.add(tabNodeHappyHour);

        tab.setContent(vBox);

    }


    public void addHappyHourTab(SlideHappyHour happyHourSlide){

        Tab tab;
        if(tabCollection.size()>0){
            tab = new Tab();
            tabPane.getTabs().add(tab);
            int correctingIndexingIssues = tabPane.getTabs().size()+ 1;
            String title = String.valueOf(correctingIndexingIssues);
            tab.setText(title);
        } else {
            tab = tabPane.getSelectionModel().getSelectedItem();
            tabPane.getSelectionModel().select(tab);
            int correctingIndexingIssues = tabPane.getSelectionModel().getSelectedIndex() + 2;
            String title = String.valueOf(correctingIndexingIssues);
            tab.setText(title);
        }

        tabPane.getSelectionModel().select(tab);
        int correctingIndexingIssues = tabPane.getSelectionModel().getSelectedIndex() + 1;
        String title = String.valueOf(correctingIndexingIssues);
        tab.setText(title);


        VBox vBox = new VBox();
        vBox.getStyleClass().add("happyHourSlide");

        TextField headerTextField = new TextField();
        if (happyHourSlide.getHeader().equals("null") || happyHourSlide.getHeader() == null) {
            headerTextField.setPromptText("Type the header here...");
        } else {
            headerTextField.setText(happyHourSlide.getHeader());
        }
        headerTextField.getStyleClass().add("header");
        headerTextField.setOpacity(0.8);


        Image image;

        if (happyHourSlide.getImagePath().equals("") || happyHourSlide.getImagePath().equals("null") || happyHourSlide.getImagePath() == null){
            image = new Image("Empty.png");
            happyHourSlide.setImagePath("file:///Empty.png");
        } else {
            //path ok her
            image = new Image(happyHourSlide.getImagePath());
        }

        ImageView imageView = new ImageView(image);

        imageView.fitHeightProperty().bind(vBox.heightProperty().divide(3));
        imageView.fitWidthProperty().bind(vBox.widthProperty().subtract(10));

        TextArea textTextArea;
        if (happyHourSlide.getText().equals("null") || happyHourSlide.getText() == null){
            textTextArea = new TextArea();
            textTextArea.setPromptText("Type more text here...");
        } else {
            textTextArea = new TextArea(happyHourSlide.getText());
        }
        textTextArea.setOpacity(0.8);
        textTextArea.getStyleClass().add("text_area");



        headerTextField.textProperty().addListener( e -> {
            happyHourSlide.setHeader(headerTextField.getText());
            justSaved = false;
        });
        textTextArea.textProperty().addListener( e -> {
            happyHourSlide.setText(textTextArea.getText());
            justSaved = false;
        });


        vBox.getChildren().addAll(headerTextField, imageView, textTextArea);

        TabNodeHappyHour tabNodeHappyHour = new TabNodeHappyHour(vBox, imageView, happyHourSlide);
        System.out.println("tabnodes path "+tabNodeHappyHour.getSlide().getImagePath());

        tabCollection.add(tabNodeHappyHour);

        tab.setContent(vBox);

    }


    /**
     * Gets all the TabNodes that correspond to the open tabs.
     * @param chosenDate
     * @param view
     */
    public void savingPresentation(String chosenDate, Layout view){


        UserMessage.setBottomLabelMessage("Presentation has been saved.", "Info");
        justSaved = true;

        ArrayList<TabNode> presentation = new ArrayList();

        for(Tab tab : tabPane.getTabs()){

            for (TabNode tabNode : tabCollection){

                if(tab.getContent() == tabNode.getNode()){

                    presentation.add(tabNode);
                }

            }
        }
        controller.savePresentation(presentation, chosenDate, view);
    }

    /**
     * Creates tabs that correspond with the Slides from the database.
     * @param presentation
     */
    public void openPresentation(ArrayList<Slide> presentation){

        tabCollection.clear();

        for(Slide slide : presentation){

            switch (slide.getSlideType()){

                case "SlideEvent":
                    addEventTab((SlideEvent) slide);
                    break;
                case "SlidePicture":
                    addPictureTab((SlidePicture) slide);
                    break;
                case "SlideHappyHour":
                    addHappyHourTab((SlideHappyHour) slide);
                    break;
                default:
                    UserMessage.setBottomLabelMessage("Error while opening one of the slides.", "Error");
            }
        }
        justSaved = true;
    }



}
