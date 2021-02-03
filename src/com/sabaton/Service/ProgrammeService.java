package com.sabaton.Service;

import com.sabaton.*;
import com.sabaton.Model.*;
import com.sabaton.Repository.*;
import org.joda.time.*;
import java.io.*;
import java.util.*;

public class ProgrammeService {
    /**
     * Gets the programmes needed for the grid for selected channel.
     * @param    channelName    Name value of the channel selected.
     * @return           The processed channel and related programme data is appended to a string representing the final formatted row.
     * @see      ArrayList<Programme>
     **/
    public static ArrayList<Programme> getGridData(String channelName) {
        ArrayList<Programme> outputList = new ArrayList<Programme>();
        if(GlobalVariables.timePointer > 23) {
            GlobalVariables.timePointer = 0;
        } else if(GlobalVariables.timePointer < 0) {
            GlobalVariables.timePointer = 23;
        }
        DateTime datePointer = DateTime.now().withTimeAtStartOfDay().hourOfDay().setCopy(GlobalVariables.timePointer).minuteOfHour().setCopy(0).secondOfMinute().setCopy(0).millisOfSecond().setCopy(0);
        DateTime lowerBound = datePointer.plusDays(GlobalVariables.displayDay);
        DateTime upperBound = lowerBound.plusHours(3);
        try {
            for (int i=0; i<GlobalVariables.xmlData.size(); i++) {
                Channel channelObj = GlobalVariables.xmlData.get(i);
                if (channelObj.name.equalsIgnoreCase(channelName) && channelObj.channelDate.getDayOfMonth() == DateTime.now().plusDays(GlobalVariables.displayDay).getDayOfMonth()) {
                    ArrayList<Programme> programmeArrayList = channelObj.channelProgrammes;
                    for (int j=0; j<programmeArrayList.size(); j++) {
                        Programme progObj = programmeArrayList.get(j);
                        if ((progObj.start.getHourOfDay() <= progObj.end.getHourOfDay() || (progObj.end.getDayOfMonth() == DateTime.now().plusDays(GlobalVariables.displayDay + 1).getDayOfMonth() && progObj.end.getHourOfDay() < progObj.start.getHourOfDay())) && ((progObj.start.isBefore(lowerBound) && progObj.end.isAfter(upperBound)) || (progObj.start.isBefore(lowerBound) && progObj.end.isAfter(lowerBound)) || (progObj.start.isAfter(lowerBound) && progObj.end.isBefore(upperBound)) || (progObj.start.isBefore(upperBound) && progObj.end.isAfter(upperBound) || (progObj.start.getHourOfDay() == lowerBound.getHourOfDay() && (progObj.end.isBefore(upperBound) || progObj.end.isAfter(upperBound))) || (progObj.end.getHourOfDay() == upperBound.getHourOfDay() && ((progObj.start == lowerBound) || (progObj.start.isBefore(upperBound) && progObj.start.isAfter(lowerBound))))))) {
                            outputList.add(progObj);
                        }
                    }
                }
            }
        } catch(Exception exception) {
            System.out.println("Error on grid load.");
        }
        ArrayList<Integer> conflictList = new ArrayList<>();
        for(int j=1; j<outputList.size(); j++) {
            Programme prevProg = outputList.get(j - 1);
            Programme currentProg = outputList.get(j);
            if ((prevProg.start == currentProg.start) && (prevProg.end == currentProg.end)) {
                conflictList.add(j);
            }
        }
        for(int index : conflictList) {
            outputList.remove(index);
        }
        return outputList;
    }

    /**
     * method loops through each channels programmes, checks them against the user input and returns the best result.
     * @param userInput catch all userInput variable.
     * @return closestProgramme
     **/
    public static Programme getProgramme(String userInput){
        ArrayList<Channel> channelList = GlobalVariables.xmlData;
        Programme programme = new Programme();
        boolean counter = true;
        for (int i=0; i < channelList.size(); i++){
            for (int j=0; j < channelList.get(i).channelProgrammes.size(); j++){
                if (channelList.get(i).channelProgrammes.get(j).title.equalsIgnoreCase(userInput) && counter){
                    programme = channelList.get(i).channelProgrammes.get(j);
                    counter = false;
                }
            }
        }
        return programme;
    }

    /**
     * Returns list of programmes that contain any of the items in the searchData parameter.
     * @param searchData String array of search data.
     * @return ArrayList of matching programmes.
     */
    public static ArrayList<Programme> searchProgrammes(String[] searchData){
        ArrayList<Channel> channelList = GlobalVariables.xmlData;
        ArrayList<Programme> matchingProgrammes = new ArrayList<Programme>();
        for (Channel channel : channelList){
            for (Programme programme : channel.channelProgrammes){
                boolean match = true;
                for (String keyword : searchData) {
                    if (keyword.length()>2){
                        if (!programme.title.toLowerCase().contains(keyword.toLowerCase())) {
                            match = false;
                            break;
                        }
                    }
                }
                if (match && !matchingProgrammes.contains(programme)){
                    matchingProgrammes.add(programme);
                }
            }
        }
        return matchingProgrammes;
    }
    /**
     * Returns list of programmes that equals the string in the searchData parameter.
     * @param searchData String of search data.
     * @return ArrayList of matching programmes.
     */
    public static ArrayList<Programme> searchProgrammes(String searchData){
        ArrayList<Channel> channelList = GlobalVariables.xmlData;
        ArrayList<Programme> matchingProgrammes = new ArrayList<Programme>();
        for (Channel channel : channelList){
            for (Programme programme : channel.channelProgrammes){
                String searchTerm = "";
                boolean change = false;
                if (programme.title.contains("(R)")) {
                    searchTerm = programme.title.replace("(R)","");
                    change = true;
                }
                if (programme.title.contains("(F)") && change) {
                    searchTerm = searchTerm.replace("(F)","");
                    change = true;
                }
                else if (programme.title.contains("(F)") && !change){
                    searchTerm = programme.title.replace("(F)","");
                    change = true;
                }
                if(!change){
                    searchTerm = programme.title;
                }
                if (searchTerm.equals(searchData)){
                    matchingProgrammes.add(programme);
                }
            }
        }
        return matchingProgrammes;
    }

    /**
     * All interaction with repository source files and processing is completed in ProgrammeRepository and the product will be returned to the main application structure. Sets global variable for all channels.
     **/
    public static void importXml() {
        ProgrammeRepository.importXml();
    }

    /**
     * Method to call getLiveData.
     **/
    public static void getLiveData() throws org.json.simple.parser.ParseException, javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.text.ParseException, java.net.MalformedURLException, java.io.IOException, java.lang.InterruptedException{
        ProgrammeRepository.getLiveData();
    }

    /**
     * Does the system current time fall between the programme start time and end time?
     * @param programme programme object.
     * @return is programme airing.
     * @see boolean
     * */
    public static boolean isProgrammeAiring(Programme programme){
        if (programme.start.isBeforeNow() && programme.end.isAfterNow()){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Buffer reads file line by line, replacing special characters with the escaped notation and builds string containing validated xml.
     * @param    path    String contains the library path and xml filename.
     * @return           Constructed string contains validated xml, which can now be manipulated.
     * @see      String
     **/
    public static String escapeSpecialChar(String path) {
        String xmlString = "";
        try {
            BufferedReader readBuffer = new BufferedReader(new FileReader(new File(path)));
            StringBuilder validString = new StringBuilder();
            String line;
            while((line = readBuffer.readLine())!= null){
                String validLine = line.replaceAll("&", "&amp;");
                validString.append(validLine.trim());
            }
            xmlString = validString.toString();
            readBuffer.close();
        } catch (Exception exception) {
            System.out.println("Error! Replacing XML special characters!");
        }
        return xmlString;
    }

    /**
     * Returns formatted title based on whether the programme is recorded or a favourite.
     * @param programme programme whose title is needed.
     * @return formatted title.
     */
    public static String getFormattedTitle(Programme programme){
        String formattedTitle = programme.title;
        if (programme.isRecording) {
            formattedTitle = "(R)" + formattedTitle;
        }
        if (programme.isFavourite) {
            formattedTitle = "(F)" + formattedTitle;
        }
        return formattedTitle;
    }
}
