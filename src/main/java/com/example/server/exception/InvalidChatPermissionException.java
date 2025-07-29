package com.example.server.exception;

public class InvalidChatPermissionException extends RuntimeException {
    public InvalidChatPermissionException(String message) {
        super(message);
    }
}