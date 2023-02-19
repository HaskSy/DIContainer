package org.example.dicontainer.dag;

import org.example.dicontainer.exceptions.CyclicDependencyException;

import java.util.*;

public class DAGraph<T> {

    private final List<DAGNode<T>> nodes = new ArrayList<>();
    private final Deque<T> order = new ArrayDeque<>();

    public DAGraph() { }

    public void addNode(DAGNode<T> node) {
        this.nodes.add(node);
    }

    public void addEdge(DAGNode<T> from, DAGNode<T> to) {
        from.addChild(to);
    }

    public Deque<T> getTopologicalOrder() {
        checkForCycles();
        return order;
    }

    private void checkForCycles() {
        order.clear();
        for (DAGNode<T> node : nodes) {
            if (!node.isVisited()) {
                checkForCycles(node);
            }
        }
    }

    private boolean checkForCycles(DAGNode<T> sourceNode) {
        sourceNode.setBeingVisited(true);
        for (DAGNode<T> child : sourceNode.getChildren()) {
            if (child.isBeingVisited() || (!child.isVisited() && checkForCycles(child))) {
                throw new CyclicDependencyException(
                        "Failed to resolve dependencies! Cyclic dependency found:" + sourceNode.getData() + " AND " + child.getData()
                );
            }
        }
        sourceNode.setBeingVisited(false);
        sourceNode.setVisited(true);
        this.order.push(sourceNode.getData());
        return false;
    }
}
