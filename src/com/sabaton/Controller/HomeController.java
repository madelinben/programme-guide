package com.sabaton.Controller;

import com.sabaton.*;
import com.sabaton.Service.*;
import org.joda.time.*;
import org.joda.time.format.*;
import org.joda.time.format.*;

import java.util.*;
import java.util.concurrent.*;

import static com.sabaton.GlobalVariables.*;

public class HomeController {
    /**
     * Scanner variable for whole controller.
     **/
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        ChannelService.getChannelNameIntialise();
        ScheduleService.getSchedule();
        FavouriteService.getFavourites();
        ProgrammeService.getLiveData();
        FavouriteService.getAutoRecord();
        TunerService.getTuner();
        displayDay = 0;
        channelPointer = 0;
        Thread thread = new Thread(){
            public void run(){
                try{
                    TunerService.updateTuner(numTuners.tuner);
                    FavouriteService.saveFavourites();
                    ScheduleService.saveSchedule();
                }
                catch (Exception exception){
                    System.out.println("Cannot save Schedule on exit.");
                }
            }
        };
        Runtime.getRuntime().addShutdownHook(thread);
        timePointer = DateTime.now().getHourOfDay();
        System.out.println("Welcome to Sabaton EPG");
        ChannelController.displayGrid();
        displayMenu();
    }
    /**
     * Produces a clear description of operation keys and alters pointers to modify grid contents based on user input.
     **/
    public static void displayMenu() throws java.lang.Exception{
        TimeUnit.SECONDS.sleep(3);
        System.out.println("Main Menu:\nH - Home\nF - Forward 2 Hrs\nB - Back 2 Hrs\nG - Forward 24 Hrs\nA - Back 24 Hrs\nU - Up 4 Channels\nD - Down 4 Channels\nN - What's on now?\nS - Search\nP - Programme Details\nR - Schedule Menu\nT - Tuner Menu\nV - Favourites Menu\nQ - Quit");
        String operation = new Scanner(System.in).nextLine().trim().toLowerCase();
        char[] input = operation.toCharArray();
        if(input.length != 1){
            System.out.println("Please only enter 1 character to select a menu item.");
            displayMenu();
        }
        switch (input[0]) {
            case 'h':
                displayDay = 0;
                timePointer = DateTime.now().getHourOfDay();
                ChannelController.displayGrid();
                TimeUnit.SECONDS.sleep(3);
                break;
            case 'f':
                timePointer += 2;
                if(timePointer >= 24){
                    timePointer -= 24;
                    displayDay++;
                }
                if(displayDay>2){
                    displayDay=2;
                    timePointer=23;
                }
                ChannelController.displayGrid();
                break;
            case 'b':
                timePointer -= 2;
                if(timePointer < 0){
                    timePointer += 24;
                    displayDay--;
                }
                if(displayDay<-1){
                    displayDay=-1;
                    timePointer=0;
                }
                ChannelController.displayGrid();
                break;
            case 'u':
                channelPointer -= 4;
                ChannelController.displayGrid();
                break;
            case 'd':
                channelPointer += 4;
                ChannelController.displayGrid();
                break;
            case 'p':
                ProgrammeController.getProgrammeSelection();
                break;
            case 'n':
                ProgrammeController.getNowAndNext();
                break;
            case 'q':
                scanner.close();
                System.exit(0);
                break;
            case 's':
                ProgrammeController.programmeSearch(false);
                break;
            case 'r':
                ScheduleController.displayScheduleMenu();
                break;
            case 'v':
                FavouriteController.displayFavouriteMenu();
                break;
            case 'a':
                if(displayDay <= -1){
                    System.out.println("There are no more days to skip.");
                }else{
                    displayDay--;
                    ChannelController.displayGrid();
                }
                break;
            case 'g':
                if(displayDay >= 2){
                    System.out.println("There are no more days to skip.");
                }else{
                    displayDay++;
                    ChannelController.displayGrid();
                }
                break;
            case 't':
                TunerController.displayTunerMenu();
                break;
            default:
                System.out.println("ERROR! User didn't select valid input case!");
                break;
        }
        displayMenu();
    }



}