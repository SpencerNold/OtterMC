package ottermc.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node<T> {

    private final T value;
    final List<Node<T>> children;
    Node<T> parent;

    public Node(T value) {
        this.value = value;
        this.children = new ArrayList<>();
        this.parent = null;
    }

    public void add(Node<T> child) {
        child.parent = this;
        children.add(child);
    }

    public Node<T> getParent() {
        return parent;
    }

    public List<Node<T>> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public T getValue() {
        return value;
    }
}
