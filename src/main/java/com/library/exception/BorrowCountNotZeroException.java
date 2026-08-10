package com.library.exception;

public class BorrowCountNotZeroException extends RuntimeException{
    public BorrowCountNotZeroException(String mssg){ super(mssg);}
}
