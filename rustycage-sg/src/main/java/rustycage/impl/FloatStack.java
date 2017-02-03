package rustycage.impl;

/**
 * Created by breh on 2/2/17.
 */
public final class FloatStack {

    private static final int INITIAL_SIZE = 128;
    private float[] stack = new float[INITIAL_SIZE];
    private int currentSize = 0;

    public FloatStack() {
    }

    public void push(float value) {
        if (currentSize >= stack.length) {
            // need to reallocate the stack
            float[] newStack = new float[stack.length * 2];
            System.arraycopy(stack,0,newStack,0,stack.length);
            stack = newStack;
        }
        stack[currentSize] = value;
        currentSize++;
    }

    public float pop() {
        if (currentSize > 0) {
            currentSize--;
            return stack[currentSize];
        } else {
            throw new StackOverflowError("Stack underflow");
        }
    }

    public float peek() {
        if (currentSize > 0) {
            return stack[currentSize - 1];
        } else {
            throw new StackOverflowError("Stack underflow");
        }
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

}
