package Controller;

import Model.*;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Anders on 4/15/2016.
 */
public class Controller {

    // ArrayList<Slide> presentation = new ArrayList();

    // the list above will be the one we use.. the one below is to try out the database






    public void openPresentation(){
        // let the user enter a date in a pop-up first??
        // is there a more convenient way to display the slides? perhaps a description/theme/tags for each slide would be apt
        // remember error messages if images or media can't be found
    }

    public void savePresentation(ArrayList<TabNode> tabNodePresentation, String chosenDate){

        ArrayList<Slide> slidePresentation = new ArrayList<>();

        System.out.println(tabNodePresentation.size());

        for(TabNode tabNode : tabNodePresentation){

            slidePresentation.add(tabNode.getSlide());

            if (tabNode.getSlide() instanceof SlideEvent){
                System.out.println("This tabnode is an event: " + tabNode.getSlide());
            } else if (tabNode.getSlide() instanceof  SlideHappyHour){
                System.out.println("This tabnode is happy hour: " + tabNode.getSlide());
            } else if (tabNode.getSlide() instanceof  SlidePicture){
                System.out.println("This tabnode is a picture tab: " + tabNode.getSlide());
            }
        }


        DatabaseSaveAndGet databaseSaveAndGet = new DatabaseSaveAndGet();
        databaseSaveAndGet.savePresentation(slidePresentation, chosenDate);
    }



/*    public void changeSlidePosition(int index){

    }*/

    public void saveNewSlideEventToDB(SlideEvent slideEvent){
        DatabaseSaveAndGet.saveNewEventSlide(slideEvent);

    }





}
