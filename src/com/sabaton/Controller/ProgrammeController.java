package com.sabaton.Controller;

import com.sabaton.Helper.*;
import com.sabaton.Model.*;
import com.sabaton.Service.*;
import org.joda.time.*;
import java.util.*;

import static com.sabaton.GlobalVariables.*;

public class ProgrammeController {
    /**
     * Scanner variable for whole controller.
     **/
    public static Scanner scanner = new Scanner(System.in);
    /**
     * Method asks user for an input, then checks the variable against programmes in arraylist.
     **/
    public static void getProgrammeSelection() {
        System.out.println("Please input a programme name");
        String userInput = scanner.nextLine();
        Programme programme = ProgrammeService.getProgramme(userInput);
        if (programme.title == null) {
            System.out.println("No programme could be found");
        } else {
            displayProgrammeDetails(programme);
        }
    }

    /**
     * Method prints out selected programme details.
     * @param programme programme object to display.
     **/
    public static void displayProgrammeDetails(Programme programme) {
        System.out.println("Here's the closest programme to your input:");
        System.out.println("Channel Name: " + programme.channel);
        System.out.println("Title: " + programme.title);
        System.out.println("Description: " + programme.description);
        System.out.println("Start Time: " + programme.start.getHourOfDay() + ":" + programme.start.getMinuteOfHour() + " " + programme.start.getDayOfMonth() + "/" + programme.start.getMonthOfYear() + "/" + programme.start.getYear());
        System.out.println("End Time: " + + programme.end.getHourOfDay() + ":" + programme.end.getMinuteOfHour() + " " + programme.end.getDayOfMonth() + "/" + programme.end.getMonthOfYear() + "/" + programme.end.getYear());
    }
    /**
     *  Checks if each programme for each channel is airing using the isProgrammeAiring method and then passes them to displayNowAndNext method.
     **/
    public static void getNowAndNext(){
        System.out.println("Enter the name of the channel for which you would like to see the programme on now and on next.\nIf you would like to see this for all channels, please enter \"all\".");
        String channelName = scanner.nextLine();
        while (channelName.equalsIgnoreCase("")){
            System.out.println("Invalid input. Please enter \"all\", or the name of a channel.");
            channelName = scanner.nextLine();
        }
        boolean displayAllChannels = channelName.equalsIgnoreCase("all");
        boolean isHeaderDisplayed = false;
        for (Channel channel : xmlData.subList(12,23)) {
            if (channel.name.toLowerCase().contains(channelName.toLowerCase()) || displayAllChannels) {
                if (!isHeaderDisplayed){
                    System.out.println("|Channel   |Now                                |Next                               |");
                    isHeaderDisplayed = true;
                }
                String now = "No programmes airing on this channel.";
                String next = "No more programmes.";
                for (Programme programme : channel.channelProgrammes) {
                    if (ProgrammeService.isProgrammeAiring(programme)) {
                        now = programme.title;
                        DateTime nowEnd = programme.end;
                        for (Programme otherProgramme : channel.channelProgrammes) {
                            if ((otherProgramme != programme) && otherProgramme.start.equals(nowEnd)) {
                                next = otherProgramme.title;
                                break;
                            }
                        }
                    }
                }
                displayNowAndNext(channel.name, now, next);
            }
        }
        if (!isHeaderDisplayed) {
            System.out.println("No channels could be found.");
        }
    }

    /**
     *Displays currently airing programme and programme after that.
     * @param now current programme
     * @param channel channel name
     * @param next next programme
     **/
    public static void displayNowAndNext(String channel, String now, String next){
        System.out.printf("|%-10s|%-35s|%-35s|%n",channel, DisplayHelper.truncate(now, 35),DisplayHelper.truncate(next, 35));
    }

    /**
     * Search function that searches for programmes and returns a list of best matches.
     * @param favourite determines if call was made from favourites menu or schedule menu.
     */
    public static void programmeSearch(boolean favourite) throws java.lang.Exception{
        int i = 1;
        System.out.println("Enter keywords separated by a hash #: ");
        String rawSearchData = scanner.nextLine();
        int searchLength = 0;
        ArrayList<String> splitSearchData = new ArrayList<String>();
        for (String term : rawSearchData.split("#")) {
            if (!splitSearchData.contains(term)){
                splitSearchData.add(term);
                searchLength += term.length();
            }
        }
        while (searchLength<3){
            System.out.println("Search must contain more than three characters.\nEnter keywords separated by a hash #: ");
            rawSearchData = scanner.nextLine();
            searchLength = 0;
            splitSearchData = new ArrayList<String>();
            for (String term : rawSearchData.split("#")) {
                if (!splitSearchData.contains(term)){
                    splitSearchData.add(term);
                    searchLength += term.length();
                }
            }
        }

        ArrayList<Programme> matchingProgrammes = ProgrammeService.searchProgrammes(rawSearchData.split("#"));
        if (matchingProgrammes.isEmpty() && rawSearchData.contains(" ")){
            matchingProgrammes = ProgrammeService.searchProgrammes(rawSearchData.split("#|\\s"));
        }

        if(matchingProgrammes.size()!=0){
            System.out.println("|  INDEX  |               NAME               |  CHANNEL  |        START       |         END        |");
            for (Programme programme : matchingProgrammes){
                System.out.println("|"+ i + DisplayHelper.missingSpaces(9, String.valueOf(i).length()) + "|" + DisplayHelper.truncate(ProgrammeService.getFormattedTitle(programme),35) + DisplayHelper.missingSpaces(34, DisplayHelper.truncate(ProgrammeService.getFormattedTitle(programme),35).length()) + "|"  + DisplayHelper.truncate(programme.channel,12) + DisplayHelper.missingSpaces(11, DisplayHelper.truncate(programme.channel,12).length()) + "|" + String.format("  %02d-%02d-%04d %02d:%02d  |  %02d-%02d-%04d %02d:%02d  |", programme.start.getDayOfMonth(), programme.start.getMonthOfYear(), programme.start.getYear(), programme.start.getHourOfDay(), programme.start.getMinuteOfHour(), programme.end.getDayOfMonth(), programme.end.getMonthOfYear(), programme.end.getYear(),  programme.end.getHourOfDay(), programme.end.getMinuteOfHour()));
                i++;
            }
            if(!favourite){
                ScheduleController.addSchedule(matchingProgrammes);
            }
            else{
                FavouriteController.addFavourite(matchingProgrammes);
            }
        } else {
            System.out.println("No matching programmes were identified!");
        }
    }
}
