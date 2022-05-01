package com.example.proyecto;

import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private static final AtomicInteger count = new AtomicInteger(0);
    public final int id;
    public String userName;

    public User(String userName) {
        this.userName = userName;
        id = count.incrementAndGet();
    }
}
