package com.sabaton.Controller;

import com.sabaton.*;
import com.sabaton.Model.*;
import com.sabaton.Service.*;
import org.joda.time.DateTime;
import java.util.*;
import java.util.concurrent.*;

import static com.sabaton.GlobalVariables.*;

public class ChannelController {
    /**
     * Formats selected channel and programme data within the boundary of the users current location to fill the grid.
     **/
    public static void displayGrid() throws java.lang.InterruptedException{
        System.out.println("Date: " + DateTime.now().plusDays(displayDay).toLocalDate().getDayOfMonth() + "/" + DateTime.now().plusDays(displayDay).toLocalDate().getMonthOfYear() + "/" + DateTime.now().plusDays(displayDay).toLocalDate().getYear());
        int validTime = GlobalVariables.timePointer;
        String header = "|  Channel  |";
        for (int i=0; i<3; i++) {
            if (validTime == 24) {
                validTime = 0;
            }
            header += String.format("%02d:00                                          |", validTime);
            validTime+=1;
        }
        System.out.println(header);
        for (int j=0; j<=3; j++) {
            int validPointer = 0;
            if ((GlobalVariables.channelPointer + j) > 11) {
                GlobalVariables.channelPointer = 0;
                validPointer = 0;
            } else if (GlobalVariables.channelPointer < 0) {
                GlobalVariables.channelPointer = 8;
                validPointer = 8;
            } else {
                validPointer = GlobalVariables.channelPointer + j;
            }
            String channelName = ChannelService.getChannelName(validPointer);
            ArrayList<Programme> programmeArrayList = ProgrammeService.getGridData(channelName);
            String formattedRow = ChannelService.getChannelDisplay(channelName, programmeArrayList);
            System.out.println(formattedRow);
        }
        if(dayDifference >= 3){
            System.out.println("We are currently pulling down new data. Please wait 30 seconds and we will reload the grid for you.");
            TimeUnit.SECONDS.sleep(30);
            displayGrid();
        }

    }
}
