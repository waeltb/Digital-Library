package net.corilus.userservice.exception;

public class EmailExistsExecption extends RuntimeException{
    public EmailExistsExecption(String message){
        super(message);
    }
}
