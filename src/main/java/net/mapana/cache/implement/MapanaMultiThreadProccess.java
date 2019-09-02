package net.mapana.cache.implement;

import com.google.common.util.concurrent.MoreExecutors;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
public abstract class MapanaMultiThreadProccess<I, O> {

    private LinkedList<I> queue = new LinkedList<>();
    private LinkedList<O> output = new LinkedList<>();
    private Map<String, Object> params = new LinkedHashMap<>();

    protected abstract int getThreadsQty();

    public void put(I inputElement) {
        queue.add(inputElement);
    }

    private synchronized I get() {
        if (queue.isEmpty()) {
            return null;
        }
        I inputElement = queue.getFirst();
        queue.remove(inputElement);
        return inputElement;
    }

    public void putParam(String paramName, Object paramValue) {
        params.put(paramName, paramValue);
    }

    public Object getParam(String paramName) {
        if (params.containsKey(paramName)) {
            return params.get(paramName);
        }
        return null;
    }

    private synchronized void putOutput(O outputElement) {
        output.add(outputElement);
    }

    public LinkedList<O> getProccessResult() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(getThreadsQty());
        ExecutorService executorService = MoreExecutors.getExitingExecutorService(executor, 100, TimeUnit.MILLISECONDS);
        executorService.submit(() -> {
            int i = 1;
            boolean running = true;
            while (running) {
                I element = get();
                if (element == null) {
                    running = false;
                    executorService.shutdown();
                } else {
                    O out = batchProcess(element);
                    if (out != null) {
                        putOutput(out);
                    }
                    i = 1;
                }
                try {
                    Thread.sleep(i);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MapanaMultiThreadProccess.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        while (!executor.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(MapanaMultiThreadProccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return output;
    }

    public void close() {
        queue = new LinkedList<>();
        output = new LinkedList<>();
    }

    protected abstract O batchProcess(I element);
}
