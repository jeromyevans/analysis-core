package com.blueskyminds.analysis.core.statistics;

import com.blueskyminds.analysis.core.engine.ComputedResult;

import javax.persistence.Embeddable;
import javax.persistence.Basic;
import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * Simple summary statistics for a Series
 * The series is identified via the SeriesDescriptor
 *
 * The properties can be embedded for ORM persistence if desired
 *
 * Date Started: 19/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Embeddable
public class Statistics extends ComputedResult {

    private Integer size;
    private BigDecimal sum;
    private BigDecimal sumOfSquares;
    private BigDecimal min;
    private BigDecimal max;
    private BigDecimal mean;
    private BigDecimal stdDev;
    private BigDecimal median;
    private BigDecimal mode;

    // ------------------------------------------------------------------------------------------------------

    public Statistics() {
        super();
    }

    /** Default constructor for ORM */
//    protected Statistics() {
//        super(null);
//    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name = "SizeValue")
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Basic
    @Column(name = "SumValue")
    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    @Basic
    @Column(name = "SumOfSquaresValue")
    public BigDecimal getSumOfSquares() {
        return sumOfSquares;
    }

    public void setSumOfSquares(BigDecimal sumOfSquares) {
        this.sumOfSquares = sumOfSquares;
    }

    @Basic
    @Column(name = "MinValue")
    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    @Basic
    @Column(name = "MaxValue")
    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    @Basic
    @Column(name = "MeanValue")
    public BigDecimal getMean() {
        return mean;
    }

    public void setMean(BigDecimal mean) {
        this.mean = mean;
    }

    @Basic
    @Column(name = "StdDevValue")
    public BigDecimal getStdDev() {
        return stdDev;
    }

    public void setStdDev(BigDecimal stdDev) {
        this.stdDev = stdDev;
    }

    @Basic
    @Column(name = "MedianValue")
    public BigDecimal getMedian() {
        return median;
    }

    public void setMedian(BigDecimal median) {
        this.median = median;
    }

    @Basic
    @Column(name = "ModeValue")
    public BigDecimal getMode() {
        return mode;
    }

    public void setMode(BigDecimal mode) {
        this.mode = mode;
    }

    /**
     * Initialise the Statistics with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------
}
