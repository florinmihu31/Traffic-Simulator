package com.apd.tema2.factory;

import com.apd.tema2.entities.*;
import com.apd.tema2.utils.Constants;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Phaser;

import static com.apd.tema2.Main.*;
import static com.apd.tema2.utils.Constants.*;
import static java.lang.Thread.sleep;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {

    public static IntersectionHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of
        // them)
        // road in maintenance - 2 ways 1 lane each, X cars at a time
        // road in maintenance - 1 way, M out of N lanes are blocked, X cars at a time
        // railroad blockage for s seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    System.out.println("Car " + car.getId() + " has reached the semaphore, now " +
                            "waiting...");

                    // Fiecare masina asteapta un timp prestabilit
                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has waited enough, now driving...");
                }
            };
            case "simple_n_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    System.out.println("Car " + car.getId() +" has reached the roundabout, now " +
                            "waiting...");

                    /*
                     * Daca o masina intra in giratoriu decrementeaza semaforul, afiseaza mesajul
                     * si sta in sleep roundabout milisecunde
                     */
                    try {
                        semaphore.acquire();
                        System.out.println("Car " + car.getId() + " has entered the roundabout");
                        sleep(roundaboutTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() +" has exited the roundabout after " +
                            (roundaboutTime / 1000) + " seconds");

                    // La final masina elibereaza semaforul
                    semaphore.release();
                }
            };
            case "simple_strict_1_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    System.out.println("Car " + car.getId() + " has reached the roundabout");

                    // Fiecare masina incearca sa decrementeze valoare semaforului de pe banda sa
                    try {
                        semaphores[car.getStartDirection()].acquire();
                        System.out.println("Car " + car.getId() + " has entered the roundabout from " +
                                "lane " + car.getStartDirection());
                        sleep(roundaboutTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has exited the roundabout after " +
                            (roundaboutTime / 1000) + " seconds");

                    // La final masina elibereaza semaforul
                    semaphores[car.getStartDirection()].release();
                }
            };
            case "simple_strict_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");

                    // Toate masinile asteapta la bariera
                    try {
                        barriers[0].await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    try {
                        /*
                         * Masinile de pe fiecare sens incearca sa treaca, se selecteaza masinile
                         * de pe fiecare sens
                         */
                        semaphores[car.getStartDirection()].acquire();
                        System.out.println("Car " + car.getId() + " was selected to enter the " +
                                "roundabout from lane " + car.getStartDirection());

                        // Masinile selectate asteapta la bariera intrarea in giratoriu
                        barriers[1].await();

                        System.out.println("Car " + car.getId() + " has entered the roundabout from " +
                                "lane " + car.getStartDirection());
                        sleep(roundaboutTime);
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has exited the roundabout after " +
                            (roundaboutTime / 1000) + " seconds");

                    // Se asteapta ca toate masinile sa iasa din giratoriu cu ajutorul barierei
                    try {
                        barriers[1].await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    // Se incrementeaza semaforul corespunzator directiei
                    semaphores[car.getStartDirection()].release();
                }
            };
            case "simple_max_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    // Continuati de aici
                    System.out.println("Car " + car.getId() + " has reached the roundabout from " +
                            "lane " + car.getStartDirection());

                    // Fiecare masina incearca sa decrementeze valoarea semaforului benzii sale
                    try {
                        semaphores[car.getStartDirection()].acquire();

                        System.out.println("Car " + car.getId() + " has entered the roundabout from " +
                                "lane " + car.getStartDirection());
                        sleep(roundaboutTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has exited the roundabout after " +
                            (roundaboutTime / 1000) + " seconds");

                    // Fiecare masina elibereaza semaforul
                    semaphores[car.getStartDirection()].release();
                }
            };
            case "priority_intersection" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    // Continuati de aici

                    // Daca masina are prioritate
                    if (car.getPriority() > 1) {
                        /*
                         * Daca coada cu masini cu prioritate este goala, se blocheaza accesul in
                         * intersectie cu ajutorul unui semafor
                         */
                        if (highPriorityQueue.size() == 0) {
                            try {
                                semaphores[0].acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        // Adaugam masina in coada
                        try {
                            highPriorityQueue.put(car.getId());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Car " + car.getId() + " with high priority has entered the" +
                                " intersection");

                        try {
                            sleep(PRIORITY_INTERSECTION_PASSING);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Car " + car.getId() + " with high priority has exited" +
                                " the intersection");

                        // Scoatem masina din coada
                        try {
                            highPriorityQueue.take();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Deblocam intersectia daca nu mai sunt masini cu prioritate
                        if (highPriorityQueue.size() == 0) {
                            semaphores[0].release();
                        }
                    } else {
                        // Adaugam masina in coada
                        try {
                            lowPriorityQueue.put(car.getId());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Car " +  car.getId() + " with low priority is trying to " +
                                "enter the intersection...");

                        /*
                         * Masina incearca sa intre in intersectie accesand semaforul, insa daca
                         * aceasta nu este goala si nu este prima masina din coada de masini fara
                         * prioritate aceasta nu poate intra
                         */
                        do {
                            try {
                                semaphores[0].acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } while (highPriorityQueue.size() > 0 && car.getId() != lowPriorityQueue.peek());

                        // Masina fara prioritate intra in intersectia decrementand un semafor
                        try {
                            semaphores[1].acquire();
                            System.out.println("Car " + car.getId() + " with low priority has entered " +
                                    "the intersection");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Se scoate masina din coada
                        try {
                            lowPriorityQueue.take();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Se elibereaza semafoarele
                        semaphores[1].release();
                        semaphores[0].release();
                    }
                }
            };
            case "crosswalk" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    String message = null; // Mesajul original
                    String newMessage = null;// Mesajul nou

                    // Cat timp nu au trecut toti pietonii
                    while (!pedestrians.isFinished()) {
                        // Daca nu trec pietoni, semaforul este verde, altfel este rosu
                        if (!pedestrians.isPass()) {
                            newMessage = "Car " + car.getId() + " has now green light";
                        } else {
                            newMessage = "Car " + car.getId() + " has now red light";
                        }

                        // Daca mesajul nou difera de cel original, atunci se afiseaza
                        if (!newMessage.equals(message)) {
                            System.out.println(newMessage);
                            message = newMessage;
                        }
                    }

                    newMessage = "Car " + car.getId() + " has now green light";

                    if (!newMessage.equals(message)) {
                        System.out.println(newMessage);
                    }
                }
            };
            case "simple_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() +
                            " has reached the bottleneck");

                    // Daca sensul este 0
                    if (car.getStartDirection() == 0) {
                        // Masina incearca sa intre pe portiunea in reparatie, accesand semaforul
                        try {
                            semaphores[0].acquire();
                            System.out.println("Car " + car.getId() + " from side number " +
                                    car.getStartDirection() + " has passed the bottleneck");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Bariera pentru asteptarea masinilor care au trecut de portiune
                        try {
                            barriers[0].await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }

                        // Incrementarea semaforului celuilalt sens
                        semaphores[1].release();
                    } else {
                        // Masina incearca sa intre pe portiunea in reparatie, accesand semaforul
                        try {
                            semaphores[1].acquire();
                            System.out.println("Car " + car.getId() + " from side number " +
                                    car.getStartDirection() + " has passed the bottleneck");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Bariera pentru asteptarea masinilor care au trecut de portiune
                        try {
                            barriers[0].await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }

                        // Incrementarea semaforului celuilalt sens
                        semaphores[0].release();
                    }
                }
            };
            case "complex_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    try {
                        laneQueues[car.getStartDirection()].put(car.getId());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has come from the lane number " +
                            car.getStartDirection());

                    try {
                        barriers[barriers.length - 1].await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    int crtLane = car.getStartDirection() / passingCars;

                    if (crtLane > newLanes.length - 1) {
                        crtLane = newLanes.length - 1;
                    }

                    while (car.getStartDirection() != newLanes[crtLane].get(0) && car.getId() != laneQueues[car.getStartDirection()].peek()) {
                        //System.err.println("1 " + car.getStartDirection() + " " + newLanes[crtLane].get(0));
                    }

                    //while (car.getId() != laneQueues[car.getStartDirection()].peek()) {
                        //System.err.println("2 " + car.getId() + " " + laneQueues[car.getStartDirection()].peek());
                    //}

                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        semaphores[car.getStartDirection()].acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    phaser.register();

                    System.out.println("Car " + car.getId() + " from the lane " + car.getStartDirection() +
                            " has entered lane number " + crtLane);

                    phaser.awaitAdvance(1);

                    //if (laneQueues[car.getStartDirection()].size() > 0) {
                      //  try {
                        //} catch (InterruptedException e) {
                          //  e.printStackTrace();
                        //}
                    //}

                    laneQueues[car.getStartDirection()].poll();

                    if (laneQueues[car.getStartDirection()].size() == 0) {
                        System.out.println("The initial lane " + car.getStartDirection() + " has been emptied and " +
                                "removed from the new lane queue");
                        newLanes[crtLane].remove(0);
                    } else {
                        if (semaphores[car.getStartDirection()].availablePermits() == 0) {
                            System.out.println("The initial lane " + car.getStartDirection() + " has no permits and is " +
                                    "moved to the back of the new lane queue");
                            newLanes[crtLane].remove(0);
                            newLanes[crtLane].add(car.getStartDirection());
                        }
                    }

                    semaphore.release();

                    phaser.arriveAndAwaitAdvance();
                    phaser.arriveAndDeregister();

                    semaphores[car.getStartDirection()].release();
                    /*try {
                        laneQueues[car.getStartDirection()].put(car.getId());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has come from the lane number " +
                            car.getStartDirection());

                    try {
                        barriers[0].await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    int crtLane = car.getStartDirection() / passingCars;

                    if (crtLane > newLanes.length - 1) {
                        crtLane = newLanes.length - 1;
                    }

                    while (car.getStartDirection() != newLanes[crtLane].get(0)) {
                        try {
                            newLanesSemaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    while (car.getId() != laneQueues[car.getStartDirection()].peek()) {
                        try {
                            laneQueuesSemaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        semaphores[car.getStartDirection()].acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " from the lane " + car.getStartDirection() +
                            " has entered lane number " + crtLane);

                    if (laneQueues[car.getStartDirection()].size() > 0) {
                        try {
                            laneQueues[car.getStartDirection()].take();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (semaphores[car.getStartDirection()].availablePermits() == 0) {
                        if (laneQueues[car.getStartDirection()].size() == 0) {
                            System.out.println("The initial lane " + car.getStartDirection() + " has been emptied and " +
                                    "removed from the new lane queue");
                            newLanes[crtLane].remove(0);
                        } else {
                            System.out.println("The initial lane " + car.getStartDirection() + " has no permits and is " +
                                    "moved to the back of the new lane queue");
                            newLanes[crtLane].remove(0);
                            newLanes[crtLane].add(car.getStartDirection());
                        }
                    }

                    semaphore.release();
                    laneQueuesSemaphore.release();
                    newLanesSemaphore.release();
                    semaphores[car.getStartDirection()].release();*/
                }
            };
            case "railroad" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() +
                            " has stopped by the railroad");

                    // Daca masinile vin de pe sensul 0
                    if (car.getStartDirection() == 0) {
                        // Masina este adaugata in coada sensului 0
                        try {
                            laneQueues[0].put(car.getId());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Masinile se asteapta la bariera
                        try {
                            barriers[0].await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }

                        // Un singur thread afiseaza ca trenul trece
                        if (car.getId() == 0) {
                            System.out.println("The train has passed, cars can now proceed");
                        }

                        // Masinile asteapta la bariera dupa afisarea mesajului
                        try {
                            barriers[0].await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }

                        // Cat timp masina nu este prima in coada, aceasta nu poate trece
                        while (car.getId() != laneQueues[0].peek()) {
                        }

                        System.out.println("Car " + car.getId() +" from side number " + car.getStartDirection() +
                                " has started driving");

                        // Scoaterea masinii din coada
                        try {
                            laneQueues[0].take();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Masina este adaugata in coada sensului 1
                        try {
                            laneQueues[1].put(car.getId());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Masinile se asteapta la bariera
                        try {
                            barriers[0].await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }

                        // Un singur thread afiseaza ca trenul trece
                        if (car.getId() == 0) {
                            System.out.println("The train has passed, cars can now proceed");
                        }

                        // Masinile asteapta la bariera dupa afisarea mesajului
                        try {
                            barriers[0].await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }

                        // Cat timp masina nu este prima in coada, aceasta nu poate trece
                        while (car.getId() != laneQueues[1].peek()) {
                        }

                        System.out.println("Car " + car.getId() +" from side number " + car.getStartDirection() +
                                " has started driving");

                        // Scoaterea masinii din coada
                        try {
                            laneQueues[1].take();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            default -> null;
        };
    }
}
