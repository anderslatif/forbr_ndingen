package Model;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Anders on 4/21/2016.
 */
public class DatabaseConnection {

    private Connection connection;

    private static DatabaseConnection DBInstance = new DatabaseConnection();

    public static DatabaseConnection getInstance(){
        return DBInstance;
    }

    private DatabaseConnection(){
        connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?useSSL=false", "root", "root");

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public Connection getConnection(){

        return connection;

    }


}
