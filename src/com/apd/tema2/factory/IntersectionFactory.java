package com.apd.tema2.factory;

import com.apd.tema2.entities.Intersection;
import com.apd.tema2.intersections.*;

import java.util.HashMap;
import java.util.Map;

import static com.apd.tema2.Main.*;

/**
 * Prototype Factory: va puteti crea cate o instanta din fiecare tip de implementare de Intersection.
 */
public class IntersectionFactory {
    private static Map<String, Intersection> cache = new HashMap<>();

    static {
        // cache.put("name_intersection", new Intersection() {
        // });
        cache.put("simple_semaphore", new SimpleSemaphore());
        cache.put("simple_n_roundabout", new SimpleNRoundabout(roundaboutTime, roundaboutCars,
                semaphore));
        cache.put("simple_strict_1_car_roundabout", new SimpleStrict1CarRoundabout());
        cache.put("simple_strict_x_car_roundabout", new SimpleStrictXCarRoundabout());
        cache.put("simple_max_x_car_roundabout", new SimpleMaxXCarRoundabout());
        cache.put("priority_intersection", new PriorityIntersection());
        cache.put("crosswalk", new Crosswalk());
        cache.put("simple_maintenance", new SimpleMaintenance());
        cache.put("complex_maintenance", new ComplexMaintenance());
        cache.put("railroad", new Railroad());
    }

    public static Intersection getIntersection(String handlerType) {
        return cache.get(handlerType);
    }

}
