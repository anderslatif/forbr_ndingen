package View;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.MenuBar;

/**
 * Created by Anders on 4/15/2016.
 */
public class Root {

    public static BorderPane getRootLayout(){

        BorderPane borderPane = new BorderPane();

        borderPane.setTop(getMenuBar());

        //borderPane.setCenter();
        // anchorpane ? with a rectangle and padding
        // drag and drop board

        return borderPane;
    }



    public static MenuBar getMenuBar(){

        MenuBar menuBar = new MenuBar();

        return menuBar;
    }



}
