/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mapana.cache.core;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public class QueueCache {

    private static QueueCache instance = null;
    private static LinkedHashMap<String, LinkedList<Object>> queuePool = null;

    private QueueCache() {
    }

    /**
     * Return a instance from queue cache
     *
     * @return
     */
    public static QueueCache getInstance() {
        if (instance == null) {
            instance = new QueueCache();
            queuePool = new LinkedHashMap<>();
        }
        return instance;
    }

    /**
     * Put one element in the queue given as parameter
     *
     * @param queue
     * @param ocject
     */
    public synchronized void put(String queue, Object ocject) {
        if (!queuePool.containsKey(queue)) {
            LinkedList<Object> queuedObjects = new LinkedList<>();
            queuedObjects.add(ocject);
            queuePool.put(queue, queuedObjects);
            return;
        }
        queuePool.get(queue).add(ocject);
    }

    /**
     * Return the first element in queue, this method is FIFO.
     *
     * @param queue
     * @return
     */
    public synchronized Object get(String queue) {
        if (!queuePool.containsKey(queue)) {
            return null;
        }
        if (queuePool.get(queue).isEmpty()) {
            return null;
        }
        Object object = queuePool.get(queue).getFirst();
        queuePool.get(queue).remove(object);
        return object;
    }

}
