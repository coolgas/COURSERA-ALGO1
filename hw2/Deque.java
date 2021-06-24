import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item>
{
    private int n = 0;
    private Node last, first;

    private class Node
    {
        Item item;
        Node next;
        Node previous;
    }

    // is the deque empty
    public boolean isEmpty()
    {   return n == 0;  }

    // return the number of items on the deque
    public int size()
    {   return n;   }

    // add the item to the front
    public void addFirst(Item item)
    {
        if (item == null) throw new IllegalArgumentException("the input value cannot be empty.");
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.previous = null;
        first.next = oldfirst;
        if (n == 0) last = first;
        else        oldfirst.previous = first;
        n++;
    }

    // add the item to the back
    public void addLast(Item item)
    {
        if (item == null) throw new IllegalArgumentException("the input value cannot be empty.");
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.previous = oldlast;
        if (oldlast != null) oldlast.next = last;
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst()
    {
        if (n == 0) throw new NoSuchElementException("there is nothing to remove");
        Item item = first.item;
        if (n == 1)
        {
            first = null;
            last = null;
        }
        else
        {
            first = first.next;
            first.previous = null;
        }
        n--;
        return item;
    }

    // remove and return the item from the baclk
    public Item removeLast()
    {
        if (n == 0) throw new NoSuchElementException("there is nothing to remove");
        Item item = last.item;
        if (n == 1)
        {
            first = null;
            last = null;
        }
        else
        {
            last = last.previous;
            last.next = null;
        }
        n--;
        return item;
    }

    // the construction of the iterator
    public Iterator<Item> iterator()
    {   return new ListIterator();   }

    private class ListIterator implements Iterator<Item>
    {
        private Node current = first;

        public boolean hasNext() {  return current != null;  }
        public void remove() {  throw new UnsupportedOperationException("this method is not supported.");    }
        public Item next()
        {
            if (!hasNext()) throw new NoSuchElementException("there is no next element.");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args)
    {
        // this is intended to be empty
        Deque<Integer> trial = new Deque<>();
        trial.addLast(1);
        trial.addFirst(2);
        trial.addFirst(3);
        trial.addFirst(4);
        trial.addLast(5);
        //for (Integer i : trial)
        //    System.out.println(i);
        Iterator<Integer> i = trial.iterator();
        while (i.hasNext())
            System.out.println(i.next());
    }
}
