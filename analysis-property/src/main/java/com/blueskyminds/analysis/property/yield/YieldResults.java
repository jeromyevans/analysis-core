package com.blueskyminds.analysis.property.yield;

import com.blueskyminds.analysis.engine.ComputedResult;
import com.blueskyminds.analysis.series.SeriesDescriptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.LinkedList;

/**
 * Results of a yield calculations by the YieldWorker
 *
 * Date Started: 25/09/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class YieldResults extends ComputedResult {

    private SeriesDescriptor descriptor;
    private List<BigDecimal> yield;

    public YieldResults(SeriesDescriptor descriptor) {
        this.descriptor = descriptor;
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the YieldResults with default attributes
     */
    private void init() {
        yield = new LinkedList<BigDecimal>();
    }

    public boolean add(BigDecimal value) {
        return yield.add(value);
    }

    // ------------------------------------------------------------------------------------------------------

    public SeriesDescriptor getDescriptor() {
        return descriptor;
    }

    public BigDecimal get(int index) {
        if (index < size()) {
            return yield.get(index);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public int size() {
        return yield.size();
    }
}
