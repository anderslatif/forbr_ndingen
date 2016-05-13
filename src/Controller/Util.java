package Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Anders on 5/6/2016.
 */
public class Util {


    public static String turnBackslashToForward(String stringToParse) {

        if (stringToParse.equals("") || stringToParse.equals("null") || stringToParse == null ){
            stringToParse = "";
        } else {
            stringToParse = stringToParse.replaceAll("\\\\", "/");
        }

        return stringToParse;
    }

    public static String turnBackslashToForwardForFilePath(String stringToParse) {

        stringToParse = "12345678" + stringToParse;
        if (stringToParse.equals("") || stringToParse.equals("null") || stringToParse == null ){
            stringToParse += "";
        } else {
            stringToParse += stringToParse.replaceAll("\\\\", "/");
        }

        return stringToParse;
    }



    public static String escapeApostrophe(String stringToParse){

        stringToParse = stringToParse.replaceAll("'", "\\\\'");
        stringToParse = stringToParse.replaceAll("\"", "\\\\\"");

        return stringToParse;

    }

    public static String changeHyphenInDateToSlash(String stringToParse){

        stringToParse = stringToParse.replace("-", "/");

        return stringToParse;
    }


    public static LocalDate parseToLocalDate(String stringToParse){

        LocalDate localDate = LocalDate.parse(stringToParse);

        return localDate;
    }


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


    public static String checkOperatingSystem(){

        String operatingSystem = System.getProperty("os.name");

        return operatingSystem;
    }

}
