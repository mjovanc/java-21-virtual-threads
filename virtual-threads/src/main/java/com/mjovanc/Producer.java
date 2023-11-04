package com.mjovanc;

public class Producer implements Runnable {
    private Buffer buffer;
    private String name;
    private int numTasksToProduce;

    public Producer(Buffer buffer, String name, int numTasksToProduce) {
        this.buffer = buffer;
        this.name = name;
        this.numTasksToProduce = numTasksToProduce;
    }

    @Override
    public void run() {
        for (int i = 1; i <= numTasksToProduce; i++) {
            buffer.produce("task " + i, name);
        }
    }
}
