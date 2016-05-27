package controller;

import model.*;
import view.Layout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Anders on 4/15/2016.
 */
public class Controller {


    public void saveNewSlideEventToDB(SlideEvent slideEvent){
        DatabaseSaveAndGet.saveNewEventSlide(slideEvent);
    }


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

    public String copyFileToDrive(String filePath){

        String tempPath = Util.turnBackslashToForward(filePath.substring(8));

        File file = new File(tempPath);

        FileInputStream in = null;
        FileOutputStream out = null;

        try{
            in = new FileInputStream(tempPath);
            out = new FileOutputStream("FileServer/"+Util.turnBackslashToForward(file.getName())); //end-point
            int myByte;

            //while Loop - Kører så længe inputted ikke er -1
            while ((myByte = in.read()) != -1){
                out.write(myByte);
            }

        } catch (IOException e){
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
                e.printStackTrace();
            }

        }

        File copiedFile = new File("FileServer/"+Util.turnBackslashToForward(file.getName()));

        filePath = "file:///"+Util.turnBackslashToForward(copiedFile.getAbsoluteFile().toString());

        return filePath;

    }


    public void deleteEvent(SlideEvent slideEvent){

        DatabaseSaveAndGet.deleteEventSlide(slideEvent);

    }


}
