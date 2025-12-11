package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;

public class GuitarHero {
    public static final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    public static void main(String[] args) {
        GuitarString[] guitarStrings = new GuitarString[37];
        for (int i = 0; i < guitarStrings.length; i++) {
            double frequency = 440 * Math.pow(2, (i - 24) / 12.0);
            guitarStrings[i] = new GuitarString(frequency);
        }

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index != -1) {
                    guitarStrings[index].pluck();
                }
            }

            double sample = 0;
            for (GuitarString guitarString : guitarStrings) {
                sample += guitarString.sample();
            }

            StdAudio.play(sample);

            for (GuitarString guitarString : guitarStrings) {
                guitarString.tic();
            }
        }
    }
}
