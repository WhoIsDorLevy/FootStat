package com.example.statsapp;

public interface Callback<T,E> {
    T activate(E arg);
}
