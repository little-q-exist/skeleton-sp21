package deque;

import java.util.Iterator;

public class LinkedListDeque<Item> implements Deque<Item> {
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

    private final Node sentinel;
    private int size;

    public LinkedListDeque() {
         sentinel = new Node(null);
         sentinel.next = sentinel;
         sentinel.prev = sentinel;
         size = 0;
    }

    public void addFirst(Item item) {
        Node newNode = new Node(item);
        newNode.prev = sentinel;
        newNode.next = sentinel.next;
        // sentinel.next is never null under maintained invariants
        sentinel.next.prev = newNode; // old first (or sentinel if empty) now points back to newNode
        sentinel.next = newNode;
        if (size == 0) { // when empty also update sentinel.prev
            sentinel.prev = newNode;
        }
        size ++;
    }

    public void addLast(Item item) {
        Node newNode = new Node(item);
        newNode.next = sentinel;
        newNode.prev = sentinel.prev;
        sentinel.prev.next = newNode; // old last (or sentinel if empty) points forward to newNode
        sentinel.prev = newNode;
        if (size == 0) { // when empty also update sentinel.next
            sentinel.next = newNode;
        }
        size ++;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node current = sentinel.next;
        while (current != sentinel) { // current never null if invariants hold
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }

    public Item removeFirst() {
        if (size == 0) {
            return null;
        }
        Node first = sentinel.next;
        Item removedData = first.data;
        if (size == 1) {
            // restoring empty circular sentinel
            sentinel.next = sentinel;
            sentinel.prev = sentinel;
        } else {
            Node newFirst = first.next;
            sentinel.next = newFirst;
            newFirst.prev = sentinel;
        }
        size--;
        return removedData;
    }

    public Item removeLast() {
        if (size == 0) {
            return null;
        }
        Node last = sentinel.prev;
        Item removedData = last.data;
        if (size == 1) {
            sentinel.next = sentinel;
            sentinel.prev = sentinel;
        } else {
            Node newLast = last.prev;
            sentinel.prev = newLast;
            newLast.next = sentinel;
        }
        size--;
        return removedData;
    }

    public Item get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node current = sentinel.next;
        for (int i = 0; i < index; i++) {
            current = current.next; // safe because we only iterate size steps
        }
        return current.data;
    }

    public Iterator<Item> iterator() {
        return null;
    }

    public Item getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        } else {
            return getRecursiveHelper(index, 0, sentinel.next).data;
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
            return sentinel.next == sentinel && sentinel.prev == sentinel;
        }
        // Non-empty invariants: forward and backward traversal counts match size
        int forwardCount = 0;
        Node cur = sentinel.next;
        Node lastSeen = null;
        while (cur != sentinel && forwardCount <= size) {
            lastSeen = cur;
            cur = cur.next;
            forwardCount++;
        }
        if (forwardCount != size || cur != sentinel) return false;
        // backward
        int backwardCount = 0;
        cur = sentinel.prev;
        while (cur != sentinel && backwardCount <= size) {
            cur = cur.prev;
            backwardCount++;
        }
        if (backwardCount != size || cur != sentinel) return false;
        // lastSeen should equal sentinel.prev
        return lastSeen == sentinel.prev;
    }
}
