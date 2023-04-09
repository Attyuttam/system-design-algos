package com.systemdesign.algo.lsmtreealgo.application.storage.exception;

public class DataDeleteFromStorageException extends Exception{
    public DataDeleteFromStorageException(String errorMessage, Exception ex){
        super(String.format(errorMessage, ex.getMessage()));
    }
}
