package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    private static double addLastHelper(int N) {
        AList<Integer> testAList = new AList<Integer>();
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < N;i++) {
            testAList.addLast(1);
        }
        double timeInSeconds = sw.elapsedTime();
        return timeInSeconds;
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<Integer>();
        AList<Double> times = new AList<Double>();
        AList<Integer> opCounts = new AList<Integer>();
        int[] samples = new int[]{1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000};
        for (int sample : samples) {
            Ns.addLast(sample);
            opCounts.addLast(sample);
            times.addLast(addLastHelper(sample));
        }
        printTimingTable(Ns, times, opCounts);
    }
}
