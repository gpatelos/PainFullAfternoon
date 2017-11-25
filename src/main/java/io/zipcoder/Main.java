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
        System.out.println(output+"\n\n");
        // TODO: parse the data in output into items, and display to console.

        ArrayList<Item> listOfItems;
        ItemParser itemParser = new ItemParser();
        listOfItems = itemParser.parseRawIntoListOfItems(output);

        String outputFormattedNicely;
        String outputAlmostFormattedNicelyInOnePass;

        outputFormattedNicely = itemParser.generateNameOutputManually(listOfItems, "Milk");
        outputFormattedNicely += itemParser.generateNameOutputManually(listOfItems, "Bread");
        outputFormattedNicely += itemParser.generateNameOutputManually(listOfItems, "Cookies");
        outputFormattedNicely += itemParser.generateNameOutputManually(listOfItems, "Apples");
        outputFormattedNicely += itemParser.printErrorLine(output);
        System.out.println(outputFormattedNicely);

        outputAlmostFormattedNicelyInOnePass = itemParser.generateCompleteOutput(listOfItems);
        System.out.println(outputAlmostFormattedNicelyInOnePass);



    }
}
