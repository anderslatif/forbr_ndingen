package Model;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Anders on 4/21/2016.
 */
public class DatabaseSaveAndGet {


    public void savePresentation(ArrayList<TabNode> presentation){

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();

            if(connection != null){
                preparedStatement = connection.prepareStatement("INSERT INTO slides(id, column_2) VALUES(DEFAULT, LOAD_FILE())");
//                ResultSet resultSet = statement.executeQuery("SELECT first_name, last_name FROM login;");
//                String firstName = resultSet.getString("first_name");



                for(TabNode tabNode: presentation){
                    System.out.println(tabNode);
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
            if(preparedStatement != null){
                try{
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }


    }



    public static ArrayList<SlideEvent> loadAllEvents(){

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        ArrayList<SlideEvent> eventCollection = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();

            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT * FROM events;");

            if(connection != null){

                while(resultSet.next()){

                    String date = resultSet.getString("date");
                    String header = resultSet.getString("header");
                    String textLabel = resultSet.getString("text");
                    String imagePath = resultSet.getString("image_path");


                    SlideEvent slideEvent = new SlideEvent(date, header, textLabel, imagePath);

                    eventCollection.add(slideEvent);

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
            if(statement != null){
                try{
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return eventCollection;
        }

    }







}
