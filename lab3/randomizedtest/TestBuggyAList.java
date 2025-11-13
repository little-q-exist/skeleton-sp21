package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> anr = new AListNoResizing<Integer>();
        BuggyAList<Integer> ba = new BuggyAList<Integer>();
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
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> TL = new BuggyAList<>();

        int N = 50000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                TL.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int Lsize = L.size();
                int TLsize = TL.size();
                System.out.println("Lsize: " + Lsize);
                System.out.println("TLsize: " + TLsize);
                assertEquals(Lsize, TLsize);
            } else if (operationNumber == 2) {
                if (L.size() == 0) continue;
                int Lresult = L.getLast();
                int TLresult = TL.getLast();
                System.out.println("result: " + Lresult + "?=" + TLresult);
            } else if (operationNumber == 3) {
                if (L.size() == 0) continue;
                int Lresult = L.removeLast();
                int TLresult = TL.removeLast();
                System.out.println("result: " + Lresult + "?=" + TLresult);
            }
        }

    }
}
