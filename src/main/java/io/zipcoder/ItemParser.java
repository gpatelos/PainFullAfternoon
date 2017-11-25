package io.zipcoder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemParser {

    public static Integer errorCount;


    public ArrayList<String> parseRawDataIntoStringArray(String rawData){
        String stringPattern = "##";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawData);
        return response;
    }

    public String parseNameFromRawItem(String rawItem) throws ItemParseException{
        ArrayList<String> keyValuePairs = findKeyValuePairsInRawItemData(rawItem);
        String namePair = keyValuePairs.get(0);
        String name;
        Pattern namePattern = Pattern.compile("(c\\w\\wkies)", Pattern.CASE_INSENSITIVE);
        Matcher nameMatch= namePattern.matcher(namePair);
        if (nameMatch.find()) {
            namePair = namePair.replaceAll(nameMatch.group(),"cookies");
        }


        try{
             name = namePair.split(":")[1];
        }catch (ArrayIndexOutOfBoundsException value){
            throw new ItemParseException("No name", rawItem);
        }

        return name;


/*
        Pattern namePattern = Pattern.compile("(?<=:)\\w+", Pattern.CASE_INSENSITIVE);
        Matcher nameMatch= namePattern.matcher(namePair);

        if (nameMatch.find()) {
            name = rawItem.replaceAll(rawItem,nameMatch.group());
        }else {
            throw new ItemParseException("Name missing", rawItem);
        }
*/

    }

    public Double parsePriceFromRawItem(String rawItem) throws ItemParseException{
        ArrayList<String> keyValuePairs = findKeyValuePairsInRawItemData(rawItem);
        String valuePair = keyValuePairs.get(1);
        Double price = 0.0;
        try{
        String value = valuePair.split(":")[1];
        price = Double.parseDouble(value);
        }catch (ArrayIndexOutOfBoundsException value){
            throw new ItemParseException("No Price", rawItem);
        }
        return price;
    }

    public String parseTypeFromRawItem(String rawItem) throws ItemParseException {
        ArrayList<String> keyValuePairs = findKeyValuePairsInRawItemData(rawItem);
        String typePair = keyValuePairs.get(2);
        String type ="";
        try{ type = typePair.split(":")[1];
        }catch (ArrayIndexOutOfBoundsException value){
            throw new ItemParseException("No Type", rawItem);
        }
        return type;
    }

    public String parseExpirationFromRawItem(String rawItem) throws ItemParseException {
        ArrayList<String> keyValuePairs = findKeyValuePairsInRawItemData(rawItem);
        String expirationPair = keyValuePairs.get(3);
        String expiration="";
        try{
        expiration = expirationPair.split(":")[1];
        }catch (ArrayIndexOutOfBoundsException value){
            throw new ItemParseException("No expiration", rawItem);
        }
        return expiration;
    }



    public Item parseStringIntoItem(String rawItem) throws ItemParseException{


        try {
            String name = parseNameFromRawItem(rawItem).toLowerCase();
            name = name.substring(0,1).toUpperCase()+name.substring(1);
            Double price = parsePriceFromRawItem(rawItem);
            String type = parseTypeFromRawItem(rawItem).toLowerCase();
            String expiration = parseExpirationFromRawItem(rawItem).toLowerCase();
            Item item = new Item(name,price,type,expiration);
            return item;
        }catch (ArrayIndexOutOfBoundsException e){
                throw new ItemParseException("Error", e.getMessage() );
        }

/*
        for(String keyValuePair: keyValuePairs){
            Pattern namePattern = Pattern.compile("(?<=name:)\\w+", Pattern.CASE_INSENSITIVE);
            Matcher nameMatch= namePattern.matcher(rawItem);
            Pattern pricePattern = Pattern.compile("(?<=price:)\\d*.\\d*", Pattern.CASE_INSENSITIVE);
            Matcher priceMatch= pricePattern.matcher(rawItem);
            Pattern typePattern = Pattern.compile("(?<=type:)\\w+", Pattern.CASE_INSENSITIVE);
            Matcher typeMatch= typePattern.matcher(rawItem);
            Pattern expirationPattern = Pattern.compile("(?<=expiration:)\\d+/\\d+/\\d+", Pattern.CASE_INSENSITIVE);
            Matcher expirationMatch= expirationPattern.matcher(rawItem);

            String name;
            Double price;
            String type;
            String expiration;

            if (nameMatch.find()) {
                name = rawItem.replaceAll(rawItem,nameMatch.group());
            }else {
                throw new ItemParseException("Name missing", rawItem);
            }

            if (priceMatch.find()) {
                String priceString = rawItem.replaceAll(rawItem,priceMatch.group());
                price = Double.parseDouble(priceString);
            }else {
                throw new ItemParseException("Price missing", rawItem);
            }

            if (typeMatch.find()) {
                type = rawItem.replaceAll(rawItem,typeMatch.group());
            }else {
                throw new ItemParseException("Type missing", rawItem);
            }

            if (expirationMatch.find()) {
                expiration = rawItem.replaceAll(rawItem,expirationMatch.group());
            }else {
                throw new ItemParseException("Expiration missing", rawItem);
            }


            Item newItem = new Item(name.toLowerCase(), price,type.toLowerCase(),expiration);
            return newItem;
        }*/
    }
    public ArrayList<Item> parseRawIntoListOfItems(String rawData){
        ItemParser itemParser = new ItemParser();
        ArrayList<String> items = itemParser.parseRawDataIntoStringArray(rawData);
        ArrayList<Item> listOfItems = new ArrayList<>();

        for (String eachStringItem : items) {
            try {
                Item item = itemParser.parseStringIntoItem(eachStringItem);
                listOfItems.add(item);

            } catch (ItemParseException e) {
            }
        }

        return listOfItems;
    }


    public ArrayList<String> findKeyValuePairsInRawItemData(String rawItem){
        String stringPattern = "[;|^|!|%|*|@]";
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


    public String generateNameCountOutput(ArrayList<Item> items) {
       /* Stream<Item> itemStream = items.stream()
                                       .collect(Collectors.groupingByConcurrent());
        List<Item> itemList = itemStream.collect(Collectors.toList());

        Map<String, List<Item>> nameMap = items.stream().collect(Collectors.groupingBy(Item::getName));
        Map<Double, List<Item>> priceMap = items.stream().collect(Collectors.groupingBy(Item::getPrice));*/
        String output ="";
        Map<String, Long> counts = items.stream().collect(Collectors.groupingBy(Item::getName, Collectors.counting()));
        for (Map.Entry entry : counts.entrySet()) {
            output += String.format("name: %7s%12sseen:%2s times\n",entry.getKey()," ",entry.getValue())+"=============            =============\n";
        }

        return output;
    }

    public Integer captureCountedErrors(String rawAll) {
        ArrayList<Item> listOfItems;
        ItemParser itemParser = new ItemParser();
        listOfItems = itemParser.parseRawIntoListOfItems(rawAll);


        ArrayList<String> items = itemParser.parseRawDataIntoStringArray(rawAll);
        Integer linesNotProcessed = items.size() - listOfItems.size();

        return linesNotProcessed;
    }

    public String printErrorLine(String rawAll) {
        return "Errors                   seen: " + captureCountedErrors(rawAll) +" times";
    }

    public String generateCompleteOutput(ArrayList<Item> items) {

        String output = "\n\nFailed first attempt.\n\n";
        Map<String, Map<Double, Long>> counts = items.stream().collect(Collectors.groupingBy(Item::getName, Collectors
                                                              .groupingBy(Item::getPrice, Collectors.counting())));

        for (Map.Entry<String, Map<Double, Long>> entry : counts.entrySet()) {

                output += String.format("name: %7s%12sseen:%2s times\n",entry.getKey()," ",entry.getValue())+"=============            =============\n";

        }

        return output;

    }


    public List<Item> filterItemsByName(ArrayList<Item> items, String itemToFilter) {
        List<Item> filteredItemList= items.stream().filter(item -> item.getName().contains(itemToFilter)).collect(Collectors.toList());


        return filteredItemList;
    }

    public String generateNameOutputManually(ArrayList<Item> arrayListOfItems, String itemToFilter) {
        List<Item> listOfItems;
        listOfItems = filterItemsByName(arrayListOfItems,itemToFilter);

        String output ="";
        Map<String, Long> summaryCount = listOfItems.stream().collect(Collectors.groupingBy(Item::getName, Collectors.counting()));
        for (Map.Entry entry : summaryCount.entrySet()) {
            output += String.format("Name: %7s%12sseen:%2s times\n",entry.getKey()," ",entry.getValue())+"=============            =============\n";
        }
        Map<Double, Long> counts = listOfItems.stream().collect(Collectors.groupingBy(Item::getPrice, Collectors.counting()));

        for (Map.Entry entry : counts.entrySet()) {
            output += String.format("Price: %6s%12sseen:%2s times\n",entry.getKey()," ",entry.getValue())+"-------------            -------------\n";
        }

        return output+"\n";
    }
}
