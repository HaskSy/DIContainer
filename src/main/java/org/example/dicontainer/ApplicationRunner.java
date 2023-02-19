package org.example.dicontainer;

import org.example.dicontainer.annotations.DIContainerApplication;

import java.io.IOException;

public class ApplicationRunner {
    public static DIContainer run(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(DIContainerApplication.class)) {
            throw new IllegalArgumentException("Class is not annotated with @DIContainerApplication");
        }
        try {
            DIContainer container = new DIContainer();
            container.scan(clazz.getPackageName());
            return container;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
