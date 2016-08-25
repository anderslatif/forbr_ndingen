package model;

import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

//Husk at fjerne mellemrummet efter stort T. Søg evt. på "T"

public class SCPTo {


    public static void getFileOverSCPTo(String localFilePath) {

        FileInputStream fis = null;
        try {

            //String[] args = {"pi@192.168.1.181:/home/pi/FileServer/"};
            String user = "pi";
            String host = "192.168.1.181";
            String rfile = "/home/pi/FileServer/";

            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);

            // username and password will be given via UserInfo interface.
            UserInfo ui = new MyUserInfo();
            session.setUserInfo(ui);
            session.connect();

            boolean ptimestamp = true;

            // exec 'scp -t rfile' remotely
            String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + rfile;
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            /*if (checkAck(in) != 0) {
                System.exit(0);
            }*/

            File _lfile = new File(localFilePath);// skal være Path

            if (ptimestamp) {
                command = "T" + (_lfile.lastModified() / 1000) + " 0";
                // The access time should be sent here,
                // but it is not accessible with JavaAPI ;-<
                command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
                out.write(command.getBytes());
                out.flush();
                /*if (checkAck(in) != 0) {
                    System.exit(0);
                }*/
            }

            // send "C0644 filesize filename", where filename should not include '/'
            long filesize = _lfile.length();
            command = "C0644 " + filesize + " ";
            if (localFilePath.lastIndexOf('/') > 0) {
                command += localFilePath.substring(localFilePath.lastIndexOf('/') + 1);
            } else {
                command += localFilePath;
            }
            command += "\n";
            out.write(command.getBytes());
            out.flush();
            /*if (checkAck(in) != 0) {
                System.exit(0);
            }*/

            // send a content of localFilePath
            fis = new FileInputStream(localFilePath);
            byte[] buf = new byte[1024];
            while (true) {
                int len = fis.read(buf, 0, buf.length);
                if (len <= 0) break;
                out.write(buf, 0, len); //out.flush();
            }
            fis.close();
            fis = null;
            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            /*if (checkAck(in) != 0) {
                System.exit(0);
            }*/
            out.close();

            channel.disconnect();
            session.disconnect();

            //System.exit(0);
        } catch (Exception e) {
            System.out.println(e);
            try {
                if (fis != null) fis.close();
            } catch (Exception ee) {
            }
        }
    }

    static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }

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