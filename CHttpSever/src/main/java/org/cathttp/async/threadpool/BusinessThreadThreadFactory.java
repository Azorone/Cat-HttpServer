package org.cathttp.async.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class BusinessThreadThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(0);



    @Override
    public Thread newThread(Runnable r) {

        Thread thread = new Thread(r);
        thread.setName("businessThread["+poolNumber.getAndIncrement()+"]:");
        return thread;
    }
}
