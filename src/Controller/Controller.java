package Controller;

import Model.*;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Anders on 4/15/2016.
 */
public class Controller {


    public void openPresentation(){
        // let the user enter a date in a pop-up first??
        // is there a more convenient way to display the slides? perhaps a description/theme/tags for each slide would be apt
        // remember error messages if images or media can't be found
    }



    public void saveNewSlideEventToDB(SlideEvent slideEvent){
        DatabaseSaveAndGet.saveNewEventSlide(slideEvent);
    }



    public void savePresentation(ArrayList<TabNode> tabNodePresentation, String chosenDate){

        ArrayList<Slide> slidePresentation = new ArrayList<>();


        for(TabNode tabNode : tabNodePresentation){

            Slide slide = tabNode.getSlide();

            slidePresentation.add(slide);


            if(slide.getImagePath() != null){  // todo check if the null check is correct, need to check on empty string and null string?
<<<<<<< HEAD
                System.out.println("Filepath sent to copyFileToDrive: " + Util.turnBackslashToForward(slide.getImagePath()));
=======
>>>>>>> 8edcc97ee4de0ad6dc42a6f5979c900112c48156
                slide.setImagePath(copyFileToDrive(Util.turnBackslashToForward(slide.getImagePath())));
            }

        }

        DatabaseSaveAndGet.savePresentation(slidePresentation, chosenDate);
    }

    public String copyFileToDrive(String filePath){

        File file = new File(Util.turnBackslashToForward(filePath));

        System.out.println("Filepath: " + filePath);
        System.out.println("File: " + file);

        FileInputStream in = null;
        FileOutputStream out = null;

        try{
            in = new FileInputStream(filePath);
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

        File copiedFile = new File("FileServer/"+file.getName());

        filePath = "file:///"+copiedFile.getAbsoluteFile().toString();

        System.out.println("New Copied File: " + copiedFile);
        System.out.println("New file path: " + filePath);

        return filePath;

    }


    public void deleteEvent(SlideEvent slideEvent){

        DatabaseSaveAndGet.deleteEventSlide(slideEvent);

    }



}
