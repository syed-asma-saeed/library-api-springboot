package com.library.exception;

public class BorrowLimitExceededException extends RuntimeException{
    public BorrowLimitExceededException(String mssg){ super(mssg);}
}
