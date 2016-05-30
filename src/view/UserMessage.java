package view;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * Created by Anders on 5/27/2016.
 */
public class UserMessage {

    public static BorderPane borderPane;

    public static void setBottomLabelMessage(String message){

        Label bottomLabel = new Label();
        bottomLabel.setMaxHeight(10);
        borderPane.setBottom(bottomLabel);

        bottomLabel.setTextFill(Color.RED);
        bottomLabel.setMaxWidth(Double.MAX_VALUE);
        bottomLabel.setAlignment(Pos.CENTER);
        bottomLabel.getStyleClass().add("bottomLabel");

        bottomLabel.setText(message);

        borderPane.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                borderPane.setBottom(null);
            }
        });
    }


}