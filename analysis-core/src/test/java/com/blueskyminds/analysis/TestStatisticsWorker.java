package com.blueskyminds.analysis;

import com.blueskyminds.framework.test.BaseTestCase;
import com.blueskyminds.analysis.core.series.UnivariateSeries;
import com.blueskyminds.analysis.statistics.StatisticsWorker;
import com.blueskyminds.analysis.statistics.Statistics;

import java.math.MathContext;
import java.math.RoundingMode;
import java.math.BigDecimal;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

/**
 * Tests the analysis enginer
 *
 * Date Started: 17/06/2007 (refactored out of existing code in the Analysis module)
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestStatisticsWorker extends BaseTestCase {

//    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestAnalysisPersistenceUnit";
//
//    public TestStatisticsWorker() {
//        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
//    }

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


    private UnivariateSeries generateTestSeries() {

        BigDecimal[] values = { new BigDecimal(7), new BigDecimal(9), new BigDecimal(-2), new BigDecimal(-9), new BigDecimal(2)};
        UnivariateSeries series = new UnivariateSeries(null, values);

        return series;
    }

        // ------------------------------------------------------------------------------------------------------

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

    // ------------------------------------------------------------------------------------------------------

    public void testStatisticsWorker2() {
        StatisticsWorker statisticsWorker = new StatisticsWorker();
        UnivariateSeries s = generateRandomSeries(100000);
        Statistics result = (Statistics) statisticsWorker.compute(s);

        assertNotNull(result);
    }

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
