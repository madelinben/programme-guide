package com.sabaton.Repository;

import com.sabaton.GlobalVariables;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class TunerRepository {
    /**
     * Initializes the global tuner value on program startup
     */
    public static void getTuner(){
        try {
            String test = new String(Files.readAllBytes(Paths.get("src/sabaton/Tuners.sabaton")));
            GlobalVariables.numTuners.tuner = Integer.parseInt(test);
        } catch (Exception exception) {
            System.out.println("Error! Identifying file path!");
        }
    }

    /**
     * Updates Tuners.sabaton file and numTuners global variable
     */
    public static void updateTuner(int newTuner){
        String stringTuner = String.valueOf(newTuner);
        try{
            FileWriter fileTuners = new FileWriter(new File(System.getProperty("user.dir") + "/src/sabaton/Tuners.sabaton"),false);
            fileTuners.write(stringTuner);
            GlobalVariables.numTuners.tuner = newTuner;
            fileTuners.close();
        } catch (Exception exception) {
            System.out.println("Error! Identifying file path!");
        }
    }

}
