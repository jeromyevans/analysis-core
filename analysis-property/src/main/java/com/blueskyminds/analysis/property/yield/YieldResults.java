package com.blueskyminds.analysis.property.yield;

import com.blueskyminds.analysis.core.engine.ComputedResult;
import com.blueskyminds.analysis.core.series.SeriesDescriptor;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * Results of a yield calculations by the YieldWorker
 *
 * Date Started: 25/09/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class YieldResults implements ComputedResult {

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


    /**
     * This flag indicates whether this result is part of a ComputedResult and needs to be merged
     * with other parts.
     *
     * @return false
     * @see com.blueskyminds.analysis.core.engine.PartialResult
     */
    public boolean isPartial() {
        return false;  
    }
}
