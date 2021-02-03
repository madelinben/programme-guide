package com.sabaton.Service;

import com.sabaton.*;
import com.sabaton.Model.*;
import com.sabaton.Repository.*;
import java.io.*;
import java.util.*;

public class ScheduleService {
    /**
     * Scanner variable for whole controller.
     **/
    public static Scanner scanner = new Scanner(System.in);

    /**
     * Method to call getSchedule.
     **/
    public static void getSchedule() {
        ScheduleRepository.getSchedule();
    }

    /**
     * Method to call saveSchedule.
     **/
    public static void saveSchedule() throws IOException {
        ScheduleRepository.saveSchedule();
    }

    /**
     * Method to add user selected programme to the global ArrayList.
     *
     * @param userInput ArrayList userInput that contains the programme selected to be recorded.
     **/
    public static void addProgramme(Programme userInput) {
        GlobalVariables.scheduleData.scheduledProgrammes.add(userInput);
    }

    /**
     * Method to remove Item at list at index choice.
     *
     * @param choice user selected item that represents the index position of said item.
     **/
    public static void removeProgramme(int choice) {
        GlobalVariables.scheduleData.scheduledProgrammes.remove(choice);
    }

    /**
     * Method to remove Item at list at index choice.
     **/
    public static void removeProgramme(Programme programme) {
        ArrayList<Integer> remove = new ArrayList<>();
        for (int i = 0; i < GlobalVariables.scheduleData.scheduledProgrammes.size(); i++) {
            Programme scheduledProgramme = GlobalVariables.scheduleData.scheduledProgrammes.get(i);
            if (scheduledProgramme.title == programme.title && programme.start == scheduledProgramme.start) {
                remove.add(i);
            }
        }
        for (int i : remove) {
            removeProgramme(i);
        }
    }

    /**
     * Method to mark a scheduled programme as recorded in the EPG grid and other systems from reading the schedule file
     **/
    public static void applyTag() {
        for (Programme scheduleProgramme : GlobalVariables.scheduleData.scheduledProgrammes) {
            for (Channel channel : GlobalVariables.xmlData) {
                for (Programme channelProgramme : channel.channelProgrammes) {
                    if (channelProgramme.title.equalsIgnoreCase(scheduleProgramme.title) && channelProgramme.start.getHourOfDay() == scheduleProgramme.start.getHourOfDay() && channelProgramme.start.getMinuteOfHour() == scheduleProgramme.start.getMinuteOfHour() && channelProgramme.start.getDayOfMonth() == scheduleProgramme.start.getDayOfMonth() && channelProgramme.start.getMonthOfYear() == scheduleProgramme.start.getMonthOfYear() && channelProgramme.start.getYear() == scheduleProgramme.start.getYear()) {
                        channelProgramme.isRecording = true;
                    }
                }
            }
        }
    }

    /**
     * Method to mark a scheduled programme as recorded in the EPG grid and other systems
     *
     * @param programme programme added to the schedule
     **/
    public static void applyTag(Programme programme) {
        for (Channel sourceChannel : GlobalVariables.xmlData) {
            for (Programme sourceProgramme : sourceChannel.channelProgrammes) {
                if (programme.title.equals(sourceProgramme.title) && programme.start.equals(sourceProgramme.start) && programme.end.equals(sourceProgramme.end) && programme.channelDate.equals(sourceProgramme.channelDate)) {
                    if (!sourceProgramme.title.contains("(R)")) {
                        sourceProgramme.isRecording = true;
                    }
                }
            }
        }
    }

    /**
     * Method to mark a scheduled programme as not recorded in the EPG grid and other systems
     *
     * @param programme programme removed from the schedule
     **/
    public static void removeTag(Programme programme) {
        for (Channel sourceChannel : GlobalVariables.xmlData) {
            for (Programme sourceProgramme : sourceChannel.channelProgrammes) {
                if (programme.title.equals(sourceProgramme.title) && programme.start.equals(sourceProgramme.start) && programme.end.equals(sourceProgramme.end) && programme.channelDate.equals(sourceProgramme.channelDate)) {
                    sourceProgramme.isRecording = false;
                }
            }
        }
    }



}