import Model.SlidePicture;
import Model.TabNodePicture;
import View.Layout;
import Controller.Controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.time.LocalDate;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }







    @Override
    public void start(Stage primaryStage) throws Exception {

        SlidePicture sp1 = new SlidePicture(LocalDate.now(), "dokumenter/billeder/test.jpg");
        ImageView iv1 = new ImageView();

        TabNodePicture tabNP = new TabNodePicture(iv1, sp1);

        Layout root = new Layout();

        Scene scene = new Scene(root.getRootLayout(), 800, 475);

        root.initializeLayout(scene, primaryStage);


        //primaryStage.setOnCloseRequest( e -> closeConfirmationPopUp());
        primaryStage.setScene(scene);
        primaryStage.show();
    }





}
