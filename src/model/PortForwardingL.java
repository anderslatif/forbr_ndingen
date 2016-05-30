package model;
import com.jcraft.jsch.*;


public class PortForwardingL{

    private static Session session = null;

    public static Session getSession() {
        return session;
    }

    /**
     * Creates an ssh connection to the server and passes the local port 1025 to the remote port 3306.
     */
    public static void portForwardL() {

        if (session == null) {

            int lport = 1025;
            String rhost = "localhost";
            int rport = 3306;
            String user = "pi";
            String host = "192.168.43.65";

            try {
                JSch jsch = new JSch();

                session = jsch.getSession(user, host, 22);

                // username and password will be given via UserInfo interface.
                UserInfo ui = new MyUserInfo();
                session.setUserInfo(ui);

                session.connect(4000);

                int assigned_port = session.setPortForwardingL(lport, rhost, rport);
                System.out.println("localhost:" + assigned_port + " -> " + rhost + ":" + rport);
            } catch (JSchException e) {
                System.out.println(e);
            }
        }

    }


    /**
     *Checks whether a connection is established and then disconnects it.
     */
    public static void closeConnection() {

        if(session !=null && session.isConnected()){
            session.disconnect();
            session = null;
            System.out.println("disconnecting session");
        } else {
            System.out.println("no connection to disconnect");
        }
    }


    /**
     * Implements UserInfo and contains the credentials to connect to the Raspberry Pi.
     */
    public static class MyUserInfo implements UserInfo{

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

                passwd = "raspberry";
                return true;
            }

        public void showMessage(String message){

        }
    }
}