package Model;

import Controller.Util;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Anders on 4/21/2016.
 */
public class DatabaseSaveAndGet {


    public void savePresentation(ArrayList<TabNode> presentation, LocalDate date){

        LocalDate chosenDate = date;
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

                    //todo check if the date is later than today's date if so we go into an if statement and add the event to the ArrayList

                    String date = resultSet.getString("slide_date");
                    String header = resultSet.getString("header");
                    String textLabel = resultSet.getString("slide_text");
                    String imagePath = resultSet.getString("image_path");


                    SlideEvent slideEvent = new SlideEvent(date, header, textLabel, imagePath);

                    eventCollection.add(slideEvent);

                    //todo finally the ArrayList should be sorted based on the date

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
            if(resultSet != null){
                try{
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            return eventCollection;
        }

    }





    public static void saveNewEventSlide(SlideEvent slideEvent){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();

            if(connection != null){

                String date = slideEvent.getDate();
                String header = Util.escapeApostrophe(slideEvent.getHeader());
                String textLabel = Util.escapeApostrophe(slideEvent.getText());

                String sanitizedPath = slideEvent.getImagePath();
                String queryString = "'" + date + "', '" + header + "', '" +
                                          textLabel+ "', '" + sanitizedPath + "'";

                System.out.println(date);
                System.out.println(sanitizedPath);

                preparedStatement = connection.prepareStatement("INSERT INTO events(slide_date, header, slide_text, image_path) VALUES(" + queryString + ")");

                preparedStatement.execute();




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




}
