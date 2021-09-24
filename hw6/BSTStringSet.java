import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Weijie Yuan
 */
public class BSTStringSet implements SortedStringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        _root = put(s, _root);
    }

    /** Help function for put. Return a BSTString set after add
     * new string s. */
    private Node put(String s, Node n) {
        if (n == null) {
            return new Node(s);
        }
        if (s.compareTo(n.s) < 0) {
            n.left = put(s, n.left);
        } else if (s.compareTo(n.s) > 0) {
            n.right = put(s, n.right);
        }
        return n;
    }

    @Override
    public boolean contains(String s) {
        return contains(s, _root);
    }

    /** Help function for contains. Return true iff BSTStringSet
     * rooted at n contains string s. */
    private boolean contains(String s, Node n) {
        if (n == null) {
            return false;
        }
        if (s.compareTo(n.s) == 0) {
            return true;
        } else if (s.compareTo(n.s) > 0) {
            return contains(s, n.right);
        } else {
            return contains(s, n.left);
        }
    }

    @Override
    public List<String> asList() {
        List<String> res = new ArrayList<>();
        Iterator<String> Iter = new BSTIterator(_root);
        while (Iter.hasNext()) {
            res.add(Iter.next());
        }
        return res;
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    @Override
    public Iterator<String> iterator(String low, String high) {
        return new BSTIteratorBounded(low, high);

    }

    private class BSTIteratorBounded implements Iterator<String> {

        private Stack<Node> toDo;
        private String upper;
        private String lower;

        public BSTIteratorBounded(String L, String U){
            this.upper = U;
            this.lower = L;
            this.toDo = new Stack<>();
            addTree(_root);
        }

        @Override
        public boolean hasNext() {
            return (!toDo.isEmpty())
                    && (toDo.peek().s.compareTo(this.upper) <= 0);
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void addTree(Node node) {
            while (node != null && node.s.compareTo(lower) >= 0) {
                toDo.push(node);
                node = node.left;
            }
            if (node != null) {
                addTree(node.right);
            }
        }
    }

    /** Root node of the tree. */
    private Node _root;
}
