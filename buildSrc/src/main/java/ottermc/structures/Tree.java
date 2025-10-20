package ottermc.structures;

import java.util.function.Predicate;

public class Tree<T> {

    private Node<T> target;

    public Tree(T value) {
        target = new Node<>(value);
    }

    public void add(T value) {
        target.add(new Node<>(value));
    }

    public void addAndStep(T value) {
        Node<T> node = new Node<>(value);
        target.add(node);
        target = node;
    }

    public boolean navigate(T value) {
        Node<T> node = find(target, val -> val.equals(value));
        if (node == null)
            return false;
        target = node;
        return true;
    }

    public boolean back() {
        Node<T> node = target.parent;
        if (node == null)
            return false;
        target = node;
        return true;
    }

    public T getCurrentValue() {
        return target.getValue();
    }

    public boolean isRoot() {
        return target.parent == null;
    }

    private Node<T> find(Node<T> node, Predicate<T> predicate) {
        for (Node<T> child : node.children) {
            boolean test = predicate.test(child.getValue());
            if (test)
                return child;
            return find(child, predicate);
        }
        return null;
    }
}
