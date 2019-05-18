package edu.uci.ics.jkotha.service.api_gateway.threadpool;

import edu.uci.ics.jkotha.service.api_gateway.logger.ServiceLogger;
import org.glassfish.jersey.internal.util.ExceptionUtils;


public class ThreadPool {
    private int numWorkers;
    private Worker[] workers;
    private ClientRequestQueue queue;

    public ThreadPool(int numWorkers) {
        this.numWorkers=numWorkers;
        //create workers
        workers = new Worker[numWorkers];
        for (int i=0;i<numWorkers;i++){
            workers[i] = Worker.CreateWorker(i,this);
            workers[i].start();
        }
        //create queue
        queue = new ClientRequestQueue();
    }

    public void add(ClientRequest clientRequest) {
            queue.enqueue(clientRequest);
    }

    public ClientRequest remove() {
        try {
            return queue.dequeue();
        }catch (InterruptedException e){
            ServiceLogger.LOGGER.warning(ExceptionUtils.exceptionStackTraceAsString(e));
            return remove();
        }
    }

    public ClientRequestQueue getQueue() {
        return queue;
    }
}
