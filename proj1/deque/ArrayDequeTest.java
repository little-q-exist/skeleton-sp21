package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class ArrayDequeTest {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        LinkedListDeque<Integer> anr = new LinkedListDeque<Integer>();
        ArrayDeque<Integer> ba = new ArrayDeque<Integer>();
        for (int i = 0; i < 3; i++) {
            anr.addLast(i);
            ba.addLast(i);
        }
        for (int i = 0; i< 3; i++) {
            assertEquals(anr.removeLast(), ba.removeLast());
        }
    }

    @Test
    public void randomizedFunctionCall() {
        LinkedListDeque<Integer> L = new LinkedListDeque<>();
        ArrayDeque<Integer> TL = new ArrayDeque<>();

        int N = 50000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(-85, 1589964);
                L.addLast(randVal);
                TL.addLast(randVal);

                // addFront
                L.addLast(randVal);
                TL.addLast(randVal);

            } else if (operationNumber == 1) {
                // size
                int Lsize = L.size();
                int TLsize = TL.size();
                assertEquals(Lsize, TLsize);
            } else if (operationNumber == 2) {
                if (L.isEmpty()) continue;
                int randVal = StdRandom.uniform(0, L.size() - 1);
                int Lresult = L.get(randVal);
                int TLresult = TL.get(randVal);
                assertEquals(Lresult, TLresult);

            } else if (operationNumber == 3) {
                if (L.size() < 2) continue;
                int Lresult1 = L.removeLast();
                int TLresult1 = TL.removeLast();
                assertEquals(Lresult1, TLresult1);

                int Lresult2 = L.removeFirst();
                int TLresult2 = TL.removeFirst();
                assertEquals(Lresult2, TLresult2);

            }
        }

    }
}
