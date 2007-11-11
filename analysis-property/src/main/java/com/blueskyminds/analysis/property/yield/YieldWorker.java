package com.blueskyminds.analysis.property.yield;

import com.blueskyminds.analysis.basic.statistics.BigDecimalAdapter;
import com.blueskyminds.analysis.core.series.Pair;
import com.blueskyminds.analysis.core.series.BivariateSeries;
import com.blueskyminds.analysis.core.series.Series;
import com.blueskyminds.analysis.core.engine.AggregateResult;
import com.blueskyminds.analysis.core.engine.ComputationTask;
import com.blueskyminds.analysis.core.engine.ComputedResult;
import com.blueskyminds.analysis.core.engine.ComputeWorker;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * Implements a ComputeWorker to calculate the yield from a sales and rental series
 *
 * The input series must be:
 *    an AggregateSeries of bivariate series
 *    In the BivariateSeries the left value in each pair is the sale price, and the right
 *   value the rental price.  Each bivariate series may contain any number of pairs.
 *
 * The output is:
 *   an AggregateResult containing a ComputedResult for each BivariateSeries in the input series
 *
 * Date Started: 25/09/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class YieldWorker extends ComputeWorker {

    private static final int TASKS = 1;
    private static final BigDecimal FIFTY_TWO = new BigDecimal("52.0");

    /**
     * Initialsie a new YieldWorker
     *
     * The worker creates two ComputationTasks ready for computation - call compute directly to use
     *  the worker
     */
    public YieldWorker(Series inputSeries) {
        super(TASKS, inputSeries, new BigDecimalAdapter());
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create the two ComputationTasks - these will be invoked when a computation needs to be performed */
    private void init() {
        addTask(new YieldCalculator());
    }

    // ------------------------------------------------------------------------------------------------------

    /** Factory method required by the superclass - not used in this implementation because there
     *  is only one task (no merging of PartialResult's)
     * @return an AggregateResult
     */
    protected ComputedResult newResultInstance() {
        return new AggregateResult();
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /**
     *  A Runnable class that calculates the Yield for multiple series
     *
     */
    private class YieldCalculator extends ComputationTask {

        // ------------------------------------------------------------------------------------------------------

        /**
         * Calculate the yield for a bivariate series [saleprice, rentalprice]
         * @param vals is an array of bivariate series
         * @return AggregateResult of YieldResults, one for each series in the input
         */
        public ComputedResult compute(Object[] vals) {
            BivariateSeries series;

            BigDecimal rent;
            BigDecimal sale;
            BigDecimal yield;
            AggregateResult result = new AggregateResult();
            YieldResults yieldResults;
            Pair pair;
            boolean skip;

            for (Object o : vals) {
                series = (BivariateSeries) o;
                Iterator<Pair> iterator = series.iterator();
                yieldResults = new YieldResults(series.getSeriesDescriptor());
                skip = false;
                while ((iterator.hasNext()) && (!skip)) {
                    pair = iterator.next();
                    sale = pair.getLeft();
                    rent = pair.getRight();

                     // (rentPerWeek * 52) / purchasePrice
                    if ((sale != null) && (sale.compareTo(BigDecimal.ZERO) > 0)) {
                        yield = rent.multiply(FIFTY_TWO).divide(sale, BigDecimal.ROUND_HALF_DOWN);
                        yieldResults.add(yield);
                    } else {
                        // couldn't calculate the yield
                        skip = true;
                        break;
                    }
                }
                result.addComputedResult(yieldResults);
            }

            return result;
        }

    }

}
