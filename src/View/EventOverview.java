package View;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by Anders on 5/2/2016.
 */
public class EventOverview {


    Stage stage;

    public void getEventOverview(){

        stage = new Stage();

        Scene scene = new Scene(getGridPane(), 800, 475);

        stage.setScene(scene);
        stage.show();
    }


    public GridPane getGridPane(){

        GridPane gridPane = new GridPane();

        gridPane.setOnMouseClicked( e -> stage.close());

        return gridPane;
    }


}
