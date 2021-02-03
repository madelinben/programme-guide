package com.sabaton.Helper;

public class DisplayHelper {

    /**
     * Adds whitespaces to column.
     * @param    columnLength    Index value that identifies the length of the column.
     * @param    valueLength    Index value that identifies the length of the object to go into column.
     * @return           String of whitespaces.
     * @see      String
     **/
    public static String missingSpaces(int columnLength, int valueLength){
        String returnString = "";
        for(int i=0; i < (columnLength - valueLength); i++){
            if(i + valueLength == columnLength){
                returnString += " ";
                break;
            }
            else{
                returnString += " ";
            }
        }
        return returnString;
    }
    /**
     * Truncates <code>string</code> to be <code>length</code> if it is bigger than <code>length</code>.
     * @param    string    String to truncate.
     * @param    length    Length of the string.
     * @return           The string truncated if it needed to be.
     * @see      String
     **/
    public static String truncate(String string, int length){
        if(string.length() > length){
            if(length <= 3){
                String output = "";
                for (int i = 0; i < length; i++){
                    output += ".";
                }
                return output;
            }
            else{
                return string.substring(0, Math.min(string.length(), (int)length) - 3) + "...";
            }
        }
        else {
            return string;
        }
    }
}
