package model;

<<<<<<< HEAD
import com.jcraft.jsch.JSchException;

import javax.sound.sampled.Port;
import java.net.NoRouteToHostException;
=======
import controller.Util;

>>>>>>> 2df434379e3bebd942940d1d3918b6c2f8129eef
import java.sql.Connection;
import java.sql.DriverManager;

import static model.PortForwardingL.portForwardL;

/**
 * Created by Anders on 4/21/2016.
 */
public class DatabaseConnection {

    private static int iPort = 3306; // ændres til 1025, hvis der skal forbindes til Raspberry.

    public static Connection getConnection(){
        Connection connection = null;

        //PortForwardingL.portForwardL();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:"+iPort+"/mydb?useSSL=false", "root", "root");

        } catch(Exception e){
            e.printStackTrace();
        }

        return connection;

    }

    public static void chooseDatabase(){

        PortForwardingL.portForwardL();

        if(PortForwardingL.getSession().isConnected()){
            iPort = 1025;
        }
        PortForwardingL.closeConnection();

    }


}