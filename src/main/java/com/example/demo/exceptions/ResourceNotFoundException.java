package com.example.demo.exceptions;//Exception class checks the entities existence in tables
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
