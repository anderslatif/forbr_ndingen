import View.Root;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        Scene scene = new Scene(Root.getRootLayout(), 300, 275);

        primaryStage.setScene(scene);
        primaryStage.show();
    }




}
