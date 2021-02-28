package com.apd.tema2;

import com.apd.tema2.entities.Intersection;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.io.Reader;

import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;
import java.util.concurrent.Semaphore;

public class Main {
    public static Pedestrians pedestrians = null;
    public static Intersection intersection;
    public static int carsNo;

    // Task 2
    public static int roundaboutTime;
    public static int roundaboutCars;
    public static Semaphore semaphore;

    // Task 3
    public static int directionsNo;
    public static Semaphore[] semaphores;

    // Task 4
    public static CyclicBarrier[] barriers;

    // Task 6
    public static BlockingQueue<Integer> highPriorityQueue;
    public static BlockingQueue<Integer> lowPriorityQueue;
    public static int highPriorityCarsNo;
    public static int lowPriorityCarsNo;

    // Task 7
    public static int executeTime;
    public static long startTime;
    public static int pedestriansNo;

    // Task 8
    public static int carsOnLaneNo;

    // Task 9
    public static int freeLanes;
    public static int initialLanes;
    public static int passingCars;
    public static List<Integer>[] newLanes;
    public static Semaphore newLanesSemaphore;
    public static Semaphore laneQueuesSemaphore;
    public static Phaser phaser;

    // Task 10
    public static BlockingQueue<Integer>[] laneQueues;

    public static void main(String[] args) {
        Reader fileReader = Reader.getInstance(args[0]);
        Set<Thread> cars = fileReader.getCarsFromInput();

        for(Thread car : cars) {
            car.start();
        }

        if (pedestrians != null) {
            try {
                Thread p = new Thread(pedestrians);
                p.start();
                p.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(Thread car : cars) {
            try {
                car.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
