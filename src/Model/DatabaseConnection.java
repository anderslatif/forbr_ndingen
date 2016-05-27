package model;

import java.sql.Connection;
import java.sql.DriverManager;

import static model.PortForwardingL.portForwardL;

/**
 * Created by Anders on 4/21/2016.
 */
public class DatabaseConnection {



    public static Connection getConnection(){
        Connection connection = null;
        //PortForwardingL.portForwardL();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?useSSL=false", "root", "root");
            //connection = DriverManager.getConnection("jdbc:mysql://localhost:1025/mydb?useSSL=false", "root", "root");

        } catch(Exception e){
            e.printStackTrace();
        }

        return connection;

    }


}