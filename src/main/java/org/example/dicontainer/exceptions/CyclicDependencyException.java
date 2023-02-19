package org.example.dicontainer.exceptions;

public class CyclicDependencyException extends RuntimeException {
    public CyclicDependencyException(String errorMessage) {
        super(errorMessage);
    }
}
