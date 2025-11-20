package deque;

public class ArrayDeque<Item> {
    private Item[] items;
    private int size;
    private int front;
    private int back;

    public ArrayDeque() {
        items = (Item[]) new Object[8];
        size = 0;
        front = 0;
        back = 0;
    }

    /*
    The amount of memory that your program uses at any given time must be proportional to the number of items.
     For example, if you add 10,000 items to the deque, and then remove 9,999 items, you shouldn’t still be using an array of length 10,000ish. For arrays of length 16 or more, your usage factor should always be at least 25%. This means that before performing a remove operation that will bring the number of elements in the array under 25% the length of the array, you should resize the size of the array down.
     For smaller arrays, your usage factor can be arbitrarily low.
     */
    private void resize(int capacity) {
        Item[] newItems = (Item[]) new Object[capacity];
        if (size >= 0) System.arraycopy(items, 0, newItems, 0, size);
        items = newItems;
    }

    private int getIndex(int index) {
        return (index % items.length + items.length) % items.length;
    }

    public void addFirst(Item item) {
        if (size >= items.length || back == front) {
            resize(size * 2);
        }
        front = getIndex(front - 1);
        items[getIndex(front)] = item;
        size ++;
    }

    public void addLast(Item item) {
        if (size >= items.length || back == front) {
            resize(size * 2);
        }
        items[getIndex(back)] = item;
        back = getIndex(back + 1);
        size ++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = front; i < back; i = getIndex(i + 1)) {
            System.out.print(items[0]);
        }
        System.out.println();
    }

    public Item removeFirst() {
        Item item = items[front];
        front = getIndex(front + 1);
        size --;
        return item;
    }

    public Item removeLast() {
        Item item = items[getIndex(back - 1)];
        back = getIndex(back - 1);
        size --;
        return item;
    }

    public Item get(int index) {
        int absoluteIndex = getIndex(front + index);
        return items[absoluteIndex];
    }
}
