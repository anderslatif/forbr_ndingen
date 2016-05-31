package controller;

import model.*;
import view.Layout;
import view.UserMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Anders, Mikkel on 4/15/2016.
 */
public class Controller {

    /**
     * We get the slides from the TabNode objects. We pass the chosen date and the Layout as parameters.
     * @param tabNodePresentation
     * @param chosenDate
     * @param view
     */
    public void savePresentation(ArrayList<TabNode> tabNodePresentation, String chosenDate, Layout view){


        ArrayList<Slide> slidePresentation = new ArrayList<>();


        for(TabNode tabNode : tabNodePresentation){


            Slide slide = tabNode.getSlide();

            if(!(slide.getImagePath() == null  || slide.getImagePath().equals("null") || slide.getImagePath().equals(""))){
                String new_path = copyFileToDrive(slide.getImagePath());
                slide.setImagePath(new_path);
                slidePresentation.add(slide);
            } else {
                slidePresentation.add(slide);
            }

        }

        DatabaseSaveAndGet.savePresentation(slidePresentation, chosenDate, view);
    }

    /**
     * Creates a copy of a file and returns the path of the new file as a string.
     * Is used to copy images from the local computer to a fileserver.
     * @param filePath
     * @return
     */
    public String copyFileToDrive(String filePath){

        String prePath  = Util.turnBackslashToForward(filePath.substring(8));
        String postPath = "";

        File file = new File(prePath);
        File fileToCheck = new File("FileServer/" + file.getName());

        if(fileToCheck.exists()){

            long fileLength = file.length() / 1024;
            long fileToCheckLength = fileToCheck.length() / 1024;

            if(fileLength != fileToCheckLength){

                postPath = doubleIO(prePath);
            } else {
                return "file:///"+Util.turnBackslashToForward(fileToCheck.getAbsoluteFile().toString());
            }

        } else {
            postPath = singleIO(prePath);
        }

        File copiedFile = new File(postPath);

        filePath = "file:///"+Util.turnBackslashToForward(copiedFile.getAbsoluteFile().toString());

        return filePath;

    }

    /**
     * This is to write a file to the Fileserver that isn't there yet.
     * @param filePath
     * @return
     */
    private String singleIO(String filePath){

        FileInputStream in = null;
        FileOutputStream out = null;

        File file = new File(filePath);
        String destination = "FileServer/"+Util.turnBackslashToForward(file.getName());

        try{
            in = new FileInputStream(filePath);
            out = new FileOutputStream(destination); //end-point

            int myByte;

            //while Loop - Kører så længe inputted ikke er -1
            while ((myByte = in.read()) != -1){
                out.write(myByte);
            }

        } catch (IOException e){
            UserMessage.setBottomLabelMessage("Error while copying a file to the new folder.", "Error");
            e.printStackTrace();

        }finally {

            try{
                if (in != null){
                    in.close();
                }
                if (out != null){
                    out.close();
                }
            } catch (IOException e){
                UserMessage.setBottomLabelMessage("Error. Could not abort file copying operation correctly.", "Error");
                e.printStackTrace();
            }

        }
       return destination;
    }

    /**
     * This is in case a different file with the same name is already on the Fileserver.
     * Since we can't read and write to the file at the sam time we have to write the file to a temp file and then write it back.
     * @param filePath
     * @return
     */
    private String doubleIO(String filePath){

        FileInputStream in = null;
        FileOutputStream out = null;

        File file = new File(filePath);

        String destination = "FileServer/"+Util.turnBackslashToForward(file.getName());

        try{
            in = new FileInputStream(filePath);
            out = new FileOutputStream("FileServer/temp"); //end-point

            int myByte;

            //while Loop - Kører så længe inputted ikke er -1
            while ((myByte = in.read()) != -1){
                out.write(myByte);
            }

        } catch (IOException e){
            UserMessage.setBottomLabelMessage("Error while copying a file to the new folder.", "Error");
            e.printStackTrace();

        }finally {

            try{
                if (in != null){
                    in.close();
                }
                if (out != null){
                    out.close();
                }
            } catch (IOException e){
                UserMessage.setBottomLabelMessage("Error. Could not abort file copying operation correctly.", "Error");
                e.printStackTrace();
            }

        }

        in = null;
        out = null;

        try{  // second time
            in = new FileInputStream("FileServer/temp");
            out = new FileOutputStream(destination); //end-point

            int myByte;

            //while Loop - Kører så længe inputted ikke er -1
            while ((myByte = in.read()) != -1){
                out.write(myByte);
            }

        } catch (IOException e){
            UserMessage.setBottomLabelMessage("Error while copying a file to the new folder.", "Error");
            e.printStackTrace();

        }finally {

            try{
                if (in != null){
                    in.close();
                }
                if (out != null){
                    out.close();
                }
            } catch (IOException e){
                UserMessage.setBottomLabelMessage("Error. Could not abort file copying operation correctly.", "Error");
                e.printStackTrace();
            }

        }

        return destination;
    }


    public void saveNewSlideEventToDB(SlideEvent slideEvent){
        DatabaseSaveAndGet.saveNewEventSlide(slideEvent);
    }


    public void deleteEvent(SlideEvent slideEvent){
        DatabaseSaveAndGet.deleteEventSlide(slideEvent);
    }


}
