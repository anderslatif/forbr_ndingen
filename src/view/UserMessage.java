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

    /**
     * We access the BorderPane from here to set the bottom.
     * It's made static to access this class from everywhere without object initialisation.
     */
    public static BorderPane borderPane;

    /**
     * This method creates a message to the user and shows it at the bottom of the program.
     * The message comes in two varieties "Error" and "Info" where the first is red and the second is green.
     * @param message
     * @param request
     */
    public static void setBottomLabelMessage(String message, String request){

        Label bottomLabel = new Label();
        //bottomLabel.setMaxHeight(20);
        borderPane.setBottom(bottomLabel);

        if(request.equals("Info")){
            bottomLabel.setTextFill(Color.GREEN);

        } else if(request.equals("Error")){
            bottomLabel.setTextFill(Color.RED);

        }
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
