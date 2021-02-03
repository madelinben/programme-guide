package com.sabaton.Service;

import com.sabaton.*;
import com.sabaton.Model.*;
import com.sabaton.Repository.TunerRepository;
import org.joda.time.DateTime;

public class TunerService {

    /**
     * Method to call getTuner in TunerRepository.
     */
    public static void getTuner(){
        TunerRepository.getTuner();
    }
    /**
     * Method to call updateTuner in TunerRepository.
     * @param newTuner new number of tuners.
     */
    public static void updateTuner(int newTuner){
        TunerRepository.updateTuner(newTuner);
    }

    /**
     * Checks to see if number of scheduled programmes doesn't exceed tuner amount.
     */
    public static boolean validateTuner(Programme programme){
        int tunerCount = 0;
        for (Programme scheduledProgramme : GlobalVariables.scheduleData.scheduledProgrammes) {
            if((scheduledProgramme.start.isBefore(programme.start) && scheduledProgramme.end.isAfter(programme.end))||(scheduledProgramme.start.isBefore(programme.start) && scheduledProgramme.end.isBefore(programme.end))||((scheduledProgramme.start.isAfter(programme.start) && scheduledProgramme.start.isBefore(programme.end)) && scheduledProgramme.end.isAfter(programme.end))||(scheduledProgramme.start.isAfter(programme.start) && scheduledProgramme.end.isBefore(programme.end))||(scheduledProgramme.start.getHourOfDay() == programme.start.getHourOfDay()) || (scheduledProgramme.end.getHourOfDay() == programme.end.getHourOfDay())||(scheduledProgramme.start.getMinuteOfHour() == programme.start.getMinuteOfHour()) || (scheduledProgramme.end.getMinuteOfHour() == programme.end.getMinuteOfHour())){
                tunerCount++;
            }

            //(programme.start.is start1 <= end2 and start2 <= end1 and start1 <= end1 and start2 <= end2 )
        }
        return tunerCount < GlobalVariables.numTuners.tuner;
    }

}
