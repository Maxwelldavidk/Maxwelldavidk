//Node for the front of the stack
//size keeps track of the number of elements in the stack
public class LinkedStack<T> implements MyStack<T> {
    private ListNode<T> top;
    private int size;

    //Constructor to initialize an empty stack
    public LinkedStack() {
        top = null;
        size = 0;
    }

    @Override
    //Push and item to the top of the stack
    //create a new node with the given item
    //link the new node to the top of the stack
    //update the top pointer
    //increment the size of the stack
    public void push(T item) {
        ListNode<T> newNode = new ListNode<>(item);
        newNode.next = top;
        top = newNode;
        size += 1;

    }

    @Override
    //pop a tem from the top of the stack and return it
    //check if the stack is empty and throw an exception if it is
    //retrieve the data from the top node
    //move the top pointer to the next node
    //decrement the size of the stack
    //return the popped item
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty on pop");
        }
        T item = top.data;
        top = top.next;
        size -=1;
        return item;
    }

    @Override
    //Returns the item at the top of the stack without removing it
    //check if the stack is empty and throw an exception if it is
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty on peek");
        }
        return top.data;
    }

    @Override
    //Return the number of items in the stack
    public int size() {
        return size;
    }

    @Override
    //returns true if the stack is empty, false otherwise
    public boolean isEmpty() {
        return size == 0;
    }

    private static class ListNode<T>{
        private final T data;
        private ListNode<T> next;

        private ListNode(T data, ListNode<T> next){
            this.data = data;
            this.next = next;
        }

        private ListNode(T data){
            this.data = data;
        }
    }
}
