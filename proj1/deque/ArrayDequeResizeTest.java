package deque;

public class ArrayDequeResizeTest {
    public static void main(String[] args) {
        ArrayDeque<Integer> dq = new ArrayDeque<>();
        // Fill beyond initial capacity to trigger growth
        int N = 40; // will force multiple resizes
        for (int i = 0; i < N; i++) {
            if (i % 2 == 0) {
                dq.addFirst(i); // mix adds to both ends
            } else {
                dq.addLast(i);
            }
        }
        // Validate order by printing
        dq.printDeque();
        System.out.println("Size=" + dq.size() + ", Capacity=" + dq.capacity());

        // Randomly remove half from both ends
        for (int i = 0; i < N / 2; i++) {
            if (i % 2 == 0) {
                dq.removeFirst();
            } else {
                dq.removeLast();
            }
        }
        dq.printDeque();
        System.out.println("After removals Size=" + dq.size() + ", Capacity=" + dq.capacity());

        // Force shrink: remove until usage < 25%
        while (dq.size() > 0) {
            dq.removeLast();
            if (dq.capacity() >= 16) {
                double usage = (double) dq.size() / dq.capacity();
                if (usage < 0.25) {
                    System.out.println("Usage=" + usage + " Size=" + dq.size() + " Capacity=" + dq.capacity());
                }
            }
        }
        System.out.println("Final Size=" + dq.size() + ", Capacity=" + dq.capacity());
    }
}
