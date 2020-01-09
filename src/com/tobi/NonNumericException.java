package com.tobi;

public class NonNumericException extends Exception {

    public NonNumericException(){
        super("Non Numeric Data Entered from Input");
    }
    public NonNumericException(String msg){
        super(msg);
    }
}
