import Model.DatabaseSaveAndGet;
import Model.SlidePicture;
import Model.TabNodePicture;
import View.Layout;
import Controller.Controller;
import Controller.Util;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }







    @Override
    public void start(Stage primaryStage) throws Exception {



        Layout root = new Layout();


        Scene scene = new Scene(root.getRootLayout());

        primaryStage.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight());
        primaryStage.setMinWidth(Screen.getPrimary().getVisualBounds().getHeight()/1.5);



        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());

        root.initializeLayout(scene, primaryStage);


        //primaryStage.setOnCloseRequest( e -> closeConfirmationPopUp());
        primaryStage.setScene(scene);
        primaryStage.show();
    }





}
