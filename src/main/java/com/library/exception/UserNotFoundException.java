package com.library.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String mssg){ super(mssg);}
}
