import View.Layout;

import View.Login;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        Layout view = new Layout();

<<<<<<< HEAD
        Login l = new Login(){};
        Scene scene = new Scene(l.loginScreen());
        if(l.accessAllowed()) {
            Scene scene = new Scene(view.getRootLayout());
            view.initializeLayout(scene, primaryStage);
        }
=======
        Scene scene = new Scene(Login.loginScreen());

        //Scene scene = new Scene(view.getRootLayout());
        //view.initializeLayout(scene, primaryStage);


>>>>>>> c95d8dbba54305054e3bc0596e70da485d0390a1

        double menuPlusTabHeaderHeight = view.getMenuBar().getHeight() + 20;

        double height = Screen.getPrimary().getVisualBounds().getHeight();
        double width = (Screen.getPrimary().getVisualBounds().getHeight()-menuPlusTabHeaderHeight) /1.7777;

<<<<<<< HEAD
        //primaryStage.setMinHeight(height);
        //primaryStage.setMinWidth(width);
=======
//        primaryStage.setMinHeight(height);
//        primaryStage.setMinWidth(width);
>>>>>>> c95d8dbba54305054e3bc0596e70da485d0390a1


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
