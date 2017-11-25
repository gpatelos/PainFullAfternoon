package io.zipcoder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class ItemParserTest {

    private String rawSingleItem =    "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016";

    private String rawSingleItemIrregularSeparatorSample = "naMe:MiLK;price:3.23;type:Food^expiration:1/11/2016";

    private String rawBrokenSingleItem =  "naMe:;price:3.23;type:Food;expiration:1/25/2016";

    private String rawMultipleItems = "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##"
                                      +"naME:BreaD;price:1.23;type:Food;expiration:1/02/2016##"
                                      +"NAMe:BrEAD;price:1.23;type:Food;expiration:2/25/2016##";
    private ItemParser itemParser;

    private String batchOne = "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##" +
                            "naME:BreaD;price:1.23;type:Food;expiration:1/02/2016##" +
                            "NAMe:BrEAD;price:1.23;type:Food;expiration:2/25/2016##" +
                            "naMe:MiLK;price:3.23;type:Food^expiration:1/11/2016##" +
                            "naMe:Cookies;price:2.25;type:Food%expiration:1/25/2016##" +
                            "naMe:CoOkieS;price:2.25;type:Food*expiration:1/25/2016##" +
                            "naMe:COokIes;price:2.25;type:Food;expiration:3/22/2016##" +
                           "naMe:COOkieS;price:2.25;type:Food;expiration:1/25/2016##" +
                            "NAME:MilK;price:3.23;type:Food;expiration:1/17/2016##";

    private String rawAll = "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##naME:BreaD;price:1.23;type:Food;expiration:1/02/2016##NAMe:BrEAD;price:1.23;type:Food;expiration:2/25/2016##naMe:MiLK;price:3.23;type:Food^expiration:1/11/2016##naMe:Cookies;price:2.25;type:Food%expiration:1/25/2016##naMe:CoOkieS;price:2.25;type:Food*expiration:1/25/2016##naMe:COokIes;price:2.25;type:Food;expiration:3/22/2016##naMe:COOkieS;price:2.25;type:Food;expiration:1/25/2016##NAME:MilK;price:3.23;type:Food;expiration:1/17/2016##naMe:MilK;price:1.23;type:Food!expiration:4/25/2016##naMe:apPles;price:0.25;type:Food;expiration:1/23/2016##naMe:apPles;price:0.23;type:Food;expiration:5/02/2016##NAMe:BrEAD;price:1.23;type:Food;expiration:1/25/2016##naMe:;price:3.23;type:Food;expiration:1/04/2016##naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##naME:BreaD;price:1.23;type:Food@expiration:1/02/2016##NAMe:BrEAD;price:1.23;type:Food@expiration:2/25/2016##naMe:MiLK;priCe:;type:Food;expiration:1/11/2016##naMe:Cookies;price:2.25;type:Food;expiration:1/25/2016##naMe:Co0kieS;pRice:2.25;type:Food;expiration:1/25/2016##naMe:COokIes;price:2.25;type:Food;expiration:3/22/2016##naMe:COOkieS;Price:2.25;type:Food;expiration:1/25/2016##NAME:MilK;price:3.23;type:Food;expiration:1/17/2016##naMe:MilK;priCe:;type:Food;expiration:4/25/2016##naMe:apPles;prIce:0.25;type:Food;expiration:1/23/2016##naMe:apPles;pRice:0.23;type:Food;expiration:5/02/2016##NAMe:BrEAD;price:1.23;type:Food;expiration:1/25/2016##naMe:;price:3.23;type:Food^expiration:1/04/2016##";

    @Before
    public void setUp(){
        itemParser = new ItemParser();
    }

    @Test
    public void parseRawDataIntoStringArrayTest(){
        Integer expectedArraySize = 9;
        ArrayList<String> items = itemParser.parseRawDataIntoStringArray(batchOne);
        Integer actualArraySize = items.size();
        assertEquals(expectedArraySize, actualArraySize);
    }

    @Test
    public void parseStringIntoItemTest() throws ItemParseException{
        Item expected = new Item("milk", 3.23, "food","1/25/2016");
        Item actual = itemParser.parseStringIntoItem(rawSingleItem);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = ItemParseException.class)
    public void parseBrokenStringIntoItemTest() throws ItemParseException{
        itemParser.parseStringIntoItem(rawBrokenSingleItem);
    }

    @Test
    public void findKeyValuePairsInRawItemDataTest(){
        Integer expected = 4;
        Integer actual = itemParser.findKeyValuePairsInRawItemData(rawSingleItem).size();
        assertEquals(expected, actual);
    }

    @Test
    public void parseNameFromRawItemsTest() throws ItemParseException{
        String expected = "---output---\nMilk\nBreaD\nBrEAD\nMiLK\nCookies\nCoOkieS\nCOokIes\nCOOkieS\nMilK";
        String actual = "---output---";
        ArrayList<String> items = itemParser.parseRawDataIntoStringArray(batchOne);

        for(String eachStringItem: items){

            actual += "\n"+ itemParser.parseNameFromRawItem(eachStringItem);
        }

        Assert.assertEquals(expected,actual);


    }

    @Test
    public void parseDoubleFromRawItemsTest() throws ItemParseException{
        String expected = "bunch of prices";
        String actual = "";
        ArrayList<String> items = itemParser.parseRawDataIntoStringArray(batchOne);

        for(String eachStringItem: items){

            actual += itemParser.parsePriceFromRawItem(eachStringItem)+"\n";
        }

        Assert.assertEquals(expected,actual);


    }

    @Test
    public void parseTypeFromRawItemsTest() throws ItemParseException{
        String expected = "---output---";
        for(int i=0; i<28; i++){
            expected += "\nFood";
        }
        String actual = "---output---";
        ArrayList<String> items = itemParser.parseRawDataIntoStringArray(rawAll);

        for(String eachStringItem: items){

            actual += "\n"+itemParser.parseTypeFromRawItem(eachStringItem);
        }

        Assert.assertEquals(expected,actual);


    }

    @Test
    public void parseExpirationFromRawItemsTest() throws ItemParseException{
        String expected = "bunch of expirations";
        String actual = "";
        ArrayList<String> items = itemParser.parseRawDataIntoStringArray(rawAll);

        for(String eachStringItem: items){

            actual += itemParser.parseExpirationFromRawItem(eachStringItem)+"\n";
        }

        Assert.assertEquals(expected,actual);


    }


    @Test
    public void splitKeyValuePairsIntoKeyAndValue() throws Exception {
        ArrayList<String> arrayOfKeyValuePairs;
        arrayOfKeyValuePairs = itemParser.findKeyValuePairsInRawItemData(rawSingleItemIrregularSeparatorSample);

        String expected = "[price, 3.23]";
        String actual = itemParser.splitKeyValuePairsIntoKeysAndValues(arrayOfKeyValuePairs.get(1)).toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void findKeyValuePairsInRawItemDataTestIrregular(){
        Integer expected = 4;
        Integer actual = itemParser.findKeyValuePairsInRawItemData(rawSingleItemIrregularSeparatorSample).size();
        assertEquals(expected, actual);
    }

    @Test
    public void checkProcessAllToListOfItems() throws ItemParseException {

        ArrayList<Item> listOfItems;
        listOfItems = itemParser.parseRawIntoListOfItems(rawAll);

        Integer expected = 24;
        Integer actual = listOfItems.size();
        Assert.assertEquals(expected,actual);
    }
    @Test
    public void checkToSeeIfCanProcessAll() throws ItemParseException {

        ArrayList<String> items = itemParser.parseRawDataIntoStringArray(rawAll);

        String expected = "a list of Items";
        String actual = "";

        for (String eachStringItem : items) {
            try {
                Item item = itemParser.parseStringIntoItem(eachStringItem);
                actual += item.toString() + "\n";
            } catch (ItemParseException e) {
            }
        }Assert.assertEquals(expected, actual);
    }


    @Test
    public void countMilkEntries(){
        ArrayList<String> items = itemParser.parseRawDataIntoStringArray(rawAll);
        Integer expected = 6;
        Integer count = 0;
        for (String eachStringItem : items) {
            try {
                Item item = itemParser.parseStringIntoItem(eachStringItem);
                if (item.getName().equalsIgnoreCase("milk")){
                    count++;
                }
            } catch (ItemParseException e) {
            }
        }


        Assert.assertEquals(expected, count);
    }

    @Test
    public void generateNameAndSeen() throws ItemParseException{
        ArrayList<Item> listOfItems;
        listOfItems = itemParser.parseRawIntoListOfItems(rawAll);
        String actual = itemParser.generateNameCountOutput(listOfItems);
        String expected = "name and seen";
        Assert.assertEquals(expected, actual);
    }


}


