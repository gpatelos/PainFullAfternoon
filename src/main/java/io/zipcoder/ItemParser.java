package io.zipcoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {


    public ArrayList<String> parseRawDataIntoStringArray(String rawData){
        String stringPattern = "##";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawData);
        return response;
    }

    public Item parseStringIntoItem(String rawItem) throws ItemParseException{

        Pattern namePattern = Pattern.compile("(?<=name:)\\w+", Pattern.CASE_INSENSITIVE);
        Matcher nameMatch= namePattern.matcher(rawItem);
        Pattern pricePattern = Pattern.compile("(?<=price:)\\d*.\\d*", Pattern.CASE_INSENSITIVE);
        Matcher priceMatch= pricePattern.matcher(rawItem);
        Pattern typePattern = Pattern.compile("(?<=type:)\\w+", Pattern.CASE_INSENSITIVE);
        Matcher typeMatch= typePattern.matcher(rawItem);
        Pattern expirationPattern = Pattern.compile("(?<=expiration:)\\d+/\\d+/\\d+", Pattern.CASE_INSENSITIVE);
        Matcher expirationMatch= expirationPattern.matcher(rawItem);

        String name ="";
        Double price = 0.0;
        String type ="";
        String expiration="";

        while (nameMatch.find()) {
            name = rawItem.replaceAll(rawItem,nameMatch.group());
        }

        while (priceMatch.find()) {
            String priceString = rawItem.replaceAll(rawItem,priceMatch.group());
            price = Double.parseDouble(priceString);
        }

        while (typeMatch.find()) {
            type = rawItem.replaceAll(rawItem,typeMatch.group());
        }

        while (expirationMatch.find()) {
            expiration = rawItem.replaceAll(rawItem,expirationMatch.group());
        }


        Item newItem = new Item(name.toLowerCase(), price,type.toLowerCase(),expiration);
        return newItem;
    }



    public ArrayList<String> findKeyValuePairsInRawItemData(String rawItem){
        String stringPattern = "[;|^]";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawItem);
        return response;
    }

    public ArrayList<String> splitKeyValuePairsIntoKeysAndValues(String singleKeyValuePair){
        String stringPattern = ":";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern, singleKeyValuePair);
        return response;
    }

    private ArrayList<String> splitStringWithRegexPattern(String stringPattern, String inputString){
        return new ArrayList<String>(Arrays.asList(inputString.split(stringPattern)));
    }



}
