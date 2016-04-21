import View.Layout;
import Controller.Controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }







    @Override
    public void start(Stage primaryStage) throws Exception {

        Layout root = new Layout();

        Scene scene = new Scene(root.getRootLayout(), 800, 475);

        root.initializeLayout(scene, primaryStage);


/*        scene.setOnDragOver( e -> {
            Dragboard db = e.getDragboard();
            if (db.hasFiles()) {
                e.acceptTransferModes(TransferMode.ANY);
            } else {
                e.consume();
            }
        });


        scene.setOnDragDropped( e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                for (File file : db.getFiles()) {
                    root.addPictureToAnchorPane(file);
                }
            }
            e.setDropCompleted(success);
            e.consume();
        });*/



        //primaryStage.setOnCloseRequest( e -> confirmationPopUp());
        primaryStage.setScene(scene);
        primaryStage.show();
    }





}
