package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Q
 */
public class MyHashMap<K, V> implements Map61B<K, V>, Iterable<K> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private HashSet<K> keyset = new HashSet<>();
    // You should probably define some more!
    final int INITIAL_SIZE = 16;
    final double LOAD_FACTOR = 0.75;
    int initialSize = INITIAL_SIZE;
    double maxLoad = LOAD_FACTOR;
    int size = 0;

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(INITIAL_SIZE);
    }

    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize);
        this.initialSize = initialSize;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        this.initialSize = initialSize;
        this.maxLoad = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

     /**
     * Removes all of the mappings from this map.
     */
    @Override
    public void clear() {
        buckets = createTable(initialSize);
        keyset = new HashSet<>();
        size = 0;
    }

    /**
     * Returns true if this map contains a mapping for the specified key.
     *
     * @param key
     */
    @Override
    public boolean containsKey(K key) {
        Collection<Node> bucket = buckets[getBucketIndex(key)];
        return find(bucket, key) != null;
    }

    /**
     * Helper function to find the index of the bucket from current table with a specific key
     */
    private int getBucketIndex(K key) {
        int hash = key.hashCode();
        return Math.floorMod(hash, buckets.length);
    }

    /**
     * Helper function to find the index of the bucket from specified table with a specific key
     * @param key
     * @return
     */
    private int getBucketIndex(Collection<Node>[] buckets, K key) {
        int hash = key.hashCode();
        return Math.floorMod(hash, buckets.length);
    }

    /**
     * Helper function to find a node with a specific key in one bucket
     * @param bucket
     * @param key
     * @return
     */
    private Node find(Collection<Node> bucket, K key) {
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    private Collection<Node> findNodeAndUpdate(Collection<Node> bucket, K key, V value) {
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                node.value = value;
            }
        }
        return bucket;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     *
     * @param key
     */
    @Override
    public V get(K key) {
        Collection<Node> bucket = buckets[getBucketIndex(key)];
        Node node = find(bucket, key);
        return node == null? null : node.value;
    }

    /**
     * Returns the number of key-value mappings in this map.
     */
    @Override
    public int size() {
        return size;
    }

    private void resizeWhenNeeded() {
        double load = 1.0 * size / buckets.length;
        if (load > maxLoad) {
            resize();
        }
    }

    private void resize() {
        int newBucketsSize = buckets.length * 2;
        Collection<Node>[] newBuckets = createTable(newBucketsSize);
        // iterate all Nodes and place them in the new bucket.
        for (K key: this) {
            V value = get(key);
            concatToBucket(newBuckets, key, value);
        }
        buckets = newBuckets;
    }

    private Collection<Node>[] updateBucket(Collection<Node>[] buckets, K key, V value) {
        int bucketIndex = getBucketIndex(buckets, key);
        if (buckets[bucketIndex] == null) {
            buckets[bucketIndex] = createBucket();
        }
        buckets[bucketIndex] = findNodeAndUpdate(buckets[bucketIndex], key, value);
        return buckets;
    }

    /**
     * Add a key-value pair to a bucket. Create a bucket if not exist.
     * @return a new table after the add operation
     */
    private Collection<Node>[] concatToBucket(Collection<Node>[] buckets, K key, V value) {
        int bucketIndex = getBucketIndex(buckets, key);
        if (buckets[bucketIndex] == null) {
            buckets[bucketIndex] = createBucket();
        }
        Node node = createNode(key, value);
        buckets[bucketIndex].add(node);
        return buckets;
    }

    /**
     * Add a node to a bucket. Create a bucket if not exist.
      @return a new bucket table after the add operation
     */
    private Collection<Node>[] concatToBucket(Collection<Node>[] buckets, Node node) {
        int bucketIndex = getBucketIndex(buckets, node.key);
        if (buckets[bucketIndex] == null) {
            buckets[bucketIndex] = createBucket();
        }
        buckets[bucketIndex].add(node);
        return buckets;
    }


    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     *
     * @param key
     * @param value
     */
    @Override
    public void put(K key, V value) {
        resizeWhenNeeded();
        if (containsKey(key)) {
            // update value when key is not unique
            buckets = updateBucket(buckets, key, value);
        } else {
            buckets = concatToBucket(this.buckets, key, value);
            keyset.add(key);
            size++;
        }
    }

    /**
     * Returns a Set view of the keys contained in this map.
     */
    @Override
    public Set<K> keySet() {
        HashSet<K> hashSet = new HashSet<>();
        for (K k : this) {
            hashSet.add(k);
        }
        return hashSet;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     *
     * @param key
     */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     *
     * @param key
     * @param value
     */
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }

    private class MyHashMapIterator implements Iterator<K> {
        Iterator<K> it = keyset.iterator();

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public K next() {
            return it.next();
        }
    }
}
