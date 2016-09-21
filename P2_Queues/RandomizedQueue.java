import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

import java.lang.NullPointerException;
import java.lang.UnsupportedOperationException;

/**
 * Randomized Queue Class using Resizing Array
 * Assignment 2
 * @author Hendrik Kits van Heyningen
 */
public class RandomizedQueue<Item> implements Iterable<Item> 
{
    private Item[] rq;            // queue elements
    private int N = 0;           // number of elements on queue
    private int first = 0;       // index of first element of queue
    private int last  = 0;       // index of next available slot
    
    
    /*
     * Construct an empty randomized Queue
     */
    @SuppressWarnings("unchecked")
    public RandomizedQueue() 
    {
        // cast needed since no generic array creation in Java
        rq = (Item[]) new Object[2]; //start array at size 2
    }
    
    // resize the underlying array
    @SuppressWarnings("unchecked") //problem with java that can't make an array of undetermined type
    private void resize(int max) 
    {
        assert max >= N;
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++) 
        {
            temp[i] = rq[(first + i) % rq.length]; //mod so that can take care of wrap-around
        }
        rq = temp;
        first = 0;
        last  = N;
    }
    
    /**
     * Is this queue empty?
     * @return true if this queue is empty; false otherwise
     */
    public boolean isEmpty() 
    {
        return N == 0;
    }
    
    /**
     * Returns the number of items in this queue.
     * @return the number of items in this queue
     */
    public int size() 
    {
        return N;
    }
    
    /**
     * Adds the item to this queue.
     * @param item the item to add
     * @throws java.lang.NullPointerException if the user attempts to add a null item
     */
    public void enqueue(Item item) 
    {
    	if (item == null)
    		throw new NullPointerException();
    	// double size of array if necessary and recopy to front of array
        if (N == rq.length) 
        	resize(2*rq.length);   // double size of array if necessary
        rq[last++] = item;         // add item and increment counter
        
        if (last == rq.length) 
        	last = 0;          // wrap-around if we've reached end of array
        N++;
    }
    
    /**
     * Removes and returns a random item on this queue
     * @return a random item on this queue that was least recently added
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    public Item dequeue()
    {	
    	if (isEmpty())
    		throw new NoSuchElementException("Queue Underflow");
    	
    	//the array will contain a block starting at first, and we pick a uniform random integer on this continuous block
    	//(We recall that we need %rq.length since the continuous block can wrap around)
    	int randomIndex = (StdRandom.uniform(N) + first)%rq.length; 
    	
    	Item item = rq[randomIndex]; //store item to return later
    	//We then swap first and index which enables us to stay with a continuous block of full array
        rq[randomIndex] = rq[first];
        
    	rq[first] = null;              
        N--;
        first++; //after swapping the first and index, removing it amounts to a standard FIFO dequeue so we simply must remove first
        if (first == rq.length) first = 0;      // wrap-around if necessary
        
    	//If the array is under 1/4 full we resize to avoid loitering and wasted extra memory
        if (N > 0 && N == rq.length/4) 
        	resize(rq.length/2); 

        return item;
    }
    
    /**
     * Returns a random item in the queue without deleting it
     * @return a random item on the queue
     */
    public Item sample()
    {
    	if (isEmpty())
    		throw new NoSuchElementException("Queue Underflow");
    	//the array will contain a block starting at first, and we pick a uniform random integer on this continuous block
    	//(We recall that we need %rq.length since the continuous block can wrap around)
    	int randomIndex = (StdRandom.uniform(N) + first)%rq.length;  
    	return rq[randomIndex]; //return this random
    }
    
    /**
     * Returns an iterator that iterates over the items in this queue in random order.
     * @return an iterator that iterates over the items in this queue in random order
     */
    @Override
	public Iterator<Item> iterator() 
    {
        return new ArrayIterator();
    }
    
    // an iterator, doesn't implement remove() since it's optional
    private class ArrayIterator implements Iterator<Item> 
    {
        private  RandomizedQueue<Item> dup; 
        //a duplicate queue of rq that we will dequeue from to generate a random permutation
        
        private ArrayIterator()
        {
        	dup = new RandomizedQueue<Item>();
        	for (int i = 0; i < rq.length; i++)
        	{
        		if (rq[i] != null)
        			dup.enqueue(rq[i]);
        	}
        }
        @Override
		public boolean hasNext()  
        { 
        	return (!dup.isEmpty());
        }
        
        @Override
		public void remove()      
        { 
            throw new UnsupportedOperationException();  
        }
        
        @Override
		public Item next() 
        {
            if (!hasNext()) 
                throw new NoSuchElementException();
            return dup.dequeue();
        }
    }
    
    /**
     * Main method to test
     * @param args
     */
    public static void main(String[]args)
    {
    	RandomizedQueue<Integer> randQ = new RandomizedQueue<Integer>();
    	System.out.println("Size of empty randomized queue = " + randQ.size());
        System.out.println("Is randQ empty? " + randQ.isEmpty());
        System.out.println("Testing enque method");
        for (int i=0; i<10; i++)
        {
            randQ.enqueue(i);
        }
        
        System.out.println("Size of randomized queue after adding 10 elements = " + randQ.size());
        
        System.out.println("Is randQ empty? " + randQ.isEmpty());
        
        System.out.println("Testing sample method");
        
        for (int i = 0; i < 10; i++)
        {
            System.out.println("Sample " + i + " = " + randQ.sample());
        }
        
        System.out.println("Testing iterator");
        for (int i : randQ)
        {
            System.out.println(" " + i);
        }
        
        System.out.println("Testing dequeue method");
        
        for (int i = 0; i < 10; i++)
        {
            System.out.println("Deque number " + i + " = " + randQ.dequeue());
            System.out.println("Size of randomized queue = " + randQ.size());
        }
    }

}

