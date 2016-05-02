package Controller;

import Model.DatabaseSaveAndGet;
import Model.TabNode;

import java.io.File;
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

    public void savePresentation(ArrayList<TabNode> presentation){

        // pop-up asks for date

        DatabaseSaveAndGet databaseSaveAndGet = new DatabaseSaveAndGet();
        databaseSaveAndGet.savePresentation(presentation);
    }



/*    public void changeSlidePosition(int index){

    }*/





}
