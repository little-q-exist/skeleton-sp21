package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque<Item> implements Iterable<Item>,Deque<Item> {
    private Item[] items;
    private int size;            // number of elements currently stored
    private int front;           // index of first element
    private int back;            // index one past last element

    private static final int INIT_CAPACITY = 8;

    public ArrayDeque() {
        items = (Item[]) new Object[INIT_CAPACITY];
        size = 0;
        front = 0;
        back = 0; // invariant: when empty front == back
    }

    public ArrayDeque(int capcaity) {
        items = (Item[]) new Object[capcaity];
        size = 0;
        front = 0;
        back = 0; // invariant: when empty front == back
    }

    /*
     * Resize to the given capacity. We must copy starting at current 'front' to preserve logical order.
     * After resize we normalize so that 'front' = 0 and 'back' = size.
     */
    private void resize(int capacity) {
        Item[] newItems = (Item[]) new Object[capacity];
        // Copy elements in order starting from front
        for (int i = 0; i < size; i++) {
            newItems[i] = items[getIndex(front + i)];
        }
        items = newItems;
        front = 0;
        back = size;
    }

    // Normalize any (possibly negative) index into [0, items.length)
    private int getIndex(int index) {
        int m = items.length;
        int r = index % m; // Java's % keeps sign of dividend
        return r >= 0 ? r : r + m;
    }

    // Ensure there is room for one more element.
    private void growIfNeeded() {
        if (size == items.length) {
            resize(items.length * 2);
        }
    }

    // Shrink if usage factor < 25% and capacity >= 16.
    private void shrinkIfNeeded() {
        if (items.length >= 16 && size < items.length / 4) {
            int newCap = Math.max(INIT_CAPACITY, items.length / 2);
            // Only shrink if newCap still fits all elements.
            if (newCap >= size) {
                resize(newCap);
            }
        }
    }

    @Override
    public void addFirst(Item item) {
        if (item == null) {
            return; // or throw IllegalArgumentException; choice: ignore null adds
        }
        growIfNeeded();
        front = getIndex(front - 1); // move front backward
        items[front] = item;
        size++;
    }

    @Override
    public void addLast(Item item) {
        if (item == null) {
            return; // ignore null adds
        }
        growIfNeeded();
        items[back] = item;
        back = getIndex(back + 1); // advance back
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(items[getIndex(front + i)] + (i == size - 1 ? "" : " "));
        }
        System.out.println();
    }

    @Override
    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Item item = items[front];
        items[front] = null; // avoid loitering
        front = getIndex(front + 1);
        size--;
        shrinkIfNeeded();
        return item;
    }

    @Override
    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        int lastIndex = getIndex(back - 1);
        Item item = items[lastIndex];
        items[lastIndex] = null;
        back = lastIndex; // move back backward
        size--;
        shrinkIfNeeded();
        return item;
    }

    @Override
    public Item get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int absoluteIndex = getIndex(front + index);
        return items[absoluteIndex];
    }

    // Optional: expose current capacity (helpful for tests / debugging)
    int capacity() {
        return items.length;
    }

    public Iterator<Item> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<Item> {
        private int pos;
        private int count;

        ArrayDequeIterator() {
            pos = front;
            count = 0;
        }

        @Override
        public boolean hasNext() {
            return count < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = items[pos];
            pos = getIndex(pos + 1);
            count ++;
            return item;
        }
    }
}
