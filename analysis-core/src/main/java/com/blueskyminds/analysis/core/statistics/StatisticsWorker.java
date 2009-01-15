package com.blueskyminds.analysis.core.statistics;

import com.blueskyminds.analysis.core.series.Series;
import com.blueskyminds.analysis.core.series.DataAdapter;
import com.blueskyminds.analysis.core.series.Data;
import com.blueskyminds.analysis.core.engine.ComputeWorker;
import com.blueskyminds.analysis.core.engine.PartialResult;
import com.blueskyminds.analysis.core.engine.ComputationTask;
import com.blueskyminds.analysis.core.engine.ComputedResult;
import com.blueskyminds.homebyfive.framework.core.analysis.AnalysisTools;
import com.blueskyminds.homebyfive.framework.core.analysis.statistics.ComputeAdapter;
import com.blueskyminds.homebyfive.framework.core.analysis.statistics.BigDecimalAdapter;

import java.math.BigDecimal;
import java.util.*;

/**
 * Implements a ComputeWorker to calculate statistics for a Series
 * This is multithreaded and suited for large Series that require BigDecimal calculations
 *
 * Date Started: 24/08/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class StatisticsWorker extends ComputeWorker {

    private static final int TASKS = 2;

    /**
     * Initialsie a new StatisticsWorker
     * The worker creates two ComputationTasks ready for computation of the result derived from the
     *  series
     */
    public StatisticsWorker(Series series, ComputeAdapter adapter) {
        super(TASKS, series, adapter);
        init();
    }

    /**
     * Initialsie a new StatisticsWorker that can be re-used
     *
     * The worker creates two ComputationTasks ready for computation - call compute directly to use
     *  the worker
     */
    public StatisticsWorker() {
        super(TASKS, new DataAdapter());
        init();
    }

    /** Create the two ComputationTasks - these will be invoked when a computation needs to be performed */
    private void init() {
        addTask(new BasicStatsCalculator(getAdapter()));
        addTask(new MedianModeCalculator(getAdapter()));
    }

    /** Factory method used by the superclass to create a new instance of the ComputedResult
     *  Called prior to merging PartialResults into the ComputedResult
     *
     * @return Statistics instance
     */
    protected ComputedResult newResultInstance() {
        return new Statistics();
    }

    /** Stores the partial result from the median and mode calculation */
    private class MedianModeResult implements PartialResult<Statistics> {

        private BigDecimal median;
        private BigDecimal mode;

        public MedianModeResult(BigDecimal median, BigDecimal mode) {
            this.median = median;
            this.mode = mode;
        }

        public void merge(Statistics statistics) {
            statistics.setMedian(median);
            statistics.setMode(mode);
        }

        public boolean isPartial() {
            return true;
        }
    }

    /** A Runnable class that calculates the Mode and Median for a Series
     * Updates the mode and median values (ONLY) in Statistics
     */
    private class MedianModeCalculator extends ComputationTask {

        public MedianModeCalculator(ComputeAdapter adapter) {
            super(adapter);
        }

        /**
         * Calculate the median and mode of the series
         * These two calculations are combined because the mode can be determined from the sorted array already
         * used to find the median
         */
        public PartialResult compute(Object[] values) {
            BigDecimal median;

            if (values.length > 0) {
                Arrays.sort(values);

                // test if the length of the array is odd - two different methods to grab the middle number...
                if ((values.length & 0x01) > 0) {
                    // the series has an odd length, so the median is the middle value
                    median = ((Data) values[values.length >> 1]).getValue();
                } else {
                    // if the length of the list is even, find the middle pair of numbers and take the centre of those
                    BigDecimal medianLower = adapter.valueOf(values[(values.length >> 1) - 1]);
                    BigDecimal medianUpper = adapter.valueOf(values[values.length >> 1]);

                    median = medianLower.add((medianUpper.subtract(medianLower)).divide(AnalysisTools.TWO, AnalysisTools.mc));
                }
                return new MedianModeResult(median, calculateMode(values));
            } else {
                return null;
            }

        }

        /**
         * Calculate the mode from a an array of sorted values.
         * As the array is sorted into ascending order, one pass through the array from start to finish can count the
         * value that occurs the most times
         *
         * The array is assumed to be an array of BigDecimals
         *
         * @param sortedValues
         * @return mode
         */
        private BigDecimal calculateMode(Object[] sortedValues) {
            // the sorted array can be used to calculate the mode too  -
            long maxOccurances = 1;
            long currentOccurances = 1;  // the first value in the list
            int modeIndex = -1; // no mode exists
            BigDecimal mode = null;

            // note the loop starts at the 2nd index to compare it to the first
            for (int index = 1; index < sortedValues.length; index++) {
                // compare this value to the previous value in the array
                if (sortedValues[index].equals(sortedValues[index - 1])) {
                    // track the number of occurances of this value
                    currentOccurances++;
                } else {
                    // a new value has been encountered - reset the current counter
                    currentOccurances = 1;
                }

                if (currentOccurances > maxOccurances) {
                    // this is a new maximum number of occurances - track it
                    maxOccurances = currentOccurances;
                    // track the index - this could be the mode
                    modeIndex = index;
                } else {
                    // check if the current mode has been matched
                    if (currentOccurances == maxOccurances) {
                        // cancel the current mode - unless there are more occurances, a mode may not exist
                        // (there is no mode if the number of occurances of the two most frequent values are identical)
                        modeIndex = -1;
                    }
                }
            }

            // set the mode if a value was found...
            if (modeIndex >= 0) {
                mode = (BigDecimal) sortedValues[modeIndex];
            }

            return mode;
        }
    }


    /** Stores the partial results from the basic stats calculation */
    private class BasicStatsResult implements PartialResult<Statistics> {
        private Integer size;
        private BigDecimal sum;
        private BigDecimal sumOfSquares;
        private BigDecimal min;
        private BigDecimal max;
        private BigDecimal mean;
        private BigDecimal stdDev;

        public BasicStatsResult(Integer size, BigDecimal sum, BigDecimal sumOfSquares, BigDecimal min, BigDecimal max, BigDecimal mean, BigDecimal stdDev) {
            this.size = size;
            this.sum = sum;
            this.sumOfSquares = sumOfSquares;
            this.min = min;
            this.max = max;
            this.mean = mean;
            this.stdDev = stdDev;
        }

        /** Merge these results into the statistics */
        public void merge(Statistics statistics) {
            statistics.setSize(size);
            statistics.setSum(sum);
            statistics.setSumOfSquares(sumOfSquares);
            statistics.setMin(min);
            statistics.setMax(max);
            statistics.setMean(mean);
            statistics.setStdDev(stdDev);
        }

        public boolean isPartial() {
            return true;
        }
    }

    /**
     * A Runnable class that calculates the Mode and Median for a Series
     * Updates the mode and median values (ONLY) in Statistics
     */
    private class BasicStatsCalculator extends ComputationTask {

        public BasicStatsCalculator(ComputeAdapter adapter) {
            super(adapter);
        }

        /**
         * Preprocesses the series - this requires a single pass through all of the values to
         *  calculate the sum, sumOfSquares, min and max values.  These are all performed during the
         *  one iteration as an optimisation compared to iterating for each calculation.
         *
         *  The results are also used to calculate the mean and stddev if possible
         *
         * The array is assumed to be an array of BigDecimals
         * */
        public BasicStatsResult compute(Object[] values) {
            BigDecimal sum = AnalysisTools.ZERO;
            BigDecimal sumOfSquares = AnalysisTools.ZERO;
            BigDecimal minValue = new BigDecimal(Double.MAX_VALUE, AnalysisTools.mc);
            BigDecimal maxValue = new BigDecimal(Double.MIN_VALUE, AnalysisTools.mc);
            BigDecimal mean = null;
            BigDecimal stddev = null;
            int noOfValues = values.length;
            BigDecimal n = new BigDecimal(noOfValues, AnalysisTools.mc);
            BigDecimal value;

            for (Object val : values) {
                value = adapter.valueOf(val);
                sum = sum.add(value);
                sumOfSquares = sumOfSquares.add(value.multiply(value));

                if (value.compareTo(minValue) < 0) {
                    minValue = value;
                }

                if (value.compareTo(maxValue) > 0) {
                    maxValue = value;
                }
            }

            if (noOfValues > 0)
            {
                // calculate the mean
                mean = sum.divide(n, AnalysisTools.mc);
            }

            // calculate the standard deviation  note: need to avoid div0 and sqrt(0)
            if ((noOfValues > 1) && (sumOfSquares.compareTo(AnalysisTools.ZERO) > 0))
            {
                // unbiased stddev = sqrt(n*sum(x^2) - (sum(x))^2 / (n(n-1)))
                //stddev = AnalysisTools.sqrt(((n.multiply(sumOfSquares)).subtract(sum.multiply(sum))).divide(new BigDecimal(noOfValues * (noOfValues - 1)), AnalysisTools.mc));
                stddev = AnalysisTools.sqrt(((n.multiply(sumOfSquares)).subtract(sum.multiply(sum))).divide(n.multiply(n.subtract(BigDecimal.ONE)), AnalysisTools.mc));
            }

            return new BasicStatsResult(noOfValues, sum, sumOfSquares, minValue, maxValue, mean, stddev);
        }


    }

}
