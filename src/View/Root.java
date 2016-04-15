package View;

import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
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

        Menu menu1 = new Menu("File");
        Menu menu2 = new Menu("Edit");
        Menu menu3 = new Menu("Tilføj Slide");

        MenuItem m3_1 = new MenuItem("Event slide");

        menu3.getItems().addAll(m3_1);

        menuBar.getMenus().addAll(menu1, menu2, menu3);

        return menuBar;
    }



}
