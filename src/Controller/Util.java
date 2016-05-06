package Controller;

/**
 * Created by Anders on 5/6/2016.
 */
public class Util {


    public static String turnBackslashToForward(String textToParse){

        textToParse = textToParse.replaceAll("\\\\", "/");

        return textToParse;

/*        StringBuilder stringToBuild = new StringBuilder();

            for(int i = 0; i < textToParse.length(); i++){

                if (textToParse.charAt(i) == '\\'){
                    stringToBuild.append("//");
                } else{
                    stringToBuild.append(textToParse.charAt(i));
                }

            }

            return stringToBuild.toString();*/
    }


}
