package com.backend.backend_application.Exception;

public class RessourceNotFound extends RuntimeException{
    public RessourceNotFound(String message){
        super(message);
    }
}
