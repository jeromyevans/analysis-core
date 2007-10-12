package com.blueskyminds.framework.analysis;

import com.blueskyminds.framework.test.BaseTestCase;

import java.util.concurrent.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

/**
 * Methods to get familiar with java.concurrency
 *
 * Date Started: 25/08/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestConcurrency extends BaseTestCase {

    public TestConcurrency(String string) {
        super(string);
    }

    private ExecutorService executorService;
    private BlockingQueue<Runnable> workQueue;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestConcurrency with default attributes
     */
    private void init() {
    }

    private class WorkTask implements Callable<Integer> {
        private int taskNo;

        public WorkTask(int taskNo) {
            this.taskNo = taskNo;
        }

        public Integer call() throws Exception {
            int value = 0;
            System.out.println("Starting: "+taskNo);
            for (int i = 0; i < 50000000; i++) {
                value++;
            }
            return taskNo;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    private static final int CORE_POOL_SIZE     = 5;
    private static final int MAXIMUM_POOL_SIZE  = 20;
    private static final int KEEP_ALIVE_TIME    = 180;

    /** Sets up a thread pool with a limited size and demonstrates how the worker threads are resused */
    public void testConcurrency() {
        workQueue = new LinkedBlockingQueue<Runnable>();
        executorService = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);

        int value;

        Collection<Callable<Integer>> work = new ArrayList<Callable<Integer>>();
        for (int i = 0; i < 100; i++) {
            work.add(new WorkTask(i));
        }

        try {
            List<Future<Integer>> results = executorService.invokeAll(work);

            for (Future<Integer> task : results) {
                value = task.get();
                System.out.println(value);
            }
        } catch (InterruptedException e) {
            fail();
        } catch(ExecutionException e) {
            fail();
        }
    }
}
