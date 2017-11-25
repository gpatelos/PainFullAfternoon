package io.zipcoder;

public class ItemParseException extends Exception {

    public ItemParseException(String reason, String statement){
        super(reason + ":" + statement);
    }
}
