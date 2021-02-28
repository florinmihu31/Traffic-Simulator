package com.apd.tema2.factory;

import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.entities.ReaderHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.*;

import static com.apd.tema2.Main.*;

/**
 * Returneaza sub forma unor clase anonime implementari pentru metoda de citire din fisier.
 */
public class ReaderHandlerFactory {

    public static ReaderHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of them)
        // road in maintenance - 1 lane 2 ways, X cars at a time
        // road in maintenance - N lanes 2 ways, X cars at a time
        // railroad blockage for T seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) {
                    // Instantiere intersectie
                    intersection = IntersectionFactory.getIntersection(handlerType);
                }
            };
            case "simple_n_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    // To parse input line use:
                    // String[] line = br.readLine().split(" ");
                    String[] line = br.readLine().split(" ");

                    // Numarul de masini care pot intra maximum in giratoriu
                    roundaboutCars = Integer.parseInt(line[0]);

                    // Timpul petrecut in giratoriu de masini
                    roundaboutTime = Integer.parseInt(line[1]);

                    // Semafor folosit pentru a lasa in giratoriu maximum roundaboutCars masini
                    semaphore = new Semaphore(roundaboutCars);

                    // Instantiere intersectie
                    intersection = IntersectionFactory.getIntersection(handlerType);
                }
            };
            case "simple_strict_1_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");

                    // Numarul sensurilor
                    directionsNo = Integer.parseInt(line[0]);

                    // Timpul petrecut in giratoriu
                    roundaboutTime = Integer.parseInt(line[1]);

                    // Vector de semafoare pentru a permite o singura masina pe sens
                    semaphores = new Semaphore[directionsNo];

                    for (int i = 0; i < semaphores.length; i++) {
                        semaphores[i] = new Semaphore(1);
                    }

                    // Instantiere intersectie
                    intersection = IntersectionFactory.getIntersection(handlerType);
                }
            };
            case "simple_strict_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");

                    // Numarul sensurilor
                    directionsNo = Integer.parseInt(line[0]);

                    // Timpul petrecut in giratoriu
                    roundaboutTime = Integer.parseInt(line[1]);

                    // Numarul de masini maxim dintr-o directie
                    roundaboutCars = Integer.parseInt(line[2]);

                    // Vector de semafoare pentru fiecare sens
                    semaphores = new Semaphore[directionsNo];

                    for (int i = 0; i < semaphores.length; i++) {
                        semaphores[i] = new Semaphore(roundaboutCars);
                    }

                    barriers = new CyclicBarrier[2];

                    // Bariera pentru a astepta ca toate masinile sa ajunga la giratoriu
                    barriers[0] = new CyclicBarrier(carsNo);

                    // Bariera pentru selectia si iesirea masinilor de pe fiecare sens
                    barriers[1] = new CyclicBarrier(roundaboutCars * directionsNo);

                    // Instantiere intersectie
                    intersection = IntersectionFactory.getIntersection(handlerType);
                }
            };
            case "simple_max_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");

                    // Numarul sensurilor
                    directionsNo = Integer.parseInt(line[0]);

                    // Timpul petrecut in giratoriu
                    roundaboutTime = Integer.parseInt(line[1]);

                    // Numarul de masini maxim dintr-o directie
                    roundaboutCars = Integer.parseInt(line[2]);

                    // Vector de semafoare pentru fiecare sens
                    semaphores = new Semaphore[directionsNo];

                    for (int i = 0; i < semaphores.length; i++) {
                        semaphores[i] = new Semaphore(roundaboutCars);
                    }

                    // Instantiere intersectie
                    intersection = IntersectionFactory.getIntersection(handlerType);
                }
            };
            case "priority_intersection" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");

                    // Numarul de masini cu prioritate
                    highPriorityCarsNo = Integer.parseInt(line[0]);

                    // Numarul de masini fara prioritate
                    lowPriorityCarsNo = Integer.parseInt(line[1]);

                    // Vector de semafoare
                    semaphores = new Semaphore[2];

                    // Coada masinilor cu prioritate
                    highPriorityQueue = new ArrayBlockingQueue<>(highPriorityCarsNo);

                    // Coada masinilor fara prioritate
                    lowPriorityQueue = new ArrayBlockingQueue<>(lowPriorityCarsNo);

                    /*
                     * Un semafor pentru sincronizarea cozii de masini fara prioritate si alt
                     * semafor pentru sincronizarea masinilor in intersectie
                     */
                    for (int i = 0; i < semaphores.length; i++) {
                        semaphores[i] = new Semaphore(1);
                    }

                    // Instantiere intersectie
                    intersection = IntersectionFactory.getIntersection(handlerType);
                }
            };
            case "crosswalk" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");

                    // Timpul de executie
                    executeTime = Integer.parseInt(line[0]);

                    // Numarul maxim de pietoni
                    pedestriansNo = Integer.parseInt(line[1]);

                    // Instantiere pietoni
                    pedestrians = new Pedestrians(executeTime, pedestriansNo);

                    // Instantiere intersectie
                    intersection = IntersectionFactory.getIntersection(handlerType);
                }
            };
            case "simple_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");

                    // Numarul de masini pe o banda
                    carsOnLaneNo = Integer.parseInt(line[0]);
                    barriers = new CyclicBarrier[1];
                    // Bariera pentru asteptarea masinilor sa treaca de portiunea in lucru
                    barriers[0] = new CyclicBarrier(carsOnLaneNo);
                    semaphores = new Semaphore[2];

                    // Semafor initializat cu carsOnLaneNo pentru sensul care are voie sa treaca
                    semaphores[0] = new Semaphore(carsOnLaneNo);
                    // Semafor initializat cu 0 pentru sensul care nu are voie sa treaca
                    semaphores[1] = new Semaphore(0);

                    // Instantiere intersectie
                    intersection = IntersectionFactory.getIntersection(handlerType);
                }
            };
            case "complex_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");

                    // Numarul de sensuri circulabile
                    freeLanes = Integer.parseInt(line[0]);

                    // Numarul initial de benzi
                    initialLanes = Integer.parseInt(line[1]);

                    // Numarul de masini ce pot trece pe fiecare sens
                    passingCars = Integer.parseInt(line[2]);
                    semaphores = new Semaphore[initialLanes];
                    barriers = new CyclicBarrier[initialLanes + 1];
                    phaser = new Phaser(0);

                    // Vector cu liste pentru a mentine ordinea benzilor
                    newLanes = new ArrayList[freeLanes];

                    // Vector pentru a retine cozile de masini pentru fiecare banda
                    laneQueues = new ArrayBlockingQueue[initialLanes];

                    // Semafoare pentru sincronizare
                    semaphore = new Semaphore(1);
                    newLanesSemaphore = new Semaphore(1);
                    laneQueuesSemaphore = new Semaphore(1);

                    for (int i = 0; i < semaphores.length; i++) {
                        semaphores[i] = new Semaphore(passingCars);
                    }

                    // Bariera pentru a astepta sa ajunga toate masinile la portiunea in lucru
                    barriers[barriers.length - 1] = new CyclicBarrier(carsNo);

                    for (int i = 0; i < barriers.length - 1; i++) {
                        barriers[i] = new CyclicBarrier(passingCars);
                    }
                    
                    for (int i = 0; i < newLanes.length; i++) {
                        newLanes[i] = new ArrayList<>();
                    }

                    for (int i = 0; i < laneQueues.length; i++) {
                        laneQueues[i] = new ArrayBlockingQueue<>(carsNo);
                    }

                    int laneNo = 0;

                    for (int i = 0; i < freeLanes; i++) {
                        for (int j = 0; j < initialLanes / freeLanes; j++) {
                            newLanes[i].add(laneNo);
                            laneNo++;
                        }
                    }

                    while (laneNo < initialLanes) {
                        newLanes[freeLanes - 1].add(laneNo);
                        laneNo++;
                    }

                    // Instantiere intersectie
                    intersection = IntersectionFactory.getIntersection(handlerType);
                }
            };
            case "railroad" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    barriers = new CyclicBarrier[1];

                    // Bariera pentru asteptarea masinilor la calea ferata
                    barriers[0] = new CyclicBarrier(carsNo);

                    // Vector pentru cozile de masini de pe ambele sensuri
                    laneQueues = new BlockingQueue[2];

                    for (int i = 0; i < laneQueues.length; i++) {
                        laneQueues[i] = new ArrayBlockingQueue<>(carsNo);
                    }

                    // Instantiere intersectie
                    intersection = IntersectionFactory.getIntersection(handlerType);
                }
            };
            default -> null;
        };
    }

}
