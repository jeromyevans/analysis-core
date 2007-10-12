package com.blueskyminds.analysis.basic.statistics;

import java.math.BigDecimal;

/**
 * Contains basic statistics results for a calculation
 *
 * This properties are grouped because they can be calculated in the same single pass over the series.
 *
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class BasicStats {

    private Integer size;
    private BigDecimal sum;
    private BigDecimal sumOfSquares;
    private BigDecimal min;
    private BigDecimal max;
    private BigDecimal mean;
    private BigDecimal stdDev;

    public BasicStats(Integer size, BigDecimal sum, BigDecimal sumOfSquares, BigDecimal min, BigDecimal max, BigDecimal mean, BigDecimal stdDev) {
        this.size = size;
        this.sum = sum;
        this.sumOfSquares = sumOfSquares;
        this.min = min;
        this.max = max;
        this.mean = mean;
        this.stdDev = stdDev;
    }

    public Integer getSize() {
        return size;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public BigDecimal getSumOfSquares() {
        return sumOfSquares;
    }

    public BigDecimal getMin() {
        return min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public BigDecimal getMean() {
        return mean;
    }

    public BigDecimal getStdDev() {
        return stdDev;
    }
}
