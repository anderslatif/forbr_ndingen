package Model;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Anders on 4/21/2016.
 */
public class DatabaseSaveAndGet {


    public void savePresentation(ArrayList<File> presentation){

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();

            if(connection != null){
                preparedStatement = connection.prepareStatement("INSERT INTO slides(id, column_2) VALUES(DEFAULT, LOAD_FILE())");
//                ResultSet resultSet = statement.executeQuery("SELECT first_name, last_name FROM login;");
//                String firstName = resultSet.getString("first_name");



                for(File fil : presentation){
                    System.out.println(fil.getAbsolutePath());
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



}
