package com.sabaton.Repository;

import com.sabaton.GlobalVariables;
import com.sabaton.Service.*;
import org.apache.commons.io.*;
import org.joda.time.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ProgrammeLiveDataRepository extends Thread {
    private int days;
    /**
     * Constructor for the class to pass the days parameter into run method as it is inherited from the Thread class.
     * @param days difference between last data pull and now. In whole days.
     **/
    public ProgrammeLiveDataRepository(int days) {
        this.days = days;
    }
    /**
     * Method run code on separate thread.
     **/
    public void run(){
        try{
            int dayCounter = 0;
            if(days > 4){
                days = 4;
            }
            switch (days){
                case 1:
                    dayCounter = 2;
                    break;
                case 2:
                    dayCounter = 1;
                    break;
                case 3:
                    dayCounter = 0;
                case 4:
                    dayCounter = -1;
            }
            while(dayCounter <= 2){
                File bbc1 = new File("src/XML/" + dayCounter + "/bbc1.xml");
                FileUtils.copyURLToFile(new URL("http://bleb.org/tv/data/listings/" + dayCounter + "/bbc1.xml"),bbc1);
                TimeUnit.SECONDS.sleep(2);
                File itv1 = new File("src/XML/" + dayCounter + "/itv1.xml");
                FileUtils.copyURLToFile(new URL("http://bleb.org/tv/data/listings/" + dayCounter + "/itv1_hd.xml"),itv1);
                TimeUnit.SECONDS.sleep(2);
                File ch4 = new File("src/XML/" + dayCounter + "/ch4.xml");
                FileUtils.copyURLToFile(new URL("http://bleb.org/tv/data/listings/" + dayCounter + "/ch4.xml"),ch4);
                TimeUnit.SECONDS.sleep(2);
                File five = new File("src/XML/" + dayCounter + "/five.xml");
                FileUtils.copyURLToFile(new URL("http://bleb.org/tv/data/listings/" + dayCounter + "/five.xml"),five);
                TimeUnit.SECONDS.sleep(2);
                File bbc2 = new File("src/XML/" + dayCounter + "/bbc2.xml");
                FileUtils.copyURLToFile(new URL("http://bleb.org/tv/data/listings/" + dayCounter + "/bbc2.xml"),bbc2);
                TimeUnit.SECONDS.sleep(2);
                File cbbc = new File("src/XML/" + dayCounter + "/cbbc.xml");
                FileUtils.copyURLToFile(new URL("http://bleb.org/tv/data/listings/" + dayCounter + "/cbbc.xml"),cbbc);
                TimeUnit.SECONDS.sleep(2);
                File dave = new File("src/XML/" + dayCounter + "/dave.xml");
                FileUtils.copyURLToFile(new URL("http://bleb.org/tv/data/listings/" + dayCounter + "/dave.xml"),dave);
                TimeUnit.SECONDS.sleep(2);
                File e4 = new File("src/XML/" + dayCounter + "/e4.xml");
                FileUtils.copyURLToFile(new URL("http://bleb.org/tv/data/listings/" + dayCounter + "/e4.xml"),e4);
                TimeUnit.SECONDS.sleep(2);
                File film_four = new File("src/XML/" + dayCounter + "/film_four.xml");
                FileUtils.copyURLToFile(new URL("http://bleb.org/tv/data/listings/" + dayCounter + "/film_four.xml"),film_four);
                TimeUnit.SECONDS.sleep(2);
                File more4 = new File("src/XML/" + dayCounter + "/more4.xml");
                FileUtils.copyURLToFile(new URL("http://bleb.org/tv/data/listings/" + dayCounter + "/more4.xml"),more4);
                TimeUnit.SECONDS.sleep(2);
                File p_itv2 = new File("src/XML/" + dayCounter + "/p_itv2.xml");
                FileUtils.copyURLToFile(new URL("http://bleb.org/tv/data/listings/" + dayCounter + "/p_itv2.xml"),p_itv2);
                TimeUnit.SECONDS.sleep(2);
                File qvc = new File("src/XML/" + dayCounter + "/qvc.xml");
                FileUtils.copyURLToFile(new URL("http://bleb.org/tv/data/listings/" + dayCounter + "/qvc.xml"),qvc);
                dayCounter++;
            }
            FileWriter fileWriter = new FileWriter("src/sabaton/LiveEPG.Sabaton");
            fileWriter.write(DateTime.now().toString());
            fileWriter.close();
            ProgrammeService.importXml();
            FavouriteService.recordFavourites();
            GlobalVariables.dayDifference = 0;
        }
        catch(Exception exception){
            System.out.println("Error in getting files");
            exception.printStackTrace();
        }
    }
}
