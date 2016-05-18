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

<<<<<<< HEAD

=======
>>>>>>> 29c64c33afd6f6e0a600051db2ad5a7ec688869e
    public void start(Stage primaryStage) throws Exception {

        Layout view = new Layout();

        Scene scene = new Scene(view.getRootLayout());
        view.initializeLayout(scene, primaryStage);


        double menuPlusTabHeaderHeight = view.getMenuBar().getHeight()+20;

        double height = Screen.getPrimary().getVisualBounds().getHeight();
        double width = (Screen.getPrimary().getVisualBounds().getHeight()-menuPlusTabHeaderHeight) /1.7777;

        primaryStage.setMinHeight(height);
        primaryStage.setMinWidth(width);



        System.out.println((height-menuPlusTabHeaderHeight)/width);


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
