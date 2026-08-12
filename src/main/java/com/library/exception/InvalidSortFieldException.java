package com.library.exception;

public class InvalidSortFieldException extends RuntimeException{
    public InvalidSortFieldException(String mssg){
        super(mssg);
    }
}
