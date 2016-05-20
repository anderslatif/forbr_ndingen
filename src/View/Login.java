package View;

import Model.DatabaseConnection;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login{


    Connection connection = null;
    TextField text1;
    TextField text2;
    boolean access = false;


    public GridPane loginScreen() {

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));


        Label label1 = new Label("first_name");
        text1 = new TextField();

        Label label2 = new Label("last_name");
        text2 = new TextField();

        Button button = new Button("Go");

        button.setOnAction( e -> loginAttempt());

        gridPane.add(label1, 0, 0);
        gridPane.add(text1, 1, 0);
        gridPane.add(label2, 0, 1);
        gridPane.add(text2, 1, 1);
        gridPane.add(button, 2, 1);


        return gridPane;
    }





    public void loginAttempt(){

        try {
            connection = DatabaseConnection.getConnection();

            if(connection != null){
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT first_name, last_name FROM login;");

                while(resultSet.next()){

                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");

                    if(text1.getText().equals(firstName) && text2.getText().equals(lastName)){
                        access = true;
                    }
                }

            }


        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // check if null and try catch properly when closing statement and resultSet

        }
    }

    public boolean accessAllowed(){
        return access;
    }




}
