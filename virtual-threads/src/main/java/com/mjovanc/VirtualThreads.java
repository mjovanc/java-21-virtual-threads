package com.mjovanc;

public class VirtualThreads {
    public static void main(String[] args) {
        int numProducers = 1_000;
        int numConsumers = 1_000;
        int totalTasks = numProducers * 100;

        Buffer buffer = new Buffer(totalTasks);

        long startTime = System.nanoTime();

        Thread[] producerThreads = new Thread[numProducers];
        Thread[] consumerThreads = new Thread[numConsumers];

        for (int i = 0; i < numProducers; i++) {
            Runnable producerTask = new Producer(buffer, "Producer " + (i + 1), totalTasks / numProducers);
            producerThreads[i] = Thread.ofVirtual().start(producerTask);
        }

        for (int i = 0; i < numConsumers; i++) {
            Runnable consumerTask = new Consumer(buffer, "Consumer " + (i + 1), totalTasks / numConsumers);
            consumerThreads[i] = Thread.ofVirtual().start(consumerTask);
        }

        try {
            for (int i = 0; i < numProducers; i++) {
                producerThreads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < numConsumers; i++) {
            consumerThreads[i].interrupt();
        }

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




