package com.blueskyminds.analysis.core.statistics;

import junit.framework.TestCase;
import com.blueskyminds.analysis.core.series.UnivariateSeries;
import com.blueskyminds.analysis.core.series.AggregateSeries;
import com.blueskyminds.analysis.core.engine.ComputedResult;
import com.blueskyminds.analysis.core.engine.ComputeEngine;
import com.blueskyminds.homebyfive.framework.core.tools.DebugTools;

import java.math.MathContext;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.lang.management.MemoryMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.concurrent.Future;
import java.util.List;
import java.util.LinkedList;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Runs some heavy calculations through the statistics engine
 *
 * Date Started: 11/11/2007
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class StatisticsEngineTest extends TestCase {

    private static final Log LOG = LogFactory.getLog(StatisticsEngineTest.class);

    // ------------------------------------------------------------------------------------------------------

    private UnivariateSeries generateRandomSeries(int size) {
        UnivariateSeries series = new UnivariateSeries(null);
        MathContext mc = new MathContext(18, RoundingMode.HALF_UP);
        int d;
        RandomData r = new RandomDataImpl();
        for (int i = 0; i < size; i++) {
            d = ((Double) r.nextGaussian(1000.0, 50.0)).intValue();
            series.add(new BigDecimal(d, mc));
        }
        return series;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns true if there's not much heap left */
    private boolean almostOutOfHeap() {
        Runtime.getRuntime().gc();

        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        MemoryUsage memUsage = mem.getHeapMemoryUsage();
        Long used = memUsage.getUsed()/1000;
        Long max= memUsage.getMax()/1000;
        double percent = used.doubleValue()/max*100.0;
        return (percent > 80.0);
    }

    private static final int SIZE = 100000;

    /**
     * Feed random series into the compute engine
     **/
    private List<Future<ComputedResult>> feedEngine(ComputeEngine engine, int maxCount) {
        UnivariateSeries series;
        List<Future<ComputedResult>> futureResults = new LinkedList<Future<ComputedResult>>();

        LOG.info("Submitting up to "+maxCount+" series to the engine...");
        int submitted = 0;
        while ((submitted < maxCount) && (!almostOutOfHeap())) {
            series = generateRandomSeries(SIZE);
            futureResults.add(engine.compute(series));
            DebugTools.printAvailableHeap();
            submitted++;
        }

        return futureResults;
    }

    /**
     * Runs a serious amount of data into the StatisticsEngine
     *
     *  Feeds as much data as the heap allows into the engine and then waits for the results to be computed
     */
    public void testStatisticsEngine() {
        int count = 200;
        int loops = 3;
        int currentLoop = 0;
        StatisticsEngine engine = new StatisticsEngine();

        for (currentLoop = 0; currentLoop < loops; currentLoop++) {
            List<Future<ComputedResult>> futureResults = feedEngine(engine, count);
            int submitted = futureResults.size();

            LOG.info("Waiting for "+submitted+" results...");

            int completed;
            boolean waiting = true;
            while (waiting) {
                completed = 0;
                for (Future<ComputedResult> future : futureResults) {
                    if (future.isDone()) {
                        completed++;
                    }
                }
                LOG.info(completed +" completed");
                if (completed == submitted) {
                    waiting = false;
                } else {
                    // sleep a bit
                    try {
                        Thread.sleep(200);
                        DebugTools.printAvailableHeap();
                    } catch (InterruptedException e) {
                        //
                    }
                }
            }
        }
    }

    /**
     * Generates some AggregateSeries and feeds them to the statistics engine
     */
    public void testAggregateSeries() {
       int count = 5;
       StatisticsEngine engine = new StatisticsEngine();

       AggregateSeries aggregateSeries = new AggregateSeries(null);

       for (int i = 0; i < count; i++) {
           if (!almostOutOfHeap()) {
               aggregateSeries.add(generateRandomSeries(SIZE));
           }
       }

       List<Future<ComputedResult>> futureResults = engine.computeAll(aggregateSeries);

       int submitted = futureResults.size();

       LOG.info("Waiting for "+submitted+" results...");

       int completed;
       boolean waiting = true;
       while (waiting) {
           completed = 0;
           for (Future<ComputedResult> future : futureResults) {
               if (future.isDone()) {
                   completed++;
               }
           }
           LOG.info(completed +" completed");
           if (completed == submitted) {
               waiting = false;
           } else {
               // sleep a bit
               try {
                   Thread.sleep(200);
                   DebugTools.printAvailableHeap();
               } catch (InterruptedException e) {
                   //
               }
           }
       }
   }
}
