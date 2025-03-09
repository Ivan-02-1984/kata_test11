package ru.kata.spring.boot_security.demo.exception_handling;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
