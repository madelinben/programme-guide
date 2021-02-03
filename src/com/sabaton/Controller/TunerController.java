package com.sabaton.Controller;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.sabaton.GlobalVariables;
import com.sabaton.Model.Programme;
import com.sabaton.Service.ScheduleService;
import com.sabaton.Service.TunerService;

import static com.sabaton.GlobalVariables.*;

public class TunerController {
    /**
     * Scanner variable for whole controller.
     **/
    public static Scanner scanner = new Scanner(System.in);
    /**
     * Displays tuner menu to user.
     **/
    public static void displayTunerMenu() throws Exception{
        TimeUnit.SECONDS.sleep(3);
        System.out.println("Please select from the following options:\n1. Set Tuner Amount\n2. View Tuner Amount\n3. Reset Tuner Count To Default\n4. Exit Tuner Menu");
        String stringInput = scanner.nextLine();
        char[] inputString = stringInput.toCharArray();
        if(inputString.length != 1){
            System.out.println("Please only enter 1 character to select a menu item.");
            displayTunerMenu();
        }
        if(String.valueOf(inputString[0]).matches("^[0-9]+$")){
            switch(Integer.parseInt(String.valueOf(inputString[0]))){
                case 1:
                    System.out.println("Current Tuner amount: "+ numTuners.tuner+ "\nPlease input how many tuners you would like.");
                    stringInput = scanner.nextLine();
                    if(stringInput.matches("^[0-9]+$")) {
                        int input = Integer.parseInt(stringInput);
                        if(input > channels.size() ) {
                            input = channels.size();
                            System.out.println("Tuner input higher than amount of channels, setting to\nhighest possible amount.");
                        }
                        TunerService.updateTuner(input);
                        System.out.println("Current tuner count now: " + numTuners.tuner + "\nGoing back to main menu.");
                    } else {
                        System.out.println("Invalid input.");
                        displayTunerMenu();
                    }
                    break;
                case 2:
                    System.out.println("Current Tuner Amount: "+numTuners.tuner);
                    break;
                case 3:
                    TunerService.updateTuner(3);
                    System.out.println("Tuner count set to default(3).");
                    break;
                case 4:
                    System.out.println("Exiting menu.");
                    HomeController.displayMenu();
                    break;
                default:
                    System.out.println("Please pick a valid option.");
                    displayTunerMenu();
                    break;
            }
        }
        else{
            System.out.println("Invalid input, please try again.");
        }
        displayTunerMenu();
    }
    /**
     * Finds an alternative programme when addProgramme can't find an appropriate programme.
     *
     * @param choice             user programme choice used as index
     * @param selectedProgramme  programme user has selected
     * @param matchingProgrammes programme alternative that matches selectedProgramme
     */
    public static void findAlternative(int choice, Programme selectedProgramme, ArrayList<Programme> matchingProgrammes) {
        boolean altFound = false;
        for (int k = choice + 1; k < matchingProgrammes.size(); k++) {
            Programme altProgramme = matchingProgrammes.get(k);
            if (altProgramme.title.equals(selectedProgramme.title) && altProgramme.start.isAfter(selectedProgramme.start) && !altProgramme.isRecording && altProgramme.start.isAfterNow()) {
                altFound = true;
                System.out.println("Programme couldn't be Recorded!\nHowever, would you like to record " + altProgramme.title + " at " + String.format("%02d:%02d ", altProgramme.start.getHourOfDay(), altProgramme.start.getMinuteOfHour()) + altProgramme.channelDate.getDayOfMonth() + "/" + altProgramme.channelDate.getMonthOfYear() + "/" + altProgramme.channelDate.getYear() + " on " + altProgramme.channel + "? (y/n)");
                boolean valid = false;
                while (!valid) {
                    String userInput = scanner.nextLine().trim().toLowerCase();
                    if (userInput.equals("y")) {
                        valid = true;
                        System.out.println("Programme Added");
                        GlobalVariables.scheduleData.scheduledProgrammes.add(altProgramme);
                        ScheduleService.applyTag(altProgramme);
                        selectedProgramme.isRecording = true;
                    } else if (userInput.equals("n")) {
                        valid = true;
                        System.out.println("Programme wasn't added for record!");
                    } else {
                        System.out.println("Sorry, Please enter (y/n)!");
                    }
                }
                break;
            }
        }
        if (!altFound) {
            System.out.println("Sorry, No alternative Programmes were identified!");
        }
    }
}
