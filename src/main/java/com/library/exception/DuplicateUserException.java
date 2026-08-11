package com.library.exception;

public class DuplicateUserException extends RuntimeException{
    public DuplicateUserException(String mssg){ super(mssg);}
}
