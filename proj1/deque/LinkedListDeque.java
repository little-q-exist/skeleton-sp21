package deque;

public class LinkedListDeque<Item> {
    class Node {
        Item data;
        Node prev;
        Node next;

        public Node(Item data) {
            this.data = data;
            prev = null;
            next = null;
        }
    }

    private Node sentinal;
    private int size;

    public LinkedListDeque() {
         sentinal = new Node(null);
         sentinal.next = sentinal;
         sentinal.prev = sentinal;
         size = 0;
    }

    public void addFirst(Item item) {
        Node newNode = new Node(item);
        newNode.prev = sentinal;
        newNode.next = sentinal.next;
        if (sentinal.next != null) {
            sentinal.next.prev = newNode;
        }
        sentinal.next = newNode;
        size ++;
    }

    public void addLast(Item item) {
        Node newNode = new Node(item);
        newNode.next = sentinal;
        newNode.prev = sentinal.prev;
        if (sentinal.prev != null) {
            sentinal.prev.next = newNode;
        }
        sentinal.prev = newNode;

        size ++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node current = sentinal.next;
        while (current != null && current != sentinal) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }

    public Item removeFirst() {
        if (sentinal.next == null || sentinal.next == sentinal) {
            return null;
        }
        Item removedData = sentinal.next.data;
        sentinal.next.prev = sentinal;
        sentinal.next = sentinal.next.next;
        size--;
        return removedData;
    }

    public Item removeLast() {
        if (sentinal.prev == null || sentinal.prev == sentinal) {
            return null;
        }
        Item removedData = sentinal.prev.data;
        sentinal.prev = sentinal.prev.prev;
        sentinal.prev.next = sentinal;
        size--;
        return removedData;
    }

    public Item get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node current = sentinal.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public Item getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        } else {
            return getRecursiveHelper(index, 0, sentinal.next).data;
        }
    }

    private Node getRecursiveHelper(int index, int current, Node node) {
        if (current == index) {
            return node;
        } else {
            return getRecursiveHelper(index, current + 1, node.next);
        }
    }
}
