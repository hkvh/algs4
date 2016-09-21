import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.NullPointerException;
import java.lang.UnsupportedOperationException;

/**
 * Deque Class using Doubly Linked List
 * Assignment 2
 * @author Hendrik Kits van Heyningen
 */
public class Deque<Item> implements Iterable<Item>
{
    private Node<Item> first;     //keep track of first item
    private Node<Item> last;  //keep track of last item
    private int N;   //size of Deque
    
    //helper node class, made static to save memory
    private static class Node<Item>
    {
        Node<Item> next; //next node
        Node<Item> previous; //previous node
        Item item; //item associated with the node
    }
    
    /**
     * Construct an empty Deque
     */
    public Deque()
    {
        first = null;
        last = null;
        N = 0;
    }
    
    /**
     * Check if Deque is empty
     * @return true if Deque is empty, false if not
     */
    public boolean isEmpty()
    {
        return N == 0;
    }
    
    /**
     * Get the size of Deque
     * @return the number of items on the Deque
     */
    public int size()
    {
        return N;
    }
    
    /**
     * Insert the item at the front
     * @param item the item to insert
     */
    public void addFirst(Item item)
    {
        if (item == null) 
            throw new NullPointerException("Can't add a null item");
        Node<Item> oldfirst = first;
        first = new Node<Item>();
        first.item = item;
        
        first.next = oldfirst;
        if (first.next != null)
            oldfirst.previous = first; //make the oldfirst's previous be this one
        
        first.previous = null;
        N++;
        
        //if this is the first thing added, last and first are the same
        if (size() == 1)
            last = first;
    }
    
    /**
     * Insert the item at the back
     * @param item the item to insert
     */
    public void addLast(Item item)
    {
        if (item == null) 
            throw new NullPointerException("Can't add a null item");
        Node<Item> oldlast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        last.previous = oldlast;
        if (last.previous != null)
            oldlast.next = last; //make the oldlast point to this one
        N++;
        
        //if this is the first thing added, last and first are the same
        if (size() == 1)
            first = last;
    }
    
    /**
     * Delete and return the first item
     * @return the Item at the front
     */
    public Item removeFirst()
    {
        if (isEmpty()) 
            throw new NoSuchElementException("Stack underflow");
        Item item = first.item;        // save item to return
        first = first.next;            // delete first node
        if (first != null)
            first.previous = null;
        N--;
        if (size() == 0) //if our list is empty first and last should both be null
            last = null; //first was already set to null but last needs to be too
        return item;
        
    }
    
    /**
     * Delete and return the last item
     * @return the Item at the back
     */
    public Item removeLast()
    {
        if (isEmpty()) 
            throw new NoSuchElementException("Stack underflow");
        Item item = last.item;        // save item to return
        last = last.previous;         // delete last node
        if (last != null)
            last.next = null;
        N--; 
        if (size() == 0) //if our list is empty first and last should both be null
            first = null; //last was already set to null but first needs to be too
        return item;
    }
    
    /**
     * Gets the iterator, a required method for implementing iterable
     * @return Iterator of items to iterate through the Deque
     */
    @Override
	public Iterator<Item> iterator()
    {
        return new DequeIterator<Item>(first);
    }
    
    //A Deque iterator, doesn't implement remove() since it's optional
    private class DequeIterator<T> implements Iterator<T> 
    {
        private Node<T> current; //the current node we are about to read
        
        //Constructor
        public DequeIterator(Node<T> first) 
        {
            current = first;
        }
        
        //Checks if we have a next available node
        @Override
		public boolean hasNext()  
        { 
            return current != null;                     
        }
        
        //Doesn't support remove
        @Override
		public void remove()      
        { 
            throw new UnsupportedOperationException();  
        }
        
        //Gets the current node if we can and moves the iterator forward
        @Override
		public T next() 
        {
            if (!hasNext()) 
                throw new NoSuchElementException();
            T item = current.item;
            current = current.next; 
            return item;
        }
    }
    
    /**
     * Main method for testing
     * @param args
     */
    public static void main(String[] args)
    {
        
        Deque<String> deque = new Deque<String>();
        System.out.println("Size of empty deque = " + deque.size());
        System.out.println("Testing addFirst()");
        deque.addFirst("Hello");
        deque.addFirst("World!");
        System.out.println("After adding 'Hello' and 'World!', size of deque = " + deque.size());
        System.out.println("removeLast returns " + deque.removeLast());
        System.out.println("removeFirst returns " + deque.removeFirst());
        System.out.println("Testing addLast()");
        deque.addLast("my");
        deque.addLast("fair");
        deque.addLast("lady.");
        System.out.println("After adding 'my', 'fair', and 'lady.', size of deque = " + deque.size());
        System.out.println("removeFirst returns " + deque.removeFirst());
        System.out.println("removeLast returns " + deque.removeLast());
    }
}