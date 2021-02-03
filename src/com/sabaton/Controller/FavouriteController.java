package com.sabaton.Controller;

import com.sabaton.Helper.*;
import com.sabaton.Model.*;
import com.sabaton.Service.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.sabaton.GlobalVariables.*;

public class FavouriteController {
    /**
     * Scanner variable for whole controller.
     **/
    public static Scanner scanner = new Scanner(System.in);
    /**
     * displays favourite menu for user.
     */
    public static void displayFavouriteMenu() throws Exception {
        TimeUnit.SECONDS.sleep(3);
        System.out.println("Favourite Menu \nV - View List of Favourite Programmes\nA - Add a Programme\nR - Remove a Programme\nU - Auto Record\nQ - Return to Main Menu");
        String operation = new Scanner(System.in).nextLine().trim().toLowerCase();
        char[] input = operation.toCharArray();
        if (input.length != 1) {
            System.out.println("Please only enter 1 character to select a menu item.");
            displayFavouriteMenu();
        }
        switch (input[0]) {
            case 'v':
                displayFavouriteList();
                break;
            case 'r':
                removeFavourite();
                break;
            case 'a':
                ProgrammeController.programmeSearch(true);
                break;
            case 'u':
                autoRecordOptions();
                break;
            case 'q':
                HomeController.displayMenu();
                break;
            default:
                System.out.println("Please enter a valid option.");
                displayFavouriteMenu();
                break;

        }
        displayFavouriteMenu();
    }
    /**
     * Allows user to update system wide global Auto record settings.
     */
    public static void autoRecordOptions() throws IOException {
        System.out.println("Currently auto recording for favourites is set to: " + Boolean.toString(autoRecord) + "\nWould you like to change this? [Y|N]");
        String userInput = scanner.nextLine();
        if(userInput.equalsIgnoreCase("yes") || userInput.equalsIgnoreCase("y")){
            System.out.println("Please see options below for new value.\nFor True, please enter 1\nFor False, please enter 0");
            String newAutoRecord = scanner.nextLine();
            if(newAutoRecord.matches("^[0-9]+$") && newAutoRecord.equalsIgnoreCase("1")){
                autoRecord = true;
                FavouriteService.updateAutoRecord();
            } else if(newAutoRecord.matches("^[0-9]+$") && newAutoRecord.equalsIgnoreCase("0")){
                autoRecord = false;
                FavouriteService.updateAutoRecord();
            }
            else{
                System.out.println("Invalid input. Please try again.");
            }
        } else if(userInput.equalsIgnoreCase("no") || userInput.equalsIgnoreCase("n")){

        }
        else {
            System.out.println("Invalid input. Please try again.");
            autoRecordOptions();
        }
    }

    /**
     * Remove Programme function, which prints out all favourites and deletes the ArrayList item of int choice.
     */
    public static void removeFavourite() {
        if(favourites.size() == 0){
            System.out.println("No Favourite Programmes.");
        }
        else {
            displayFavouriteList();
            System.out.println("Please enter the index value of the programme you want remove from favourites.");
            boolean gridInput = true;
            while (gridInput) {
                String choiceString = scanner.nextLine();
                if (choiceString.matches("^[0-9]+$")) {
                    int choice = Integer.parseInt(choiceString) - 1;
                    if (choice < favourites.size()) {
                        gridInput = false;
                        if(!autoRecord){
                            FavouriteService.removeFavourite(favourites.get(choice).programmeTitle,favourites.get(choice).autoRecord);
                        }
                        else{
                            FavouriteService.removeFavourite(favourites.get(choice).programmeTitle,true);
                        }
                    } else {
                        System.out.println("Invalid Choice.");
                    }
                } else {
                    System.out.println("Please enter a valid option.");
                }
            }
        }
    }
    /**
     * Adds programme to favourites.
     * @param results list of search results.
     */
    public static void addFavourite(ArrayList<Programme> results) throws Exception {
        System.out.println("Would you like to mark a Programme as a favourite? (y/n)");
        String userInput = scanner.nextLine();
        if (userInput.equalsIgnoreCase("yes") || userInput.equalsIgnoreCase("y")){
            boolean intValue = true;
            while(intValue){
                System.out.println("Please enter the index value of the programme you want to be a favourite.");
                String index = scanner.nextLine();
                if (index.matches("^[0-9]+$")) {
                    int choice = Integer.parseInt(index) - 1;
                    if(choice < results.size()){
                        Favourite favourite = new Favourite();
                        favourite.programmeTitle = results.get(choice).title;
                        if(!autoRecord){
                            boolean auto = true;
                            while(auto) {
                                System.out.println("Would you like to automatically record your favourite? [Y|N]");
                                String autoRecord = scanner.nextLine();
                                if(autoRecord.equalsIgnoreCase("yes") || autoRecord.equalsIgnoreCase("y")){
                                    FavouriteService.addFavourite(results.get(choice).title, true);
                                    auto = false;
                                }else if (autoRecord.equalsIgnoreCase("no") || autoRecord.equalsIgnoreCase("n")){
                                    FavouriteService.addFavourite(results.get(choice).title, false);
                                    auto = false;
                                }else{
                                    System.out.println("Please make sure you enter a valid input.");
                                }
                            }
                            intValue = false;
                        }else{
                            FavouriteService.addFavourite(results.get(choice).title, true);
                            intValue = false;
                        }
                        System.out.println(results.get(choice).title + " has been added to favourites.");
                    }
                    else{
                        System.out.println("Please make sure you enter a valid input.");
                    }
                }
                else{
                    System.out.println("Please make sure you enter a valid input.");
                }
            }
        }
        else if (userInput.equalsIgnoreCase("no") || userInput.equalsIgnoreCase("n")){
            HomeController.displayMenu();
        }
        else{
            System.out.println("Invalid input, please try again.");
            addFavourite(results);
        }

    }
    /**
     * Displays all of the programmes in the favourites array.
     */
    public static void displayFavouriteList(){
        if(favourites.size() == 0){
            System.out.println("No Favourite Programmes.");
        }
        else {
            System.out.println("|  INDEX  |               NAME                |");
            for (int i = 0; i < favourites.size(); i++) {
                Favourite favourite = favourites.get(i);
                int j = i + 1;
                String title = "";
                if (favourite.autoRecord){
                    title = "(R)" + favourite.programmeTitle;
                }
                else {
                    title = favourite.programmeTitle;
                }
                System.out.println("|" + j + DisplayHelper.missingSpaces(9, String.valueOf(j).length()) + "|" + DisplayHelper.truncate(title, 34) + DisplayHelper.missingSpaces(35, DisplayHelper.truncate(favourite.programmeTitle, 35).length()) + "|");
            }
        }
    }
}
