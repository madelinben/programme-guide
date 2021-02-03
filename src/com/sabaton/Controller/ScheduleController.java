package com.sabaton.Controller;

import com.sabaton.*;
import com.sabaton.Helper.*;
import com.sabaton.Model.*;
import com.sabaton.Service.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.sabaton.GlobalVariables.*;

public class ScheduleController {
    /**
     * Scanner variable for whole controller.
     **/
    public static Scanner scanner = new Scanner(System.in);
    /**
     * Adds programme to schedule.
     * @param matchingProgrammes list of search results.
     */
    public static void addSchedule(ArrayList<Programme> matchingProgrammes) throws Exception{
        boolean schedule = true;
        while (schedule) {
            System.out.println("Would you like to Schedule a Programme for recording? (y/n)");
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("yes") || userInput.equalsIgnoreCase("y")) {
                boolean gridValue = true;
                while (gridValue) {
                    scanner.reset();
                    System.out.println("Please enter the index value of the programme you want to be a scheduled programme.");
                    String input = scanner.nextLine();
                    if (input.matches("^[0-9]+$")) {
                        int choice = Integer.parseInt(input) - 1;
                        if (choice < matchingProgrammes.size()) {
                            Programme selectedProgramme = matchingProgrammes.get(choice);
                            boolean validSchedule = TunerService.validateTuner(selectedProgramme);
                            if (!selectedProgramme.isRecording && selectedProgramme.start.isAfterNow()) {
                                if(validSchedule) {
                                    System.out.println("Programme added.");
                                    GlobalVariables.scheduleData.scheduledProgrammes.add(selectedProgramme);
                                    ScheduleService.applyTag(selectedProgramme);
                                    selectedProgramme.isRecording = true;
                                } else {
                                    System.out.println("There are No Tuners Available to record this Programme at this time.");
                                    TunerController.findAlternative(choice,selectedProgramme,matchingProgrammes);
                                }
                            } else if (selectedProgramme.start.isBeforeNow()){
                                System.out.println("Cannot record programme from the past.");
                            } else {
                                TunerController.findAlternative(choice,selectedProgramme,matchingProgrammes);
                            }
                            gridValue = false;
                        }
                    } else {
                        System.out.println("Please make sure that value you input is a whole number.");
                    }
                }
                schedule = false;
            } else if (userInput.equalsIgnoreCase("no") || userInput.equalsIgnoreCase("n")) {
                HomeController.displayMenu();
            } else {
                System.out.println("This is an invalid input, please try again.");
            }
        }
    }
    /**
     * displays schedule menu for user.
     */
    public static void displayScheduleMenu() throws Exception {
        TimeUnit.SECONDS.sleep(3);
        System.out.println("Schedule Menu\nV - View Current Schedule\nR - Remove a Programme\nQ - Return to Main Menu");
        String operation = new Scanner(System.in).nextLine().trim().toLowerCase();
        char[] input = operation.toCharArray();
        if (input.length != 1) {
            System.out.println("Please only enter 1 character to select a menu item.");
            displayScheduleMenu();
        }
        switch (input[0]) {
            case 'v':
                displayScheduleList();
                break;
            case 'r':
                removeProgramme();
                break;
            case 'q':
                HomeController.displayMenu();
                break;
            default:
                System.out.println("Please enter a valid option.");
                displayScheduleMenu();
                break;

        }
        displayScheduleMenu();
    }


    /**
     * Remove Programme function, which prints out all schedule and deletes the ArrayList item of int choice.
     */
    public static void removeProgramme() {
        if(scheduleData.scheduledProgrammes.size() == 0){
            System.out.println("No Scheduled Programmes.");
        }
        else{
            displayScheduleList();
            System.out.println("Please enter the index value of the programme you want to remove from schedule.");
            boolean gridInput = true;
            while (gridInput) {
                String choiceString = scanner.nextLine();
                if(choiceString.matches("^[0-9]+$")){
                    int choice = Integer.parseInt(choiceString) - 1;
                    if (choice <= GlobalVariables.scheduleData.scheduledProgrammes.size()) {
                        gridInput = false;
                        GlobalVariables.scheduleData.scheduledProgrammes.get(choice).isRecording = false;
                        ScheduleService.removeTag(GlobalVariables.scheduleData.scheduledProgrammes.get(choice));
                        ScheduleService.removeProgramme(choice);
                    } else {
                        System.out.println("Invalid Choice.");
                    }
                }
                else{
                    System.out.println("Please enter a valid option.");
                }
            }
        }
    }

    /**
     * Displays all of the programmes in the schedule array.
     */
    public static void displayScheduleList(){
        if(scheduleData.scheduledProgrammes.size() == 0){
            System.out.println("No Scheduled Programmes.");
        }
        else{
            System.out.println("|  INDEX  |               NAME                |  CHANNEL   |        START       |         END        |");
            for (int i = 0; i < GlobalVariables.scheduleData.scheduledProgrammes.size(); i++) {
                Programme programme = GlobalVariables.scheduleData.scheduledProgrammes.get(i);
                int j = i + 1;
                System.out.println("|"+ j + DisplayHelper.missingSpaces(9, String.valueOf(j).length()) + "|" + DisplayHelper.truncate(ProgrammeService.getFormattedTitle(programme), 34) + DisplayHelper.missingSpaces(35, DisplayHelper.truncate(ProgrammeService.getFormattedTitle(programme), 35).length()) + "|" + DisplayHelper.truncate(programme.channel, 12) + DisplayHelper.missingSpaces(12, DisplayHelper.truncate(programme.channel, 12).length()) + "|" + String.format("  %02d-%02d-%04d %02d:%02d  |  %02d-%02d-%04d %02d:%02d  |", programme.start.getDayOfMonth(), programme.start.getMonthOfYear(), programme.start.getYear(), programme.start.getHourOfDay(), programme.start.getMinuteOfHour(), programme.end.getDayOfMonth(), programme.end.getMonthOfYear(), programme.end.getYear(), programme.end.getHourOfDay(), programme.end.getMinuteOfHour()));
            }
        }
    }
}
