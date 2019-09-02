package net.mapana.cache.implement;

import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Guido A. Cafiel Vellojin
 * @param <E>
 */
public abstract class MapanaMultiThreadCache<E> extends GalileoCache<E> {

    protected abstract int getThreadsQty();

    @Override
    protected void doInBackground() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(getThreadsQty());
        ExecutorService executorService = MoreExecutors.getExitingExecutorService(executor, 100, TimeUnit.MILLISECONDS);
        executorService.submit(() -> {
            int i = 1;
            while (true) {
                E element = get();
                if (element == null) {
                    if (i <= 10000) {
                        i++;
                    }
                } else {
                    batchProcess(element);
                    i = 1;
                }
                try {
                    Thread.sleep(i);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GalileoCache.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
