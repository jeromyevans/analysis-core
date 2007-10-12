package com.blueskyminds.analysis.series;

import java.math.BigDecimal;

/**
 * A pair of BigDecimal values
 *
 * Date Started: 16/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class Pair {

    private BigDecimal left;
    private BigDecimal right;

    public Pair(BigDecimal left, BigDecimal right) {
        this.left = left;
        this.right = right;
    }

    // ------------------------------------------------------------------------------------------------------

    public BigDecimal getLeft() {
        return left;
    }

    public BigDecimal getRight() {
        return right;
    }
}
