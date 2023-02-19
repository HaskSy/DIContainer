package org.example.dicontainer.dag;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class DAGNode<T> {
    private T data;
    private boolean beingVisited;
    private boolean visited;
    private List<DAGNode<T>> children;

    public DAGNode(T data) {
        this.data = data;
        this.children = new ArrayList<>();
    }

    public void addChild(DAGNode<T> adjacent) {
        if (children.contains(adjacent)) {
            return;
        }
        this.children.add(adjacent);
    }
}
