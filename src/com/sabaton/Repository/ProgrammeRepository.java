package com.sabaton.Repository;

import com.sabaton.*;
import com.sabaton.Model.*;
import com.sabaton.Helper.*;
import java.nio.file.*;
import java.util.*;
import java.io.*;
import javax.xml.parsers.*;
import com.sabaton.Service.*;
import org.joda.time.*;
import org.joda.time.format.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class ProgrammeRepository {
    /**
     * Method creates list of .xml formatted files in respository src folder, and calls for the file to be read and validated.
     **/
    public static void importXml(){
        ArrayList<Channel> channelArrayList = new ArrayList<>();
        try {
            String src = System.getProperty("user.dir") + "/src/XML/";
            File dir = new File(src);
            File[] fileList = dir.listFiles();
            GlobalVariables.displayDay = -1;
            for(int i = 0; i < fileList.length; i++){
                File[] directoryFileList = fileList[i].listFiles();
                for(int j = 0; j < directoryFileList.length; j++){
                    String filename = directoryFileList[j].getName();
                    if (filename.toLowerCase().endsWith(".xml")) {
                        String path = src + GlobalVariables.displayDay +"/" + filename;
                        String xmlString = ProgrammeService.escapeSpecialChar(path);
                        channelArrayList.addAll(readerXml(xmlString));
                    }
                }
                GlobalVariables.displayDay++;
            }
            GlobalVariables.displayDay = 0;
            GlobalVariables.xmlData = channelArrayList;
            ScheduleService.applyTag();
            FavouriteService.addTag();

        } catch (Exception exception) {
            System.out.println("Error! Identifying file path!");
            exception.printStackTrace();
        }
    }

    /**
     * Method creates a formatted Document and produces a tree hierarchy of programme elements to increment through and store in a normalised object.
     * @param    xmlString    Document is formatted from the validated xml input source.
     * @return returns the list of all the channels in the solution.
     * @see      ArrayList<Channel>
     **/
    public static ArrayList<Channel> readerXml(String xmlString) {
        ArrayList<Channel> channelArrayList = new ArrayList<Channel>();
        boolean endOfDay = false;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            doc.getDocumentElement().normalize();
            NodeList channelList = doc.getElementsByTagName("channel");
            for (int i = 0; i < channelList.getLength(); i++) {
                Node channelNode = channelList.item(i);
                if (channelNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element channelElement = (Element) channelNode;
                    Channel channel = new Channel();
                    NodeList programmeList = channelNode.getChildNodes();
                    DateTimeFormatter dateTimeFormatterDate = DateTimeFormat.forPattern("dd/MM/yyyy");
                    DateTime channelDate = DateTimeHelper.getDateTimeOffset(dateTimeFormatterDate.parseDateTime(channelElement.getAttribute("date")));
                    ArrayList<Programme> channelProgrammes = new ArrayList<Programme>();
                    for (int j = 0; j < programmeList.getLength(); j++) {
                        Node programmeNode = programmeList.item(j);
                        if (programmeNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element programmeElement = (Element) programmeNode;
                            Programme programme = new Programme();
                            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyyHHmm");
                            programme.start = DateTimeHelper.getDateTimeOffset(dateTimeFormatter.parseDateTime(channelElement.getAttribute("date") + programmeElement.getElementsByTagName("start").item(0).getTextContent()));
                            programme.end = DateTimeHelper.getDateTimeOffset(dateTimeFormatter.parseDateTime(channelElement.getAttribute("date") + programmeElement.getElementsByTagName("end").item(0).getTextContent()));
                            programme.channel = channelElement.getAttribute("id");
                            programme.title = programmeElement.getElementsByTagName("title").item(0).getTextContent();
                            if (programme.title.contains("&amp;")) {
                                programme.title = programme.title.replaceAll("&amp;", "&");
                            }
                            programme.description = programmeElement.getElementsByTagName("desc").item(0).getTextContent();
                            if (programme.description.contains("&amp;")) {
                                programme.description = programme.description.replaceAll("&amp;", "&");
                            }
                            programme.channelDate = channelDate;
                            if (programme.start.getHourOfDay() > 12) {
                                endOfDay = true;
                            }
                            if((endOfDay && programme.start.getHourOfDay() < 12) && (endOfDay && programme.end.getHourOfDay() < 12)) {
                                programme.start = programme.start.plusDays(1);
                                programme.end = programme.end.plusDays(1);
                                programme.channelDate = programme.channelDate.plusDays(1);
                            } else if(endOfDay && programme.start.getHourOfDay() < 12) {
                                programme.start = programme.start.plusDays(1);
                            } else if (endOfDay && programme.end.getHourOfDay() < 12) {
                                programme.end = programme.end.plusDays(1);
                            }
                            channelProgrammes.add(programme);
                        }
                    }
                    channel.channelProgrammes = channelProgrammes;
                    channel.name = channelElement.getAttribute("id");
                    channel.channelDate = channelDate;
                    channelArrayList.add(channel);
                }
            }
        } catch (Exception e) {
            System.out.println("Error! Converting XML String to Document!");
            e.printStackTrace();
        }
        return channelArrayList;
    }

    /**
     * Method checks if live data has been pulled into solution today. if not it will pull into solution
     **/
    public static void getLiveData() throws  org.json.simple.parser.ParseException, javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.text.ParseException, java.net.MalformedURLException, java.io.IOException,java.lang.InterruptedException{
        DateTime dateTime = getLastDataCollect();
        if(dateTime.isBefore(DateTime.now().minusDays(1)) || (dateTime.getDayOfMonth() < DateTime.now().getDayOfMonth() && dateTime.getMonthOfYear() == DateTime.now().getMonthOfYear())){
            GlobalVariables.dayDifference = Days.daysBetween(dateTime.toLocalDate(), DateTime.now().toLocalDate()).getDays();
            getLiveFiles(GlobalVariables.dayDifference);
        }
        else {
            importXml();
        }
    }

    /**
     * Gets the last time the data was pulled down.
     **/
    public static DateTime getLastDataCollect() throws  java.text.ParseException, java.io.IOException{
        String timestamp = new String(Files.readAllBytes(Paths.get("src/sabaton/LiveEPG.sabaton")));
        return DateTime.parse(timestamp);
    }
    /**
     * Deletes the files in -1 and then moves the ones in 0,1,2 down to -1,0,1 and then calls Threading to get the data for 2.
     **/
    public static void getLiveFiles(int daysDifference){
        File xml = new File("src/XML/");
        File[] xmlList = xml.listFiles();
        if(xmlList.length == 4){
            for(int i=1;i<=daysDifference;i++){
                File dir = new File("src/XML/-1");
                File[] fileList = dir.listFiles();
                for(int j = 0; j < fileList.length ; j ++){
                    fileList[j].delete();
                }
                for(int j = 0; j < 3 ; j++){
                    File dir1 = new File("src/XML/" + j);
                    File[] fileList1 = dir1.listFiles();
                    for(int k = 0; k < fileList1.length; k++){
                        String filename = fileList1[k].getName();
                        int newFileNumber = j - 1;
                        fileList1[k].renameTo(new File("src/XML/" + newFileNumber + "/" + filename));
                    }
                }
            }
        }
        new ProgrammeLiveDataRepository(daysDifference).start();
        importXml();
    }

}