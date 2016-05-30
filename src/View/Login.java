package view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DatabaseConnection;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.sql.*;

/**
 * Created by Dennis
 */


public class Login{


    private Connection connection = null;
    private TextField text1;
    private TextField text2;
    private TextField user;
    private TextField pass;
    private Stage loginStage;
    private String userName;

    /**
     * Checks whether the user wants to log in or change username and password.
     * @param layout
     * @param id
     */
    public void userStage(Layout layout, String id){
        Scene loginScene = null;

        if(id.equals("login")) {
            loginStage = new Stage();
            loginScene = new Scene(loginScreen(layout));
        }else if(id.equals("edit")){
            loginStage = new Stage();
            loginScene = new Scene(changeUsernameAndPassword());
        }
        loginStage.setScene(loginScene);
        loginStage.show();

    }

    /**
     * Creates the login-window
     * @param layout
     * @return
     */
    public GridPane loginScreen(Layout layout) {

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));


        Label label1 = new Label("Username");
        label1.setPadding(new Insets(0, 10, 0, 0));
        text1 = new TextField();

        Label label2 = new Label("Password");
        label2.setPadding(new Insets(0, 10, 0, 0));
        text2 = new TextField();

        Button button = new Button("Go");

        button.setOnAction( e -> loginAttempt(layout));
        button.setDefaultButton(true);

        gridPane.add(label1, 0, 0);
        gridPane.add(text1, 1, 0);
        gridPane.add(label2, 0, 1);
        gridPane.add(text2, 1, 1);
        gridPane.add(button, 2, 1);


        return gridPane;
    }

    /**
     * Checks in the database whether the login is correct.
     * @param layout
     */
    public void loginAttempt(Layout layout){

        Connection connection = null;
        Statement statement;

        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM logins;");

                while(resultSet.next()){

                    String userName = resultSet.getString("username");
                    String password = resultSet.getString("password");

                    if(text1.getText().equals(userName) && text2.getText().equals(password)){
                        layout.loginState.setValue(false);
                        UserMessage.setBottomLabelMessage("You are now logged in", "info");
                        loginStage.close();
                    }else{
                        UserMessage.setBottomLabelMessage("Invalid username or password", "error");
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
        }
    }

    /**
     * Creates the window to change the username and password.
     * @return
     */
    public GridPane changeUsernameAndPassword(){

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));


        Label label1 = new Label("New user name");
        user = new TextField();

        Label label2 = new Label("New password");
        pass = new TextField();

        Button button = new Button("Go");
        button.setOnAction( e -> changeAttempt());
        button.setDefaultButton(true);

        gridPane.add(label1, 0, 0);
        gridPane.add(user, 1, 0);
        gridPane.add(label2, 0, 1);
        gridPane.add(pass, 1, 1);
        gridPane.add(button, 2, 1);


        return gridPane;
    }

    /**
     * Updates the username and password in the database.
     */
    public void changeAttempt(){

        connection = null;
        ResultSet resultSet;

        try{
            connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            if(connection != null) {
                resultSet = statement.executeQuery("SELECT username FROM logins;");
                while(resultSet.next()) {
                    userName = resultSet.getString("username");
                }

                String sqlString = "UPDATE logins SET username = '" + user.getText() + "', password = '" + pass.getText() + "' WHERE username = '" + userName + "';";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlString);

                    try {
                        preparedStatement.executeUpdate();
                        loginStage.close();
                        UserMessage.setBottomLabelMessage("Username and password updated", "info");
                    } catch (SQLException s) {
                        s.printStackTrace();
                        UserMessage.setBottomLabelMessage("Error while updating username and password", "error");
                    }
                }
        }catch(SQLException e) {
            e.printStackTrace();
            UserMessage.setBottomLabelMessage("Error while updating username and password", "error");
        }
        finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}





