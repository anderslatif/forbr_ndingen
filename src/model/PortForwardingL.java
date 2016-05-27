package model;
import com.jcraft.jsch.*;
import java.awt.*;

public class PortForwardingL{

    public static void portForwardL(){

        int lport = 1025;
        String rhost = "localhost";
        int rport = 3306;
        Session session = null;
        String user = "pi";
        String host = "192.168.1.181";

        try{
            JSch jsch=new JSch();

            session=jsch.getSession(user, host, 22);

            // username and password will be given via UserInfo interface.
            UserInfo ui=new MyUserInfo();
            session.setUserInfo(ui);

            session.connect();

            int assinged_port=session.setPortForwardingL(lport, rhost, rport);
            System.out.println("localhost:"+assinged_port+" -> "+rhost+":"+rport);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    //klassen MyUserInfo

    public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{

        public String getPassword(){ return passwd; }

        public boolean promptYesNo(String str){
            return true;
        }

        String passwd;

        public String getPassphrase(){
            return null; }

        public boolean promptPassphrase(String message){
            return true; }

        public boolean promptPassword(String message){

                passwd="raspberry";
                return true;
            }

        public void showMessage(String message){

        }

        public String[] promptKeyboardInteractive(String destination,
                                                  String name,
                                                  String instruction,
                                                  String[] prompt,
                                                  boolean[] echo){
            return null;
        }
    }
}