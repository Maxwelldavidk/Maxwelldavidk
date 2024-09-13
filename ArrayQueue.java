//create an array to store the elements of the queue
//index for the front element of the queue
//index for the back element of the queue
//number of elements in the queue
public class ArrayQueue<T> implements MyQueue<T>{
    private T[] queue;
    private int front;
    private int back;
    private int size;

    //Constructor to initialize the queue
    public ArrayQueue(){
        this(10);
    }

    //initialize the queue, front index, back index, and size.
    public ArrayQueue(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Queue size must be greater than 0 " + size);
        }
        queue = (T[]) new Object[size];
        front = 0;
        back = 0;
        size = 0;
    }
    //If the queue is full then resize.
    //add item to the back of the queue
    //update the back index in a circular style as in lecture
    //update the size of the queue
    public void enqueue (T item) {
        if (size == queue.length) {
            resize();
        }
        queue[back] = item;
        back = (back + 1) % queue.length;
        size += 1;
    }

    //removes and returns the item from the front of the queue
    //if queue is empty throw exception.
    //get the item at the front of the queue
    //update the front index in a circular style as in lecture
    //decrement the size of the queue
    //return the dequeued item
    public T dequeue () {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        T item = queue[front];
        front = (front + 1) % queue.length;
        size -= 1;
        return item;

    }
    //Return the item at the front of the queue without removing it
    //if queue is empty throw exception
    public T peek () {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return queue[front];
    }
    //check if the queue is empty
    public boolean isEmpty () {
        return size == 0;
    }
    // return the number of items in the queue
    public int size () {
        return size;
    }

    //Resize the current queue to double its size.
    //Create a new size of the queue
    //create a new array with new size
    //copy elements of the old queue to the new queue
    //update queue to point to the new queue
    //reset front index to zero
    //update back index to the size of the old queue
    private void resize () {

        int newSize = queue.length * 2;
        T[] newQueue = (T[]) new Object[newSize];
        for (int i = 0; i < size; i++) {
            newQueue[i] = queue[(front + i) % queue.length];
        }
            queue = newQueue;
            front = 0;
            back = size;

        }

    }



