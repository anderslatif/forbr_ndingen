package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by Anders on 4/21/2016.
 */
public class DatabaseConnection {

    private Connection connection;
    private Statement statement;

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

    public Statement prepareStatement(String sqlString) {
        try {
            Statement preparedStatement = connection.prepareStatement(sqlString);
            return preparedStatement;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Statement createStatement(){
        try{
            Statement createdStatement = connection.createStatement();
            return createdStatement;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public Connection getConnection(){
        return connection;
    }

}
