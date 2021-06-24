import java.util.Iterator;

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0], 10);
        RandomizedQueue<String> randomQueue = new RandomizedQueue<>();

        do
        {
            String input = StdIn.readString();
            randomQueue.enqueue(input);
        }
        while (!StdIn.isEmpty());

        Iterator<String> i = randomQueue.iterator();
        while (k > 0)
        {
            System.out.println(i.next());
            k--;
        }
    }
}
