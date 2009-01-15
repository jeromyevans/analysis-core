package com.blueskyminds.analysis.core.statistics;

import com.blueskyminds.analysis.core.series.UnivariateSeries;
import com.blueskyminds.analysis.core.series.Data;
import com.blueskyminds.analysis.core.statistics.StatisticsWorker;
import com.blueskyminds.analysis.core.statistics.Statistics;

import java.math.MathContext;
import java.math.RoundingMode;
import java.math.BigDecimal;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;
import junit.framework.TestCase;

/**
 * Tests the analysis enginer
 *
 * Date Started: 17/06/2007 (refactored out of existing code in the Analysis module)
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class StatisticsWorkerTest extends TestCase {

    private UnivariateSeries generateRandomSeries(int size) {
        UnivariateSeries series = new UnivariateSeries(null);
        MathContext mc = new MathContext(18, RoundingMode.HALF_UP);
        int d;
        RandomData r = new RandomDataImpl();
        for (int i = 0; i < size; i++) {
            d = ((Double) r.nextGaussian(1000.0, 50.0)).intValue();
            series.add(new Data(new BigDecimal(d, mc)));
        }
        return series;
    }

    private UnivariateSeries generateTestSeries() {
        BigDecimal[] values = { new BigDecimal(7), new BigDecimal(9), new BigDecimal(-2), new BigDecimal(-9), new BigDecimal(2)};
        UnivariateSeries series = new UnivariateSeries(null, values);

        return series;
    }

    /** Asserts that the statistics worker performs correct calculations */
    public void testStatisticsWorkerExact() {
        StatisticsWorker statisticsWorker = new StatisticsWorker();
        UnivariateSeries s = generateTestSeries();
        Statistics result = (Statistics) statisticsWorker.compute(s);

        assertNotNull(result);
        assertEquals(new BigDecimal(9), result.getMax());
        assertEquals(new BigDecimal(-9), result.getMin());
        assertEquals((Integer) 5, (Integer) result.getSize());
        assertEquals(new BigDecimal("1.4"), result.getMean());
        assertEquals(new BigDecimal("7.23187389"), result.getStdDev().round(new MathContext(9, RoundingMode.HALF_UP)));
        assertNull(result.getMode());
        assertEquals(new BigDecimal("2"), result.getMedian());
    }

    /** Asserts that the statistics worker runs with a large series */
    public void testLargeSeries() {
        StatisticsWorker statisticsWorker = new StatisticsWorker();
        UnivariateSeries s = generateRandomSeries(100000);
        Statistics result = (Statistics) statisticsWorker.compute(s);

        assertNotNull(result);
    }

    /**
     * Tests that the same worker instance can be reused for many series
     */
    public void testStatisticsWorkerReuse() {
        StatisticsWorker statisticsWorker = new StatisticsWorker();

        // use the worker  multiple times
        UnivariateSeries s = generateRandomSeries(100000);
        Statistics result1 = (Statistics) statisticsWorker.compute(s);
        s = generateRandomSeries(100000);
        Statistics result2 = (Statistics) statisticsWorker.compute(s);
        s = generateRandomSeries(100000);
        Statistics result3 = (Statistics) statisticsWorker.compute(s);
        s = generateRandomSeries(100000);
        Statistics result4 = (Statistics) statisticsWorker.compute(s);

        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertNotNull(result4);
    }
}
