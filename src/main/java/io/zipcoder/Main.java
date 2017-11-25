package io.zipcoder;

import org.apache.commons.io.IOUtils;

import java.util.ArrayList;


public class Main {

    public String readRawDataToString() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));
        return result;
    }

    public static void main(String[] args) throws Exception{
        String output = (new Main()).readRawDataToString();
        System.out.println(output);
        // TODO: parse the data in output into items, and display to console.

        ArrayList<Item> listOfItems;
        ItemParser itemParser = new ItemParser();
        listOfItems = itemParser.parseRawIntoListOfItems(output);
        output = itemParser.generateCompleteOutput(listOfItems);
        System.out.println(output);
      


    }
}
