package org.example.application;

import org.example.application.interfaces.UserService;
import org.example.application.services.UserServiceImpl;
import org.example.dicontainer.ApplicationRunner;
import org.example.dicontainer.DIContainer;
import org.example.dicontainer.annotations.DIContainerApplication;

@DIContainerApplication
public class Main {
    public static void main(String[] args) {
        DIContainer container = ApplicationRunner.run(Main.class);

        UserService userService = container.getInstance(UserServiceImpl.class);
        System.out.println("Username: " + userService.getUsername());
    }
}
