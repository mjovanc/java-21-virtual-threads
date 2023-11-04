package com.mjovanc;

public class Consumer implements Runnable {
    private Buffer buffer;
    private String name;
    private int numTasksToConsume;

    public Consumer(Buffer buffer, String name, int numTasksToConsume) {
        this.buffer = buffer;
        this.name = name;
        this.numTasksToConsume = numTasksToConsume;
    }

    @Override
    public void run() {
        int tasksConsumed = 0;
        while (tasksConsumed < numTasksToConsume) {
            buffer.consume(name);
            tasksConsumed++;
        }
    }
}
