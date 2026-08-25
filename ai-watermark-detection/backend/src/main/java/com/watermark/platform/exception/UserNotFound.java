package com.watermark.platform.exception;

public class UserNotFound extends  RuntimeException{

    public UserNotFound(String message){
        super(message);
    }
}
