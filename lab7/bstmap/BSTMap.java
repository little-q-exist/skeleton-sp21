package bstmap;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>, Iterable<K> {
    private class BSTNode {
        private K key = null;
        private V value = null;
        private BSTNode left = null;
        private BSTNode right = null;

        BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "{" + key.toString() + ", " + value.toString() + "}";
        }
    }

    private BSTNode root = null;
    private int size = 0;

    /**
     * Prints out your BSTMap in order of increasing Key.
     */
    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(BSTNode node) {
        if (node == null) {
            return;
        }
        printInOrder(node.left);
        System.out.println(node);
        printInOrder(node.right);
    }

    /**
     * Removes all of the mappings from this map.
     */
    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    /**
     * Returns true if this map contains a mapping for the specified key.
     *
     * @param key
     */
    @Override
    public boolean containsKey(K key) {
        return containsKey(key, root);
    }

    private boolean containsKey(K key, BSTNode node) {
        if (node == null) {
            return false;
        }
        int compareResult = node.key.compareTo(key);
        if (compareResult > 0) {
            return containsKey(key, node.left);
        } else if (compareResult < 0) {
            return containsKey(key, node.right);
        } else {
            return true;
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     *
     * @param key
     */
    @Override
    public V get(K key) {
        return get(key, root);
    }

    /**
     * helper function of get(K key)
     * @return the founded node or null, if not found
     */
    private BSTNode find(K key, BSTNode node) {
        if (node == null) {
            return null;
        }
        int compareResult = node.key.compareTo(key);
        if (compareResult > 0) {
            return find(key, node.left);
        } else if (compareResult < 0) {
            return find(key, node.right);
        } else {
            return node;
        }
    }

    /**
     * helper function of get(K key)
     * @return the value of founded node or null, if not found
     */
    private V get(K key, BSTNode node) {
        BSTNode currentNode = find(key, node);
        if (currentNode != null) {
            return currentNode.value;
        }
        return null;
    }

    /**
     * Returns the number of key-value mappings in this map.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     *
     * @param key
     * @param value
     */
    @Override
    public void put(K key, V value) {
        root = put(key, value, root);
        size ++;
    }

    private BSTNode put(K key, V value, BSTNode node) {
        if (node == null) {
            return new BSTNode(key, value);
        }
        int compareResult = node.key.compareTo(key);
        if (compareResult >= 0) {
            node.left = put(key,value, node.left);
        } else {
            node.right = put(key,value, node.right);
        }
        return node;
    }

    /**
     * Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException.
     */
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        Iterator<K> it = new BSTIterator();
        while (it.hasNext()) {
            set.add(it.next());
        }
        return set;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException.
     *
     * @param key
     */
    @Override
    public V remove(K key) {
        V value = get(key);
        if (value != null) {
            size --;
        }
        root = remove(key, root);
        return value;
    }

    private BSTNode remove(K key, BSTNode node) {
        if (node == null) {
            return null;
        }
        int compareResult = node.key.compareTo(key);
        if (compareResult > 0) {
            node.left = remove(key, node.left);
        } else if (compareResult < 0) {
            node.right = remove(key, node.right);
        } else {
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            BSTNode successor = getSuccessor(node);
            node.key = successor.key;
            node.value = successor.value;
            remove(successor.key, node.right);
        }
        return node;
    }

    private BSTNode getSuccessor(BSTNode node) {
        BSTNode rightSubTree = node.right;
        BSTNode currentNode = rightSubTree;
        while (currentNode.left != null) {
            currentNode = node.left;
        }
        return currentNode;
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.
     *
     * @param key
     * @param value
     */
    @Override
    public V remove(K key, V value) {
        if (get(key) == value) {
            remove(key);
        }
        return null;
    }

    private class BSTIterator implements Iterator<K> {
        private BSTNode node;
        Queue<BSTNode> queue;

        public BSTIterator() {
            node = root;
            queue = new LinkedList<>();
            if (node != null) {
                queue.add(node);
            }
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            node = queue.poll();
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
            return node.key;
        }
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<K> iterator() {
        return new BSTIterator();
    }
}
