package org.example.dicontainer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

class ClasspathScanner {
    public static Set<Class<?>> getClassesInPackage(String packageToScan) throws ClassNotFoundException, IOException {
        Set<Class<?>> classes = new HashSet<>();

        String packagePath = packageToScan.replace('.', '/');
        Enumeration<URL> resources = ClassLoader.getSystemClassLoader().getResources(packagePath);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getProtocol().equals("file")) {
                File directory = new File(URLDecoder.decode(resource.getFile(), StandardCharsets.UTF_8));
                scanForClasses(directory, packageToScan, classes);
            }
        }

        return classes;
    }

    private static void scanForClasses(File directory, String packageToScan, Set<Class<?>> classes) throws ClassNotFoundException {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        String subPackageName = packageToScan + "." + file.getName();
                        scanForClasses(file, subPackageName, classes);
                    } else if (file.getName().endsWith(".class")) {
                        String className = packageToScan + '.' + file.getName().substring(0, file.getName().length() - 6);
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    }
                }
            }
        }
    }
}
