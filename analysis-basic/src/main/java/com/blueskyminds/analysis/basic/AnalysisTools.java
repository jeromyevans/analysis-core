package com.blueskyminds.analysis.basic;

import com.blueskyminds.analysis.basic.statistics.BasicStats;
import com.blueskyminds.analysis.basic.statistics.ComputeAdapter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collection;

/**
 * Provides basic analysis/statistics  methods
 *
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class AnalysisTools {

    public static final int PRECISION = 18;  // maximum total digits
    public static final int SCALE = 4;       // 4 decimal points

    /** The default math context to use for all operations - important for division operations that
     * can result in non-exact results.  */
    public static final MathContext mc = new MathContext(PRECISION, RoundingMode.HALF_UP);

    public static final BigDecimal ZERO = new BigDecimal(0, mc);
    public static final BigDecimal TWO = new BigDecimal(2, mc);

    // ------------------------------------------------------------------------------------------------------

    /**
     *  Calculate the sum, sumOfSquares, min, max, mean and stddev values.  These are all performed during the
     *  one iteration as an optimisation compared to iterating for each calculation.
     *
     * @param adapter   - used to extract a BigDecimal value from each object
     * */
    public static BasicStats computeStats(Object[] values, ComputeAdapter adapter) {
        BigDecimal sum = ZERO;
        BigDecimal sumOfSquares = ZERO;
        BigDecimal minValue = new BigDecimal(Double.MAX_VALUE, mc);
        BigDecimal maxValue = new BigDecimal(Double.MIN_VALUE, mc);
        BigDecimal mean = null;
        BigDecimal stddev = null;
        int noOfValues = values.length;
        BigDecimal n = new BigDecimal(noOfValues, mc);
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
            mean = sum.divide(n, mc);
        }

        // calculate the standard deviation  note: need to avoid div0 and sqrt(0)
        if ((noOfValues > 1) && (sumOfSquares.compareTo(ZERO) > 0))
        {
            // unbiased stddev = sqrt(n*sum(x^2) - (sum(x))^2 / (n(n-1)))
            stddev = sqrt(((n.multiply(sumOfSquares)).subtract(sum.multiply(sum))).divide(n.multiply(n.subtract(BigDecimal.ONE)), AnalysisTools.mc));
        } 

        return new BasicStats(noOfValues, sum, sumOfSquares, minValue, maxValue, mean, stddev);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     *  Calculate the sum, sumOfSquares, min, max, mean and stddev values.  These are all performed during the
     *  one iteration as an optimisation compared to iterating for each calculation.
     *
     * @param adapter   - used to extract a long value from each object
     * */
    public static BasicStats computeStats(Collection values, ComputeAdapter adapter) {
        Object[] objects =  new Object[values.size()];
        values.toArray(objects);
        return computeStats(objects, adapter);
    }

    // ------------------------------------------------------------------------------------------------------

    /** An implementation of the Babylonian square root method (Newton's method) for BigDecimals
     * Probably needs some work:
     *    - if value.doubleValue > double.MAX_VALUE, choose a more appropriate initial value;
     *    - adjustment of precision and scale */
    public static BigDecimal sqrt(BigDecimal value) {
        BigDecimal x0 = ZERO;
        BigDecimal x1;
        double val = Math.sqrt(value.doubleValue());
        if (val != Double.NaN) {
            x1 = new BigDecimal(val);

            while (!x0.equals(x1)) {
                x0 = x1;
                x1 = value.divide(x0, AnalysisTools.mc);
                x1 = x1.add(x0);
                x1 = x1.divide(AnalysisTools.TWO, AnalysisTools.mc);
            }
        } else {
            x1 = null;
        }

        return x1;
    }
}
