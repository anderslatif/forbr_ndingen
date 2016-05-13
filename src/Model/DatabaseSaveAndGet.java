package Model;

import Controller.Util;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Anders on 4/21/2016.
 */
public class DatabaseSaveAndGet {

    public static void savePresentation(ArrayList<Slide> presentation, String date) {

        deleteSlidesWithThisDate(date);

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        for (Slide s : presentation) {

            String header = null;
            String text = null;
            String imagePath = null;

            String slideType = s.getSlideType();

            switch (slideType) {
                case "SlideEvent":
                    SlideEvent eS = (SlideEvent) s;
                    header = eS.getHeader();
                    text = eS.getText();
                    imagePath = Util.turnBackslashToForward(eS.getImagePath());
                    System.out.println("Slide event image path: " + imagePath);
                    break;

                case "SlidePicture":
                    SlidePicture pS = (SlidePicture) s;
                    imagePath = Util.turnBackslashToForward(pS.getImagePath());
                    System.out.println("Slide Picture image path: " + imagePath);
                    break;

                case "SlideHappyHour":
                    SlideHappyHour hS = (SlideHappyHour) s;
                    header = hS.getHeader();
                    text = hS.getText();
                    imagePath = Util.turnBackslashToForward(hS.getImagePath());
                    System.out.println("Slide happy hour image path: " + imagePath);
                    break;
                default:
                    System.out.println("Error in savePresentation() in DatabaseSaveAndGet.");

            }

            try {
                connection = DatabaseConnection.getConnection();

                if (connection != null) {

                    String qString1 = "INSERT INTO slides (slide_date, slide_type, header, slide_text, image_path) ";
                    String qString2 = "Values ('"+date+"', '"+slideType+"', '"+header+"', '"+text+"', '"+imagePath+"')";
                    preparedStatement = connection.prepareStatement(qString1+qString2);
                    preparedStatement.executeUpdate();

//                ResultSet resultSet = statement.executeQuery("SELECT first_name, last_name FROM login;");
//                String firstName = resultSet.getString("first_name");

                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void deleteSlidesWithThisDate(String date){

        Connection connection = null;
        Statement statement = null;
        String sqlQuery = "DELETE FROM slides WHERE slide_date = '"+date+"';";

        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(sqlQuery);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    public static ArrayList<Slide> openPresentation(String date){

        ArrayList<Slide> presentation = null;

        Connection connection = null;
        Statement statement = null;
        String sqlQuery = "SELECT * FROM slides WHERE slide_date = '"+date+"';";

        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();

            resultSet = statement.executeQuery(sqlQuery);
            presentation = resultSetToArrayList(resultSet, date);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return presentation;

    }

    public static ArrayList<Slide> resultSetToArrayList(ResultSet resultSet, String date){
        ArrayList<Slide> presentation = new ArrayList<>();

        try {
            while(resultSet.next()){

                String slideType = resultSet.getString("slide_type");

                switch (slideType) {
                    case "SlideEvent":

                        String eHeader = resultSet.getString("header");
                        String eText = resultSet.getString("slide_text");
                        String eImagePath = resultSet.getString("image_path");

                        SlideEvent se = new SlideEvent("SlideEvent", "", null, eHeader, eText, eImagePath);

                        presentation.add(se);

                        break;

                    case "SlidePicture":

                        String pImagePath = resultSet.getString("image_path");

                        SlidePicture sp = new SlidePicture("SlidePicture", pImagePath);

                        presentation.add(sp);

                        break;

                    case "SlideHappyHour":

                        String hHeader = resultSet.getString("header");
                        String hText = resultSet.getString("slide_text");
                        String hImagePath = ""+resultSet.getString("image_path");

                        SlideHappyHour sh = new SlideHappyHour("SlideHappyHour", hHeader, hText, hImagePath);

                        presentation.add(sh);

                        break;
                    default:
                        System.out.println("Error while calling resultSetToArrayList() in DatabaseSaveAndGet");

                }

            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return presentation;
    }



    public static ArrayList<SlideEvent> loadAllEvents(){

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        ArrayList<SlideEvent> eventCollection = new ArrayList<>();
        List<LocalDate> eventDateList = new ArrayList<>();
        ArrayList<SlideEvent> orderedEventList = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();

            statement = connection.createStatement();

            LocalDate localDate = LocalDate.now();

            resultSet = statement.executeQuery("SELECT * FROM events WHERE slide_date >= '" + localDate + "';");

            if(connection != null){

                while(resultSet.next()){


                    String date = resultSet.getString("slide_date");
                    String header = resultSet.getString("header");
                    String textLabel = resultSet.getString("slide_text");
                    String imagePath = resultSet.getString("image_path");

                    LocalDate dateOfSlideEvent = LocalDate.parse(date);

                    SlideEvent slideEvent = new SlideEvent("slideEvent", date, dateOfSlideEvent, header, textLabel, imagePath);

                    eventCollection.add(slideEvent);
                    eventDateList.add(dateOfSlideEvent);

                }

/*                Collections.sort(eventCollection, new Comparator<SlideEvent>() {
                    @Override
                    public int compare(SlideEvent o1, SlideEvent o2) {
                        return o1.getLocalDate().compareTo(o2.getLocalDate());
                    }
                });*/

                Collections.sort(eventDateList);

                for(LocalDate loco : eventDateList){

                    for(SlideEvent slideEvent : eventCollection){

                        if(loco == slideEvent.getLocalDate()){
                            orderedEventList.add(slideEvent);
                        }
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

            return orderedEventList;
        }

    }





    public static void saveNewEventSlide(SlideEvent slideEvent){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();

            if(connection != null){

                String date = Util.turnBackslashToForward(slideEvent.getDate());
                String header = Util.escapeApostrophe(slideEvent.getHeader());
                String textLabel = Util.escapeApostrophe(slideEvent.getText());

                String sanitizedPath = Util.turnBackslashToForward(slideEvent.getImagePath());
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
