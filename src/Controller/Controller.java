package controller;

import model.*;
import view.Layout;
import view.UserMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

        System.out.println(filePath);

        String tempPath = Util.turnBackslashToForward(filePath.substring(8));

        System.out.println(tempPath);

        File file = new File(tempPath);

        FileInputStream in = null;
        FileOutputStream out = null;

        //String fileToLookFor = "FileServer/"+Util.turnBackslashToForward(file.getName());

        //if(file.exists()){}

        try{
            in = new FileInputStream(tempPath);
            out = new FileOutputStream("FileServer/"+Util.turnBackslashToForward(file.getName())); //end-point

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

        File copiedFile = new File("FileServer/"+Util.turnBackslashToForward(file.getName()));

        filePath = "file:///"+Util.turnBackslashToForward(copiedFile.getAbsoluteFile().toString());

        return filePath;

    }


    public void saveNewSlideEventToDB(SlideEvent slideEvent){
        DatabaseSaveAndGet.saveNewEventSlide(slideEvent);
    }


    public void deleteEvent(SlideEvent slideEvent){
        DatabaseSaveAndGet.deleteEventSlide(slideEvent);
    }


}
