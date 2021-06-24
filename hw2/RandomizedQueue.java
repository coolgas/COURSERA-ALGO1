import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item>
{
    private int n = 0;
    private Item[] s;

    public RandomizedQueue()
    {   s = (Item[]) new Object[1]; }

    // is the randomized queue empty?
    public boolean isEmpty()
    {   return n == 0;  }

    // return the number of item on the randomized queue
    public int size()
    {   return n;   }

    // add the item
    public void enqueue(Item item)
    {
        if (item == null) throw new IllegalArgumentException("the input cannot be empty.");
        if (n == s.length) resize(2*s.length);
        s[n++] = item;
    }

    // remove and return a rantom item
    public Item dequeue()
    {   
        if (isEmpty()) throw new NoSuchElementException("the array is empty.");
        int index = StdRandom.uniform(n);
        Item item = s[index];
        s[index] = s[n-1];
        s[n-1] = null;
        if (n > 0 && n == s.length/4) resize(s.length/2);
        n--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample()
    {
        if (isEmpty()) throw new NoSuchElementException("the array is empty.");

        int index = StdRandom.uniform(n);
        Item item = s[index];
        return item;
    }

    private void resize(int capacity)
    {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++)
            copy[i] = s[i];
        s = copy;
    }

    public Iterator<Item> iterator()
    {   return new RandomIterator();    }

    private class RandomIterator implements Iterator<Item>
    {   
        private Item[] sRandom;
        private int index = n;

        public RandomIterator()
        {
            sRandom = (Item[]) new Object[n];
            for (int i = 0; i < index; i++)
                sRandom[i] = s[i];
            StdRandom.shuffle(sRandom);
        }

        public boolean hasNext() 
        {   return index > 0;   }

        public Item next()
        {
            if (!hasNext()) throw new NoSuchElementException("no next element to print");
            return sRandom[--index];
        }
        
        public void remove()
        {   throw new UnsupportedOperationException("this method is not supported");    }

    }

    public static void main(String[] args)
    {
        // this intended to be empty
    }
}
