package Controller;

import Model.DatabaseSaveAndGet;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Anders on 4/15/2016.
 */
public class Controller {

    // ArrayList<Slide> presentation = new ArrayList();

    // the list above will be the one we use.. the one below is to try out the database

    private ArrayList<File> presentation = new ArrayList();



    public void updateImageForSlideObjectInList(File file){
        presentation.add(file);
    }


    public void openPresentation(){
        // let the user enter a date in a pop-up first??
        // is there a more convenient way to display the slides? perhaps a description/theme/tags for each slide would be apt
        // remember error messages if images or media can't be found
    }

    public void savePresentation(){

        DatabaseSaveAndGet dsag = new DatabaseSaveAndGet();
        dsag.savePresentation(presentation);
    }

    public void completelyUpdatePresentation(){


    }


/*    public void changeSlidePosition(int index){

    }*/


    // this method will later on be more specific for each slide object type
    public void newSlide(){
        // create a slide object
        // add the object to the ArrayList
        // create a new tab

    }



}
