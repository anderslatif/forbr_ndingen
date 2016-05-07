package Controller;

/**
 * Created by Anders on 5/6/2016.
 */
public class Util {


    public static String turnBackslashToForward(String textToParse) {

        textToParse = textToParse.replaceAll("\\\\", "/");

        return textToParse;
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

}
