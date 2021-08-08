package com.example.statsapp;

public interface Accumulator<T,E> {
    E accumulate(E acc, T nextItem);
}
