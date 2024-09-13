import java.util.EmptyStackException;
//create array to store the elements of the stack
//size keeps track of the elements in the queue
public class ArrayStack<T> implements MyStack<T> {
    private T[] array;
    private int size;
    private int top;


    //constructor initializing the stack with a default size of 10
    public ArrayStack() {
        array = (T[]) new Object[10];
        size = 0;
        top = 0;
    }


    @Override
    //Pushes an item onto the top of the stack
    //If array is full, resize to double the current size
    //add items to the top of the stack and increment the size
    public void push(T item) {
        if (size == array.length) {
            T[] newArray = (T[]) new Object[array.length * 2];
            for (int i = 0; i < array.length; i++) {
                newArray[i] = array[i];
            }
            array = newArray;
        }
        array[top++] = item;
        size += 1;
    }

    @Override
    //pops an item from the top of the stack and returns it
    //if stack is empty, throw exception
    //retrieve the item from the top of the stack
    //null the reference to the popped item
    //return popped item
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty on pop");

        }
        T item =  (T) array[--top];
        array[top] = null;
        size -= 1;
        return item;
    }

    @Override
    //Returns the item at the top of the stack without removing it
    //if stack is empty, throw exception
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty om peek");
        }
        return  (T) array[top-1];

    }

    @Override
    //return the number of items in the stack
    public int size() {
    return size;

    }

    @Override
    //check if the stack is empty
    public boolean isEmpty() {
        return size == 0;
    }
}
