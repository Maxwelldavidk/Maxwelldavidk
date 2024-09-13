//create nodes for the front and back of the list.
//size keeps track of the elements in the queue
public class LinkedQueue<E> implements MyQueue<E>{
    private ListNode<E> front;
    private ListNode<E> back;
    private int size = 0;


    @Override
    //Enqueue an item to the back of the queue
    //Create a newNode with the given item.
    //If the queue is empty, both front and back point to the new node.
    //If the queue is not empty, link the new node to the back of the queue and update the back pointer.
    //increment the size of the queue
    public void enqueue(E item) {
        ListNode<E> newNode = new ListNode<>(item);
        if (size == 0){
            front = newNode;
            back = newNode;
        } else {
            back.next = newNode;
            back = back.next;
        }
        size += 1;
    }

    @Override
    //Dequeue an item from the front of the queue and return it.
    //Check if the queue is empty and return exception if empty
    //retrieve data from the front node.
    //Move front pointer to the next node.
    //decrement size of the queue
    //return dequeued item
    public E dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty on dequeue");
        }
        E ret = (E) front.data;
        front = front.next;
        size -= 1;
        return ret;
    }

    @Override
    //Returns the item at the front of the queue without removing it
    //Check if the queue is empty and return exception if empty
    //Return the data from the front node
    public E peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty on peek");
        }
        return (E) front.data;
    }

    @Override
    //Returns the number of items in the queue
    public int size() {
        return size;
    }

    @Override
    //Returns true if the queue is empty otherwise returns false
    public boolean isEmpty() {
        return size == 0;
    }

    private static class ListNode<E>{
        private final E data;
        private ListNode<E> next;

        private ListNode(E data, ListNode<E> next){
            this.data = data;
            this.next = next;
        }

        private ListNode(E data){
            this.data = data;
        }
    }
}
