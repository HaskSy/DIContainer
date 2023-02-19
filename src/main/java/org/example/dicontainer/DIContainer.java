package org.example.dicontainer;

import org.example.dicontainer.annotations.Autowired;
import org.example.dicontainer.annotations.Component;
import org.example.dicontainer.dag.DAGNode;
import org.example.dicontainer.dag.DAGraph;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DIContainer {
    private final Map<Class<?>, Object> instances = new HashMap<>();

    public void scan(String packageToScan) throws ClassNotFoundException, IOException {
        // Scan the package for annotated classes
        Set<Class<?>> classes = ClasspathScanner.getClassesInPackage(packageToScan).stream()
                .filter((Class<?> clazz) -> clazz.isAnnotationPresent(Component.class)).collect(Collectors.toSet());

        Deque<Class<?>> dependencyOrder = orderDependenciesResolve(classes);
        while (!dependencyOrder.isEmpty()) {
            Class<?> clazz = dependencyOrder.removeLast();
            Object instance = createInstance(clazz);
            this.instances.put(clazz, instance);
        }
    }

    public <T> T getInstance(Class<T> clazz) {
        if (!this.instances.containsKey(clazz)) {
            throw new IllegalArgumentException("No instance found for " + clazz);
        }
        return clazz.cast(this.instances.get(clazz));
    }

    private Object createInstance(Class<?> clazz) {
        try {
            // Find the constructor with @Autowired annotation, or the default constructor
            Constructor<?> constructor = findAutowiredConstructor(clazz);
            Object[] params = resolveConstructorParams(constructor);
            // Instantiate the object using the constructor and parameters
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to create instance of " + clazz, e);
        }
    }

    private Constructor<?> findAutowiredConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                return constructor;
            }
        }
        return constructors[0];
    }

    private Object[] resolveConstructorParams(Constructor<?> constructor) {
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> paramType = paramTypes[i];
            params[i] = getInstance(paramType);
        }
        return params;
    }

    private Deque<Class<?>> orderDependenciesResolve(Set<Class<?>> classes) {
        DAGraph<Class<?>> dependencyGraph = new DAGraph<>();
        for (Class<?> clazz : classes) {
            DAGNode<Class<?>> from = DependencyFactory.createDependency(clazz);
            dependencyGraph.addNode(from);
            Constructor<?> constructor = findAutowiredConstructor(clazz);
            Class<?>[] paramTypes = constructor.getParameterTypes();
            for (Class<?> paramType : paramTypes) {
                DAGNode<Class<?>> to = DependencyFactory.createDependency(paramType);
                dependencyGraph.addEdge(from, to);
            }
        }
        return dependencyGraph.getTopologicalOrder();
    }
}
