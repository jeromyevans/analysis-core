package com.blueskyminds.framework.analysis;

import junit.framework.TestCase;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 * Familiarity with commons-math
 *
 * Date Started: 11/11/2007
 * <p/>
 * History:
 */
public class CommonsMathTest extends TestCase {

    public void testStatsLibrary() {
        DescriptiveStatistics stats1 = DescriptiveStatistics.newInstance();
        stats1.addValue(10);
        stats1.addValue(20);
        stats1.addValue(30);
        stats1.addValue(40);
        stats1.addValue(50);

        double mean = stats1.getMean();

    }
}
