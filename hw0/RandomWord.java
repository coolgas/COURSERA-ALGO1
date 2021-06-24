import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        int i = 1;
        String word = "";

        while (!StdIn.isEmpty()) {
           String nextWord = StdIn.readString();

            if (StdRandom.bernoulli((double) 1/i)) {
                word = nextWord;
            }
            i += 1;
        }
        StdOut.println(word);
    }
}
