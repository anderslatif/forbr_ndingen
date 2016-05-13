import View.Layout;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;



public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        Layout view = new Layout();


        Scene scene = new Scene(view.getRootLayout());
        view.initializeLayout(scene, primaryStage);

        double compensatingForNotFullView = Screen.getPrimary().getVisualBounds().getHeight() * 0.125;
        primaryStage.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight());
        primaryStage.setMinWidth(Screen.getPrimary().getVisualBounds().getHeight()/(1.5 + compensatingForNotFullView));



        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());


        primaryStage.getIcons().add(new Image("file:src/logo.png"));


/*        primaryStage.setOnCloseRequest( e -> {
            e.consume();
            view.savePresentationBeforeClosingAll();
        });*/
        primaryStage.setScene(scene);
        primaryStage.show();
    }





}
