package org.example;

public class BadRequestException extends RuntimeException {

    public BadRequestException(){
        super("매개변수는 3개여야 합니다.");
    }
}
