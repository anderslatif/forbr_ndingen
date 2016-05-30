package model;

import controller.Util;
import view.Layout;
import view.UserMessage;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Anders, Mikkel on 4/21/2016.
 */
public class DatabaseSaveAndGet {

    /**
     * Saves each slide in a row in the slides table in the database.
     * We pass the Layout to be able to call newPresentation().
     * @param presentation
     * @param date
     * @param view
     */
    public static void savePresentation(ArrayList<Slide> presentation, String date, Layout view) {

        deleteSlidesWithThisDate(date);

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        for (Slide s : presentation) {

            String startTime = null;
            String header = null;
            String text = null;
            String imagePath = null;

            String slideType = s.getSlideType();

            switch (slideType) {
                case "SlideEvent":
                    SlideEvent eS = (SlideEvent) s;

                    if(eS.getStartTime() == null){
                        startTime = eS.getStartTime();
                    } else {
                        startTime = Util.escapeApostrophe(eS.getStartTime());
                    }
                    if(eS.getHeader() == null){
                        header = eS.getHeader();
                    } else {
                        header = Util.escapeApostrophe(eS.getHeader());
                    }
                    if(eS.getText() == null){
                        text = eS.getText();
                    } else {
                        text = Util.escapeApostrophe(eS.getText());
                    }
                    imagePath = Util.turnBackslashToForward(eS.getImagePath());

                    break;

                case "SlidePicture":
                    SlidePicture pS = (SlidePicture) s;

                    if(pS.getImagePath() == null){
                        imagePath = pS.getImagePath();
                    } else{
                        imagePath = Util.turnBackslashToForward(pS.getImagePath());
                    }
                    break;

                case "SlideHappyHour":
                    SlideHappyHour hS = (SlideHappyHour) s;

                    if(hS.getHeader() == null){
                        header = hS.getHeader();
                    } else {
                        header = Util.turnBackslashToForward(hS.getHeader());
                    }
                    if(hS.getText() == null){
                        text = hS.getText();
                    } else {
                        text = Util.turnBackslashToForward(hS.getText());
                    }

                    if(!(hS.getImagePath() == null  || hS.getImagePath().equals("null") || hS.getImagePath().equals(""))){
                        imagePath = Util.turnBackslashToForward(hS.getImagePath());
                    }
                    break;
                default:
                    System.out.println("Error in savePresentation() in DatabaseSaveAndGet.");

            }

            try {
                connection = DatabaseConnection.getConnection();

                if (connection != null) {

                    String qString1 = "INSERT INTO slides (slide_date, start_time, slide_type, header, slide_text, image_path) ";
                    String qString2 = "Values ('"+date+"', '"+startTime+"', '"+slideType+"', '"+header+"', '"+text+"', '"+imagePath+"')";
                    preparedStatement = connection.prepareStatement(qString1+qString2);
                    preparedStatement.executeUpdate();
                }


            } catch (Exception e) {
                UserMessage.setBottomLabelMessage("Error. Could not save your slides correctly. \nPlease open the presentation to check if all is there.", "Error");
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                        PortForwardingL.closeConnection();
                    } catch (SQLException e) {
                        UserMessage.setBottomLabelMessage("Could not close connections while saving.", "Error");
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
        if(Layout.newPresentation == true){
            view.newPresentation();
            Layout.newPresentation = false;
        }

    }

    /**
     * Is used for the method savePresentation() to overwrite and delete existing slides on a specific date.
     * @param date
     */
    public static void deleteSlidesWithThisDate(String date){

        Connection connection = null;
        PreparedStatement statement = null;
        String sqlQuery = "DELETE FROM slides WHERE slide_date = '"+date+"';";

        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sqlQuery);
            statement.executeUpdate();


        } catch (Exception e) {
            UserMessage.setBottomLabelMessage("Error. Could not connect to the database", "Error");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    PortForwardingL.closeConnection();
                } catch (SQLException e) {
                    UserMessage.setBottomLabelMessage("Error occurred while deleting your previous slides", "Error");
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    UserMessage.setBottomLabelMessage("Error occurred while deleting your previous slides", "Error");
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Check if slides are already saved on this date to warn users who select a date with datepicker.
     * The warning appears before the save or open button is clicked.
     * @param date
     * @return
     */
    public static boolean checkIfSlidesAreAlreadySavedOnThisDate(String date){

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String sqlQuery = "SELECT * FROM slides WHERE slide_date = '"+date+"';";
        boolean foundAny = false;

        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlQuery);

            if(resultSet.next()){
                foundAny = true;
            } else {
                foundAny = false;
            }


        } catch (Exception e) {
            UserMessage.setBottomLabelMessage("Error. Could not connect to the database", "Error");
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
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return foundAny;
        }
    }


    /**
     * Opens the presentation from the selected date.
     * @param date
     * @return
     */
    public static ArrayList<Slide> openPresentation(String date){

        ArrayList<Slide> presentation = new ArrayList<>();

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
            UserMessage.setBottomLabelMessage("Error. Could not connect to the database", "Error");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    PortForwardingL.closeConnection();
                } catch (SQLException e) {
                    UserMessage.setBottomLabelMessage("Error. Could not close the connection.", "Error");
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    UserMessage.setBottomLabelMessage("Error. Could not close the connection.", "Error");
                    e.printStackTrace();
                }
            }
        }
        return presentation;

    }

    /**
     * This aids openPresentation() with creating Slide objects from the ResultSet.
     * @param resultSet
     * @param date
     * @return
     */
    public static ArrayList<Slide> resultSetToArrayList(ResultSet resultSet, String date){
        ArrayList<Slide> presentation = new ArrayList<>();

        try {
            while(resultSet.next()){

                String slideType = resultSet.getString("slide_type");

                switch (slideType) {
                    case "SlideEvent":

                        String startTime = resultSet.getString("start_time");
                        String eHeader = resultSet.getString("header");
                        String eText = resultSet.getString("slide_text");
                        String eImagePath = resultSet.getString("image_path");

                        SlideEvent se = new SlideEvent("SlideEvent", "", null, startTime, eHeader, eText, eImagePath);

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
                        UserMessage.setBottomLabelMessage("Error. Could at least one slide is missing.", "Error");
                        System.out.println("Error while calling resultSetToArrayList() in DatabaseSaveAndGet");

                }

            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return presentation;
    }


    /**
     * Loads all events starting from today's date.
     * Orders them chronologically with Collections.sort().
     * @return
     */
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

            resultSet = statement.executeQuery("SELECT * FROM events WHERE event_date >= '" + localDate + "';");

            if(connection != null){

                while(resultSet.next()){

                    String date = resultSet.getString("event_date");
                    String startTime = resultSet.getString("start_time");
                    String header = resultSet.getString("header");
                    String textLabel = resultSet.getString("slide_text");
                    String imagePath = resultSet.getString("image_path");

                    LocalDate dateOfSlideEvent = LocalDate.parse(date);

                    SlideEvent slideEvent = new SlideEvent("slideEvent", date, dateOfSlideEvent, startTime, header, textLabel, imagePath);

                    eventCollection.add(slideEvent);
                    eventDateList.add(dateOfSlideEvent);

                }

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
            UserMessage.setBottomLabelMessage("Error. Could not connect to the Database.", "Error");
            e.printStackTrace();
        } finally {
            if(connection != null){
                try {
                    connection.close();
                    PortForwardingL.closeConnection();
                } catch (SQLException e) {
                    UserMessage.setBottomLabelMessage("Error. Could not close the connection.", "Error");
                    e.printStackTrace();
                }
            }
            if(statement != null){
                try{
                    statement.close();
                } catch (SQLException e) {
                    UserMessage.setBottomLabelMessage("Error. Could not close the connection.", "Error");
                    e.printStackTrace();
                }
            }
            if(resultSet != null){
                try{
                    resultSet.close();
                } catch (SQLException e){
                    UserMessage.setBottomLabelMessage("Error. Could not close the connection.", "Error");
                    e.printStackTrace();
                }
            }

            return orderedEventList;
        }

    }


    /**
     * Saves new event Slide. Takes one newly created event.
     * @param slideEvent
     */
    public static void saveNewEventSlide(SlideEvent slideEvent){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();

            if(connection != null){

                String date = Util.turnBackslashToForward(slideEvent.getDate());
                String startTime = slideEvent.getStartTime();
                String header = Util.escapeApostrophe(slideEvent.getHeader());
                String textLabel = Util.escapeApostrophe(slideEvent.getText());

                String imagePath =  Util.turnBackslashToForward(slideEvent.getImagePath());

                String queryString = "'" + date + "', '" + startTime + "', '" +header + "', '" +
                                          textLabel+ "', '" + imagePath + "'";

                preparedStatement = connection.prepareStatement("INSERT INTO events(event_date, start_time, header, slide_text, image_path) VALUES(" + queryString + ")");

                preparedStatement.execute();

            }


        } catch(Exception e){
            UserMessage.setBottomLabelMessage("Error. Could not connect to the Database.", "Error");
            e.printStackTrace();
        } finally {
            if(connection != null){
                try {
                    connection.close();
                    PortForwardingL.closeConnection();
                } catch (SQLException e) {
                    UserMessage.setBottomLabelMessage("Error. Could not close the connection.", "Error");
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null){
                try{
                    preparedStatement.close();
                } catch (SQLException e) {
                    UserMessage.setBottomLabelMessage("Error. Could not close the connection.", "Error");
                    e.printStackTrace();
                }
            }

        }
    }


    /**
     * Deletes an event from the event overview.
     * @param slideEvent
     */
    public static void deleteEventSlide(SlideEvent slideEvent){

        Connection connection = null;
        Statement statement = null;

        String date = slideEvent.getDate();
        String header = slideEvent.getHeader();
        String slideText = slideEvent.getText();
        String sqlQuery = "DELETE FROM events WHERE event_date = '" + date + "' AND header = '" + header +"' AND slide_text = '" + slideText + "'";

        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(sqlQuery);


        } catch (Exception e) {
            UserMessage.setBottomLabelMessage("Error. Could not connect to the Database.", "Error");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    PortForwardingL.closeConnection();
                } catch (SQLException e) {
                    UserMessage.setBottomLabelMessage("Error. Could not close the connection.", "Error");
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    UserMessage.setBottomLabelMessage("Error. Could not close the connection.", "Error");
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Used for colour coding dates in the DatePicker. Returns an ArrayList of unique dates where slides exist.
     * @return
     */
    public static ArrayList<LocalDate> getPresentationDates(){

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        ArrayList<LocalDate> uniqueDates = new ArrayList<>();


        try {
            connection = DatabaseConnection.getConnection();

            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT DISTINCT slide_date FROM slides;");

                while(resultSet.next()){

                    String date = resultSet.getString("slide_date");

                    LocalDate localDate = LocalDate.parse(date);

                    uniqueDates.add(localDate);

                }


            } catch(Exception e){
                UserMessage.setBottomLabelMessage("Error. Could not connect to the Database.", "Error");
                e.printStackTrace();
            } finally {
                if(connection != null){
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        UserMessage.setBottomLabelMessage("Error. Could not close the connection.", "Error");
                        e.printStackTrace();
                    }
                }
                if(statement != null){
                    try{
                        statement.close();
                    } catch (SQLException e) {
                        UserMessage.setBottomLabelMessage("Error. Could not close the connection.", "Error");
                        e.printStackTrace();
                    }
                }
                if(resultSet != null){
                    try{
                        resultSet.close();
                    } catch (SQLException e){
                        UserMessage.setBottomLabelMessage("Error. Could not close the connection.", "Error");
                        e.printStackTrace();
                    }
                }

                return uniqueDates;
            }



    }




}