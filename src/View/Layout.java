package View;

import Controller.Controller;
import Controller.TabController;
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
    TabController tabController;
    String operatingSystem;

    public void initializeLayout(Scene scene, Stage stage){
        this.scene = scene;
        this.stage = stage;
        controller = new Controller();
        tabController = new TabController(scene, stage);
        newPresentation();

        operatingSystem = System.getProperty("os.name");

        if(operatingSystem.startsWith("Windows")){
            operatingSystem = "Windows";
        } if(operatingSystem.startsWith("Mac")){
            operatingSystem = "Mac";
        }

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
        // m2_1.setOnAction( e -> method partly in controller and the rest in view that shows us all the events in a new scene and lets us create new ones, select from the list etc.);

        menu2.getItems().addAll(m2_1);

        ///////////////////////////////////////
        Menu menu3 = new Menu("Add a Slide");

        MenuItem m3_1 = new MenuItem("Picture slide");
        m3_1.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.SHORTCUT_DOWN));
        //m3_1.setOnAction( e -> controller.newSlide());
        m3_1.setOnAction( e -> tabController.addNewTab());


        menu3.getItems().addAll(m3_1);

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









}
