package net.mapana.cache.implement;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.mapana.cache.core.QueueCache;

/**
 *
 * @author Guido A. Cafiel Vellojin
 * @param <E>
 */
public abstract class MapanaCache<E> {

    protected boolean isRunning = false;

    /**
     * give to the component the queue name for do his work
     *
     * @return
     */
    public abstract String getQueueName();

    /**
     * Process the elements in queue. This method have a background thread, this
     * thread it process one element for milisecond, if not have element in
     * queue the component check the queue in loop of 10 seconds.
     *
     * @param element
     */
    public abstract void batchProcess(E element);

    /**
     * This method put one element to the end of the queue.
     *
     * @param element
     */
    public void put(E element) {
        QueueCache.getInstance().put(getQueueName(), element);
        if (!isRunning) {
            isRunning = true;
            doInBackground();
        }
    }

    /**
     * This method return the element most old in the queue. The queue is FIFO
     * patern.
     *
     * @return
     */
    public E get() {
        return (E) QueueCache.getInstance().get(getQueueName());
    }

    /**
     * This method run the background thread for process in batch.
     */
    protected void doInBackground() {
        BatchMethod batchMethod = new BatchMethod();
        Thread t = new Thread(batchMethod);
        t.start();
    }

    class BatchMethod implements Runnable {

        int i = 1;

        @Override
        public void run() {
            while (true) {
                E element = get();
                if (element == null) {
                    if (i <= 10000) {
                        i++;
                    }
                } else {
                    try {
                        batchProcess(element);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i = 1;
                }
                try {
                    Thread.sleep(i);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MapanaCache.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

}
