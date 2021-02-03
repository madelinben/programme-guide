package com.sabaton.Service;

import com.sabaton.*;
import com.sabaton.Model.*;
import com.sabaton.Repository.*;
import java.io.*;
import java.util.*;

public class FavouriteService {
    /**
     * Adds favourites to record if need to after Live data has pulled down.
     */
    public static void recordFavourites() {
        for (Channel channel : GlobalVariables.xmlData.subList(36,47)){
            for(Programme programme : channel.channelProgrammes){
                for(Favourite favourite : GlobalVariables.favourites){
                    if(programme.title.equalsIgnoreCase(favourite.programmeTitle) && (favourite.autoRecord || GlobalVariables.autoRecord)){
                        addTag(programme);
                        ScheduleService.addProgramme(programme);
                        ScheduleService.applyTag(programme);
                    }
                }
            }
        }
    }

    /**
     * Adds programme title to favourites if it isn't there already.
     * @param title programme title
     * @param autoRecord sees if favourite is set to autorecord programmes.
     * @return boolean whether programme was added or not.
     */
    public static boolean addFavourite(String title, boolean autoRecord) {
        for (Favourite favourite : GlobalVariables.favourites){
            if (favourite.programmeTitle == title){
                return false;
            }
        }
        Favourite newFavourite = new Favourite();
        newFavourite.programmeTitle = title;
        newFavourite.autoRecord = autoRecord;
        GlobalVariables.favourites.add(newFavourite);
        if(autoRecord){
            addFavouriteToSchedule(newFavourite);
        }
        return true;
    }
    /**
     * Adds programme title to favourites if it isn't there already.
     * @param favourite favourite object for programmes to be added to schedule.
     */
    public static void addFavouriteToSchedule(Favourite favourite){
        ArrayList<Programme> favouriteProgrammes = ProgrammeService.searchProgrammes(favourite.programmeTitle);
        for(Programme programme : favouriteProgrammes){
            programme.isFavourite = true;
            programme.isRecording = true;
        }
    }

    /**
     * Removes favourite matching given title from favorites.
     * @param title programme title
     * @param autoRecord sees if favourite is set to autorecord programmes.
     * @return boolean whether programme was removed or not.
     */
    public static boolean removeFavourite(String title, boolean autoRecord) {
        for (Favourite favourite : GlobalVariables.favourites){
            if (favourite.programmeTitle == title){
                if(autoRecord){
                    removeFavouriteFromSchedule(favourite);
                }
                GlobalVariables.favourites.remove(favourite);
                return true;
            }
        }
        return false;
    }

    /**
     * removes programme title to favourites if it isn't there already.
     * @param favourite favourite object for programmes to be removed to schedule.
     */
    public static void removeFavouriteFromSchedule(Favourite favourite){
        for(Channel channel : GlobalVariables.xmlData){
            for(Programme programme : channel.channelProgrammes){
                if(favourite.programmeTitle.equals(programme.title)){
                    programme.isFavourite = false;
                    ArrayList<Programme> programmes = ProgrammeService.searchProgrammes(programme.title);
                    for(Programme favouriteProgrammes : programmes){
                        ScheduleService.removeProgramme(favouriteProgrammes);
                        ScheduleService.removeTag(favouriteProgrammes);
                        removeTag(favouriteProgrammes);
                    }
                }
            }
        }

    }

    /**
     * Method to mark a favourite programme in the EPG grid and other systems from reading the schedule file.
     **/
    public static void addTag() {
        for(Favourite favourite : GlobalVariables.favourites){
            for(Channel channel : GlobalVariables.xmlData){
                for(Programme channelProgramme : channel.channelProgrammes){
                    if(channelProgramme.title.equalsIgnoreCase(favourite.programmeTitle)){
                        channelProgramme.isFavourite = true;
                        if(GlobalVariables.autoRecord || favourite.autoRecord){
                            channelProgramme.isRecording = true;
                        }
                    }
                }
            }
        }
    }
    /**
     * Method to mark a Favourite programme as recorded in the EPG grid and other systems.
     * @param programme programme added to the favourites.
     **/
    public static void addTag(Programme programme){
        for (Channel sourceChannel : GlobalVariables.xmlData) {
            for (Programme sourceProgramme : sourceChannel.channelProgrammes) {
                if ((programme.title == sourceProgramme.title) && (programme.start == sourceProgramme.start) && (programme.end == sourceProgramme.end) && (programme.channelDate == sourceProgramme.channelDate)) {
                    sourceProgramme.isFavourite = true;
                }
            }
        }
    }

    /**
     * Method to mark a favourite programme as not recorded in the EPG grid and other systems.
     * @param programme programme removed from the favourite.
     **/
    public static void removeTag(Programme programme) {
        for (Channel sourceChannel : GlobalVariables.xmlData) {
            for (Programme sourceProgramme : sourceChannel.channelProgrammes) {
                if ((programme.title == sourceProgramme.title) && (programme.start == sourceProgramme.start) && (programme.end == sourceProgramme.end) && (programme.channelDate == sourceProgramme.channelDate)) {
                    sourceProgramme.isFavourite = false;
                }
            }
        }
    }
    /**
     * Call for getFavourites in Favourite Repository.
     **/
    public static void getFavourites(){
        FavouriteRepository.getFavourites();
    }
    /**
     * Call for getAutoRecord in Favourite Repository.
     **/
    public static void getAutoRecord() throws IOException {
        FavouriteRepository.getAutoRecord();
    }
    /**
     * Call for saveFavourites in Favourite Repository.
     **/
    public static void saveFavourites() throws IOException {
        FavouriteRepository.saveFavourites();
    }
    /**
     * Call for updateAutoRecord in Favourite Repository.
     **/
    public static void updateAutoRecord() throws IOException{
        FavouriteRepository.updateAutoRecord();
    }
}
