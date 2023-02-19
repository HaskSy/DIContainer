package org.example.dicontainer;

import org.example.dicontainer.dag.DAGNode;

import java.util.HashMap;
import java.util.Map;

class DependencyFactory {
    private static final Map<Class<?>, DAGNode<Class<?>>> dependenciesCache = new HashMap<>();

    protected static DAGNode<Class<?>> createDependency(Class<?> clazz) {
        return dependenciesCache.computeIfAbsent(clazz, DAGNode::new);
    }
}
