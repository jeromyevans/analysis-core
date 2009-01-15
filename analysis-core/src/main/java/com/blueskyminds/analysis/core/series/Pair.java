package com.blueskyminds.analysis.core.series;

import java.math.BigDecimal;

/**
 * A pair of BigDecimal values
 *
 * Date Started: 16/09/2006
 *
 * History:
 *
 * Copyright (c) 2009 Blue Sky Minds Pty Ltd
 */
public class Pair {

    private Data left;
    private Data right;

    public Pair(BigDecimal left, BigDecimal right) {
        this.left = new Data(left, null);
        this.right = new Data(right, null);
    }

    public Pair(Data left, Data right) {
        this.left = left;
        this.right = right;
    }

    public Data getLeft() {
        return left;
    }

    public Data getRight() {
        return right;
    }

    public BigDecimal getLeftValue() {
        return left.getValue();
    }

    public BigDecimal getRightValue() {
        return right.getValue();
    }
}
