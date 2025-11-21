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
        // sentinal.next is never null under maintained invariants
        sentinal.next.prev = newNode; // old first (or sentinel if empty) now points back to newNode
        sentinal.next = newNode;
        if (size == 0) { // when empty also update sentinel.prev
            sentinal.prev = newNode;
        }
        size ++;
    }

    public void addLast(Item item) {
        Node newNode = new Node(item);
        newNode.next = sentinal;
        newNode.prev = sentinal.prev;
        sentinal.prev.next = newNode; // old last (or sentinel if empty) points forward to newNode
        sentinal.prev = newNode;
        if (size == 0) { // when empty also update sentinel.next
            sentinal.next = newNode;
        }
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
        while (current != sentinal) { // current never null if invariants hold
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }

    public Item removeFirst() {
        if (size == 0) {
            return null;
        }
        Node first = sentinal.next;
        Item removedData = first.data;
        if (size == 1) {
            // restoring empty circular sentinel
            sentinal.next = sentinal;
            sentinal.prev = sentinal;
        } else {
            Node newFirst = first.next;
            sentinal.next = newFirst;
            newFirst.prev = sentinal;
        }
        size--;
        return removedData;
    }

    public Item removeLast() {
        if (size == 0) {
            return null;
        }
        Node last = sentinal.prev;
        Item removedData = last.data;
        if (size == 1) {
            sentinal.next = sentinal;
            sentinal.prev = sentinal;
        } else {
            Node newLast = last.prev;
            sentinal.prev = newLast;
            newLast.next = sentinal;
        }
        size--;
        return removedData;
    }

    public Item get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node current = sentinal.next;
        for (int i = 0; i < index; i++) {
            current = current.next; // safe because we only iterate size steps
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

    // Optional internal invariant checker for tests / debugging
    private boolean checkInvariants() {
        // Empty deque invariants
        if (size == 0) {
            return sentinal.next == sentinal && sentinal.prev == sentinal;
        }
        // Non-empty invariants: forward and backward traversal counts match size
        int forwardCount = 0;
        Node cur = sentinal.next;
        Node lastSeen = null;
        while (cur != sentinal && forwardCount <= size) {
            lastSeen = cur;
            cur = cur.next;
            forwardCount++;
        }
        if (forwardCount != size || cur != sentinal) return false;
        // backward
        int backwardCount = 0;
        cur = sentinal.prev;
        while (cur != sentinal && backwardCount <= size) {
            cur = cur.prev;
            backwardCount++;
        }
        if (backwardCount != size || cur != sentinal) return false;
        // lastSeen should equal sentinal.prev
        return lastSeen == sentinal.prev;
    }
}
