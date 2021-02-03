package com.sabaton.Service;

import com.sabaton.*;
import com.sabaton.Helper.*;
import com.sabaton.Model.*;
import org.joda.time.*;
import java.util.*;

public class ChannelService {

    /**
     * Stores the unique Channel id values as a dictionary, ensuring a specific order.
     **/
    public static void getChannelNameIntialise(){
        GlobalVariables.channels = new Hashtable<Integer, String>();
        GlobalVariables.channels.put(0, "bbc1");
        GlobalVariables.channels.put(1, "bbc2");
        GlobalVariables.channels.put(2, "itv1_hd");
        GlobalVariables.channels.put(3, "ch4");
        GlobalVariables.channels.put(4, "five");
        GlobalVariables.channels.put(5, "e4");
        GlobalVariables.channels.put(6, "film_four");
        GlobalVariables.channels.put(7, "cbbc");
        GlobalVariables.channels.put(8, "p_itv2");
        GlobalVariables.channels.put(9, "more4");
        GlobalVariables.channels.put(10, "qvc");
        GlobalVariables.channels.put(11, "dave");
    }
    /**
     * Channel id can be referenced by incrementing through the index to follow specific order when navigating.
     * @param    channelId    Index value that identifies the Channel Id.
     * @return           String identifying specific Channel in xmlData.
     * @see      String
     **/
    public static String getChannelName(int channelId){
        return GlobalVariables.channels.get(channelId);
    }

    /**
     * Scales Channel id and relevant programme data within grid boundaries according to time period.
     * @param    channelName    Formatted in the left most column in order to clearly identify related data.
     * @param    programmes    ArrayList contains the relevant programme data within the time boundary of the grids current location.
     * @return           String is appended with truncated and scaled programme titles.
     * @see      String
     **/
    public static String getChannelDisplay(String channelName, ArrayList<Programme> programmes){
        String outputString = "";
        outputString += "|  " + DisplayHelper.truncate(channelName, 8) + DisplayHelper.missingSpaces(11, DisplayHelper.truncate(channelName, 8).length() + 2) + "|";

        for (Programme programme : programmes){
            if(programme.start.getHourOfDay() < GlobalVariables.timePointer && programme.start.getDayOfMonth() == DateTime.now().plusDays(GlobalVariables.displayDay).getDayOfMonth()){
                programme.start = programme.start.withTimeAtStartOfDay().plusHours(GlobalVariables.timePointer);
            }
            long difference;
            int lineLength;
            if(programme.start.getHourOfDay() < GlobalVariables.timePointer && programme.start.getDayOfMonth() == DateTime.now().plusDays(GlobalVariables.displayDay).getDayOfMonth()){
                DateTime start = programme.start.withTimeAtStartOfDay().plusHours(GlobalVariables.timePointer);
                difference = programme.end.getMillis() - start.getMillis();
            } else if((GlobalVariables.displayDay == -1 && GlobalVariables.timePointer == 0) && (programmes.indexOf(programme) == 0 && (programme.start.getHourOfDay() > GlobalVariables.timePointer))){
                difference = programme.start.toDateTime().getMillis() - (GlobalVariables.timePointer * 60*60*1000);
            } else {
                int timePointer;
                if(GlobalVariables.timePointer + 3 >= 24){
                    timePointer = GlobalVariables.timePointer + 3 - 24;
                }
                else{
                    timePointer = GlobalVariables.timePointer + 3;
                }
                DateTime end;
                if(programme.end.getDayOfMonth() == DateTime.now().plusDays(GlobalVariables.displayDay).getDayOfMonth()){
                    end = programme.end.withTimeAtStartOfDay().plusHours(GlobalVariables.timePointer + 3);
                }
                else{
                    end = programme.end.withTime(timePointer,0,0,0);
                }
                if(programme.end.getMillis() > end.getMillis()){
                    difference = end.getMillis() - programme.start.getMillis();
                } else {
                    difference = programme.end.getMillis() - programme.start.getMillis();
                }
            }

            long diffMins;
            long diffHours;
            if(difference >= 3600000){
                diffMins = difference / (60 * 1000) % 60;
                diffHours = difference / (60 * 60 * 1000);
            }
            else{
                diffMins = difference / (60 * 1000) % 60;
                diffHours = diffMins / (60 * 60 * 1000);
            }
            if(diffHours < 0){
                diffHours += 24;
            }
            long diff5 = (diffMins + diffHours * 60) / 5;
            long length = diff5 * 4;
            if (length < 0) {
                length = length * -1;
            }
            if(length % 4 == 0){
                length--;
            }

            String outputValue = "";
            if((GlobalVariables.displayDay == -1 && GlobalVariables.timePointer == 0) && (programmes.indexOf(programme) == 0 && (programme.start.getHourOfDay() > GlobalVariables.timePointer))){
                outputValue = DisplayHelper.truncate("NO PROGRAMME DATA FOUND!", (int)length) + DisplayHelper.missingSpaces((int)length, DisplayHelper.truncate(ProgrammeService.getFormattedTitle(programme), 48).length());
            } else {
                outputValue = DisplayHelper.truncate(ProgrammeService.getFormattedTitle(programme), (int)length) + DisplayHelper.missingSpaces((int)length, DisplayHelper.truncate(ProgrammeService.getFormattedTitle(programme), 48).length());
            }
            if(outputValue.length() % 4 == 0){
                outputValue = outputValue.substring(0, outputValue.length() - 1);
            }
            outputString += outputValue;
            int hourCounter = 0;
            for(Programme programme1 : programmes){
                if(programme1.start.getHourOfDay() == programme.start.getHourOfDay() && (programme.start.getHourOfDay() == GlobalVariables.timePointer + 1 && hourCounter == 0)){
                    hourCounter += 1;
                }
                else if (programme1.start.getHourOfDay() == programme.start.getHourOfDay() || programme1.start.getHourOfDay() <= GlobalVariables.timePointer){
                    hourCounter += 1;
                }
            }
            ArrayList<Programme> thisHour = new ArrayList<>();
            for(Programme counterProgramme : programmes){
                if(counterProgramme.start.getHourOfDay() == programme.start.getHourOfDay()){
                    thisHour.add(counterProgramme);
                }
            }
            lineLength = outputString.length();
            if(lineLength >= 157){
                int diffLength = lineLength - 157;
                for(int i = 1; i < diffLength + 2 ; i++){
                    outputString = outputString.substring(0, outputString.length() - 1);
                }
            }
            else if(programmes.indexOf(programme) == programmes.size() - 1 && lineLength < 157){
                for(int i = 1; i < 157 - lineLength; i++){
                    outputString += " ";

                }
            }
            outputString += "|";
        }
        return outputString;
    }
}
