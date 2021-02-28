package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.Semaphore;

public class SimpleNRoundabout implements Intersection {
    private int roundaboutTime;
    private int roundaboutCars;
    private Semaphore semaphore;

    public SimpleNRoundabout(int roundaboutTime, int roundaboutCars, Semaphore semaphore) {
        this.roundaboutTime = roundaboutTime;
        this.roundaboutCars = roundaboutCars;
        this.semaphore = semaphore;
    }
}
