package com.example.statsapp;


import androidx.core.util.Supplier;

import java.util.ArrayList;


public class Helpers {
    public static <T,E> T[] map(E[] arr, Callback<T,E> callback, Supplier<T[]> supplier){
        ArrayList<T> output = new ArrayList<>(arr.length);
        for (E item : arr){
            output.add(callback.activate(item));
        }
        return output.toArray(supplier.get());

    }
    public static <T> T[] filter(T[] arr, Callback<Boolean,T> predicate, Supplier<T[]> supplier){
        ArrayList<T> output = new ArrayList<>(arr.length);
        for (T item : arr){
            if (predicate.activate(item)){
                output.add(item);
            }
        }
        return output.toArray(supplier.get());
    }
    public static <T,E> T reduce(E[] arr, Accumulator<E,T> accumulator, T init){
        T curr = init;
        for (E item : arr){
            curr = accumulator.accumulate(curr,item);
        }
        return curr;
    }

    public static boolean[] toPrimitiveBoolean(Boolean[] arr){
        boolean[] output = new boolean[arr.length];
        for (int i = 0; i < arr.length; i++){
            output[i] = arr[i];
        }
        return output;
    }

    public static double calculatePercent(int dividend, int divisor){
        return (divisor == 0) ? 0 : ((double)dividend * 100.0) / ((double) divisor);
    }
}
