package com.blueskyminds.framework.analysis;

import com.blueskyminds.framework.test.BaseTestCase;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * Unit tests to get familiar with the Platform management beans
 *
 * Date Started: 25/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestMXBeans extends BaseTestCase {


    public TestMXBeans(String string) {
        super(string);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestMXBeans with default attributes
     */
    private void init() {
    }

    public void testMXBeans() {
        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        MemoryUsage memUsage = mem.getHeapMemoryUsage();
        System.out.println("Committed: "+memUsage.getCommitted());
        System.out.println("Used: "+memUsage.getUsed()+" ("+((Long) memUsage.getUsed()).doubleValue()/memUsage.getCommitted()*100.0+"%)");
        System.out.println("Max: "+memUsage.getMax());
    }

    // ------------------------------------------------------------------------------------------------------
}
