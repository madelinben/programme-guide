package com.sabaton.Repository;

import com.sabaton.*;
import com.sabaton.Model.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.*;
import java.util.*;

public class FavouriteRepository {
    /**
     * Method to get favourite programmes from the favourites.json file, or create the file should it not exist.
     */
    public static void getFavourites() {
        GlobalVariables.favourites = new ArrayList<Favourite>();
        JSONParser parser = new JSONParser();
        try {
            JSONArray favouriteData = (JSONArray) parser.parse(new FileReader(System.getProperty("user.dir") + "/src/JSON/favourites.json"));
            for (Object object : favouriteData){
                JSONObject favouriteDatum = (JSONObject) object;
                Favourite newFavourite = new Favourite();
                newFavourite.programmeTitle = favouriteDatum.get("title").toString();
                newFavourite.autoRecord = Boolean.parseBoolean(favouriteDatum.get("autoRecord").toString());
                GlobalVariables.favourites.add(newFavourite);
            }
        } catch (Exception exception) {
            System.out.println("Error! Getting Favourites.");
            exception.printStackTrace();
        }
    }
    /**
     * Method to save favourites data upon exit. Writes the favourite programme titles to json file.
     **/
    public static void saveFavourites() throws IOException {
        FileWriter favouritesFile = new FileWriter(new File(System.getProperty("user.dir") + "/src/JSON/favourites.json"));
        JSONArray favouritesData = new JSONArray();
        for (Favourite favouriteProgramme : GlobalVariables.favourites) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", favouriteProgramme.programmeTitle);
            jsonObject.put("autoRecord", favouriteProgramme.autoRecord);
            favouritesData.add(jsonObject);
        }
        favouritesFile.write(favouritesData.toJSONString());
        favouritesFile.flush();
        favouritesFile.close();
    }

    /**
     * Method to get global auto record value.
     **/
    public static void getAutoRecord() throws IOException {
        Scanner autoRecordFile = new Scanner(new File(System.getProperty("user.dir") + "/src/sabaton/AutoRecord.sabaton"));
        GlobalVariables.autoRecord = Boolean.parseBoolean(autoRecordFile.nextLine());
        autoRecordFile.close();
    }

    /**
     * Method to update auto record value.
     **/
    public static void updateAutoRecord() throws IOException {
        PrintWriter printWriter = new PrintWriter(System.getProperty("user.dir") + "/src/sabaton/AutoRecord.sabaton");
        printWriter.print(GlobalVariables.autoRecord);
        printWriter.close();
    }
}
