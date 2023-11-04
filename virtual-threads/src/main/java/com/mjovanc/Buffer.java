package com.mjovanc;

import java.util.LinkedList;

public class Buffer {
    private LinkedList<String> data;
    private int capacity;

    public Buffer(int capacity) {
        this.data = new LinkedList<>();
        this.capacity = capacity;
    }

    public synchronized void produce(String task, String producerName) {
        while (data.size() >= capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        data.add(task);
        System.out.println(producerName + " produced " + task);
        notifyAll();
    }

    public synchronized String consume(String consumerName) {
        while (data.isEmpty()) {
            try {
                System.out.println(consumerName + " is waiting for tasks.");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        String task = data.removeFirst();
        System.out.println(consumerName + " consumed " + task);
        notifyAll();
        return task;
    }
}
