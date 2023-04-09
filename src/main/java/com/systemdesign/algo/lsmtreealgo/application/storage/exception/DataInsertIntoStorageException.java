package com.systemdesign.algo.lsmtreealgo.application.storage.exception;

public class DataInsertIntoStorageException extends Exception{
    public DataInsertIntoStorageException(String errorMessage, Exception ex){
        super(String.format(errorMessage, ex.getMessage()));
    }
}
