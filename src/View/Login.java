package view;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.DatabaseConnection;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Login{


    Connection connection = null;
    TextField text1;
    TextField text2;
    boolean access = false;
    Stage loginStage;


    public void logIn(){

        loginStage = new Stage();
        Scene eventScene = new Scene(loginScreen());

        loginStage.setScene(eventScene);
        loginStage.show();
    }

    public GridPane loginScreen() {

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));


        Label label1 = new Label("User name");
        text1 = new TextField();

        Label label2 = new Label("Password");
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

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();

            if(connection != null){
                ResultSet resultSet = statement.executeQuery("SELECT * FROM logins;");

                while(resultSet.next()){

                    String userName = resultSet.getString("username");
                    String password = resultSet.getString("password");

                    if(text1.getText().equals(userName) && text2.getText().equals(password)){
                        access = true;
                        System.out.println(access);
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
