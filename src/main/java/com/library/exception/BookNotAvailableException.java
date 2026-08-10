package com.library.exception;

public class BookNotAvailableException extends RuntimeException{
    public BookNotAvailableException(String mssg){ super(mssg);}
}
