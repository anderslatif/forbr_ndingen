package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Anders on 5/6/2016.
 */
public class Util {

    /**
     * Is used for windows because filepath in Windows uses backslashes while both Java and MySQL uses forward slashes.
     * Necessary to make the program platform independent.
     * @param stringToParse
     * @return
     */
    public static String turnBackslashToForward(String stringToParse) {

        if (stringToParse.equals("") || stringToParse.equals("null") || stringToParse == null ){
            stringToParse = "";
        } else {
            stringToParse = stringToParse.replaceAll("\\\\", "/");
        }

        return stringToParse;
    }


    /**
     * Used to avoid MySQL injections.
     * @param stringToParse
     * @return
     */
    public static String escapeApostrophe(String stringToParse){

        if (stringToParse.equals("") || stringToParse.equals("null") || stringToParse == null ){
            stringToParse = stringToParse.replaceAll("'", "\\\\'");
            stringToParse = stringToParse.replaceAll("\"", "\\\\\"");
        }

        return stringToParse;

    }

    /**
     * In case it might be needed.
     * @param localDate
     * @return
     */
    public static String parseLocalDateToDateOnlyString(LocalDate localDate){

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM");
        String date;

        if(localDate != null){
            date = dateTimeFormatter.format(localDate);
        } else {
            date = "";
        }

        return date;
    }

    /**
     * It might be necessary to check the operating system to check the hardware the program is running on.
     * @return
     */
    public static String checkOperatingSystem(){

        String operatingSystem = System.getProperty("os.name");

        return operatingSystem;
    }

}
