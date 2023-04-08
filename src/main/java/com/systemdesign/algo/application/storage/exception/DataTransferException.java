package com.systemdesign.algo.application.storage.exception;

public class DataTransferException extends Exception{
    public DataTransferException(String errorMessage, Exception ex){
        super(String.format(errorMessage, ex));
    }
    
}
