import view.Layout;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import view.Login;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    public void start(Stage primaryStage) throws Exception {

<<<<<<< HEAD
                Layout view = new Layout();
                Scene scene = new Scene(view.getRootLayout());
                view.initializeLayout(scene, primaryStage, view);

                //Scene scene = new Scene(view.getRootLayout());
                //view.initializeLayout(scene, primaryStage);
=======
        Scene scene = new Scene(view.getRootLayout());
        view.initializeLayout(scene, primaryStage, view);
        double menuPlusTabHeaderHeight = view.getMenuBar().getHeight() + 20;
        double height = Screen.getPrimary().getVisualBounds().getHeight();
        double width = (Screen.getPrimary().getVisualBounds().getHeight()-menuPlusTabHeaderHeight) /1.7777;
        primaryStage.setMinHeight(height);
        primaryStage.setMinWidth(width);
        primaryStage.setResizable(false);
>>>>>>> 6c25b465b312c8ea291fce3d2521e60529cbac12

                //Scene scene = new Scene(view.getRootLayout());
                //view.initializeLayout(scene, primaryStage, view);
                double menuPlusTabHeaderHeight = view.getMenuBar().getHeight() + 20;
                double height = Screen.getPrimary().getVisualBounds().getHeight();
                double width = (Screen.getPrimary().getVisualBounds().getHeight() - menuPlusTabHeaderHeight) / 1.7777;
                primaryStage.setMinHeight(height);
                primaryStage.setMinWidth(width);
            }

<<<<<<< HEAD
            scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());


            primaryStage.getIcons().add(new Image("file:src/logo.png"));
=======
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        primaryStage.getIcons().add(new Image("file:src/logo.png"));
>>>>>>> 6c25b465b312c8ea291fce3d2521e60529cbac12

/*        primaryStage.setOnCloseRequest( e -> {
            e.consume();
            view.savePresentationBeforeClosingAll();
        });*/

            primaryStage.setScene(scene);
            primaryStage.show();
        }



}
