package rustycage.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rustycage.Attribute;
import rustycage.util.Preconditions;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by breh on 10/17/16.
 */

public final class AttributesStack {

    private final Map<Class<?>,Stack<?>> mapOfStacks = new HashMap<>();

    public AttributesStack() {}



    public <T> void push(@NonNull Class<T> clazz, @NonNull Attribute<T> attribute) {
        Preconditions.assertNotNull(clazz,"clazz");
        Preconditions.assertNotNull(attribute,"attribute");
        getOrCreateStackForClass(clazz).push(attribute.getAttribute());
    }

    public <T> void push(@NonNull Class<T> clazz, @NonNull T attribute) {
        Preconditions.assertNotNull(clazz,"clazz");
        Preconditions.assertNotNull(attribute,"attribute");
        getOrCreateStackForClass(clazz).push(attribute);
    }

    public @Nullable <T> T pop(@NonNull Class<T> clazz) {
        Stack<T> stack = getStackForClass(clazz);
        if (stack != null && ! stack.empty() ) {
            return stack.pop();
        }  else {
            return null;
        }
    }

    public @Nullable <T> T get(@NonNull Class<T> clazz) {
        Stack<T> stack = getStackForClass(clazz);
        if (stack != null && ! stack.empty() ) {
            return stack.peek();
        }  else {
            return null;
        }
    }



    @SuppressWarnings("unchecked")
    private <T> Stack<T> getStackForClass(@NonNull Class<T> clazz) {
        return (Stack<T>)mapOfStacks.get(clazz);
    }

    @SuppressWarnings("unchecked")
    private <T> Stack<T> getOrCreateStackForClass(@NonNull Class<T> clazz) {
        Stack<T> stack = (Stack<T>)mapOfStacks.get(clazz);
        if (stack == null) {
            stack = new Stack<>();
            mapOfStacks.put(clazz,stack);
        }
        return stack;
    }



}
