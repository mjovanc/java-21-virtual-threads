package com.mjovanc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PlatformThreads {
    public static void main(String[] args) {
        int numProducers = 5;
        int numConsumers = 5;
        int totalTasks = numProducers * 20000;

        BlockingQueue<Integer> taskQueue = new ArrayBlockingQueue<>(totalTasks);

        Thread[] producerThreads = new Thread[numProducers];
        Thread[] consumerThreads = new Thread[numConsumers];

        long startTime = System.nanoTime();

        for (int i = 0; i < numProducers; i++) {
            producerThreads[i] = new Thread(new Producer(taskQueue, "Producer " + (i + 1)));
            producerThreads[i].start();
        }

        for (int i = 0; i < numConsumers; i++) {
            consumerThreads[i] = new Thread(new Consumer(taskQueue, "Consumer " + (i + 1)));
            consumerThreads[i].start();
        }

        // Wait for producer threads to complete
        try {
            for (int i = 0; i < numProducers; i++) {
                producerThreads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Interrupt consumer threads to stop
        for (int i = 0; i < numConsumers; i++) {
            consumerThreads[i].interrupt();
        }

        // Wait for consumer threads to complete
        try {
            for (int i = 0; i < numConsumers; i++) {
                consumerThreads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();

        double elapsedTimeSeconds = (endTime - startTime) / 1e9;

        System.out.println("Performance Metrics:");
        System.out.println("Elapsed Time: " + elapsedTimeSeconds + " seconds");
        System.out.println("Total Tasks Processed: " + totalTasks);
        System.out.println("Throughput (Tasks/Second): " + totalTasks / elapsedTimeSeconds);
    }
}

class Producer implements Runnable {
    private BlockingQueue<Integer> taskQueue;
    private String name;

    public Producer(BlockingQueue<Integer> taskQueue, String name) {
        this.taskQueue = taskQueue;
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            try {
                taskQueue.put(i);
                System.out.println(name + " produced task " + i);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class Consumer implements Runnable {
    private BlockingQueue<Integer> taskQueue;
    private String name;

    public Consumer(BlockingQueue<Integer> taskQueue, String name) {
        this.taskQueue = taskQueue;
        this.name = name;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                int task = taskQueue.take();
                System.out.println(name + " consumed task " + task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}