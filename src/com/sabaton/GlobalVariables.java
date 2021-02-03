package com.sabaton;

import com.sabaton.Model.*;
import java.util.*;

public class GlobalVariables {
    /**
     * Variable to store the hour in which the grid starts.
     **/
    public static int timePointer;
    /**
     * Variable to store the channel index for the grid. Uses value from channels.
     **/
    public static int channelPointer;
    /**
     * Stores all of the EPG data.
     **/
    public static ArrayList<Channel> xmlData;
    /**
     * Stores all of the Schedule data.
     **/
    public static Schedule scheduleData;
    /**
     * Gives each channel an index.
     **/
    public static Dictionary<Integer, String> channels;
    /**
     * Stores the index value of the folders e.g. -1,0,1,2 in reference to day being displayed.
     **/
    public static int displayDay;
    /**
     * Stores the number of days that data is needed to be pulled.
     **/
    public static int dayDifference;
    /**
     * Stores the number of tuners, defaults to 3.
     **/
    public static Tuner numTuners;
    /**
     * Stores list of favourite programmes' titles.
     */
    public static ArrayList<Favourite> favourites;
    /**
     * Stores value for system wide auto record for favourites.
     */
    public static boolean autoRecord;
}
