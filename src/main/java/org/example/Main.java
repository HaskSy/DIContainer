package org.example;

import org.example.application.interfaces.UserService;
import org.example.application.services.UserServiceImpl;
import org.example.dicontainer.DIContainer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        DIContainer container = new DIContainer();
        try {
            container.scan("org.example.application");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UserService userService = container.getInstance(UserServiceImpl.class);
        System.out.println("Username: " + userService.getUsername());
    }
}