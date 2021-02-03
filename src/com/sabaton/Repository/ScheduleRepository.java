package com.sabaton.Repository;

import com.sabaton.*;
import com.sabaton.Helper.*;
import com.sabaton.Model.*;
import java.io.*;
import java.util.*;
import org.joda.time.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class ScheduleRepository {
    /**
     * Method to grab the schedule data from the schedule folder. If the schedule is not found one is created.
     **/
    public static void getSchedule() {
        GlobalVariables.scheduleData = new Schedule();
        GlobalVariables.scheduleData.scheduledProgrammes = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONArray scheduleData = (JSONArray) parser.parse(new FileReader(System.getProperty("user.dir") + "/src/JSON/schedule.json"));
            for (int i = 0; i < scheduleData.size(); i++) {
                Programme programme = new Programme();
                JSONObject object = (JSONObject) scheduleData.get(i);
                programme.title = (String) object.get("title");
                programme.channel = (String) object.get("channel");
                programme.start = DateTimeHelper.getDateTimeOffset(DateTime.parse((String) object.get("start")));
                programme.end = DateTimeHelper.getDateTimeOffset(DateTime.parse((String) object.get("end")));
                programme.channelDate = DateTimeHelper.getDateTimeOffset(DateTime.parse((String) object.get("channelDate")));
                programme.isRecording = Boolean.valueOf((String) object.get("isRecording"));
                programme.isFavourite = Boolean.valueOf((String) object.get("isFavourite"));
                //ScheduleService.applyTag(programme);
                if (programme.start.isAfterNow() || (programme.start.isBeforeNow() && programme.end.isAfterNow())){
                    GlobalVariables.scheduleData.scheduledProgrammes.add(programme);
                }
            }
        } catch (Exception exception) {
            System.out.println("Error! Getting Schedule.");
            exception.printStackTrace();
        }
    }
    /**
     * Method to save schedule data upon exit. writes the GlobalVariable scheduleData to file for permanent storage.
     **/
    public static void saveSchedule() throws IOException {
        FileWriter scheduleFile = new FileWriter(new File(System.getProperty("user.dir") + "/src/JSON/schedule.json"));
        JSONArray scheduleData = new JSONArray();
        for (Programme programme : GlobalVariables.scheduleData.scheduledProgrammes) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("start", programme.start.toString());
            jsonObject.put("channel", programme.channel);
            jsonObject.put("end", programme.end.toString());
            jsonObject.put("title", programme.title);
            jsonObject.put("channelDate", programme.channelDate.toString());
            jsonObject.put("isRecording", String.valueOf(programme.isRecording));
            jsonObject.put("isFavourite", String.valueOf(programme.isRecording));
            scheduleData.add(jsonObject);
        }
        scheduleFile.write(scheduleData.toJSONString());
        scheduleFile.flush();
        scheduleFile.close();
    }
}
