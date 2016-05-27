import javafx.application.Platform;
<<<<<<< HEAD
import model.DatabaseConnection;
import model.PortForwardingL;
=======
import model.DatabaseSaveAndGet;
>>>>>>> 2df434379e3bebd942940d1d3918b6c2f8129eef
import view.Layout;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import view.Login;

import javax.sound.sampled.Port;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    public void start(Stage primaryStage) throws Exception {

        Layout view = new Layout();
        Scene scene = new Scene(view.getRootLayout());

        view.initializeLayout(scene, primaryStage, view);
        double menuPlusTabHeaderHeight = view.getMenuBar().getHeight() + 20;
        double height = Screen.getPrimary().getVisualBounds().getHeight();
        double width = (Screen.getPrimary().getVisualBounds().getHeight()-menuPlusTabHeaderHeight) /1.7777;
        primaryStage.setMinHeight(height-5);
        primaryStage.setMinWidth(width);
        primaryStage.setResizable(false);

        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());

        primaryStage.getIcons().add(new Image("file:src/logo.png"));

<<<<<<< HEAD
        primaryStage.setOnCloseRequest( e -> {


            System.exit(0);
            System.out.println("killing all");
        });
=======

/*        primaryStage.setOnCloseRequest( e -> {
            e.consume();
            view.savePresentationBeforeClosingAll();
        });*/
>>>>>>> 2df434379e3bebd942940d1d3918b6c2f8129eef

        primaryStage.setScene(scene);
        primaryStage.show();
    }






}
