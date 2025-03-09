package ru.kata.spring.boot_security.demo.exception_handling;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
